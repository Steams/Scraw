package com.steams.scraw.tests

import scala.io.Source

import org.scalatest._

import com.steams.scraw.utils.JsonHandler
import com.steams.scraw.comments._
import com.steams.scraw.{Reddit}


class CommentSpec extends FlatSpec with Matchers with JsonHandler {
  /**
    * What to test
    * - Plain comment parsing -> parse a comment thread with 1 comment
    * - Multiple Flat Plain comment parsing -> parse a comment thread with multiple top level comments
    * - Nested plain comment parsing -> Factory should properly parse a comment thread with 1 comment
    * - single "more" link parsing -> resolve a comment thread with 1 "more comments" link
    * - multiple "more" link parsing -> resolve a comment thread with multiple top level comments which each have a more link in their replies
    * - nested "more" link parsing -> resolve a comment thread with 1 "more comments" whose response contains another more link
    * - multiple comments and "more links"
  */


  def compareComment(comment : Comment, json_comment : JsonValue, depth : Int){
    comment.id should be {json_comment.property("id").toString}
    comment.name should be {json_comment.property("name").toString}
    comment.approved_by should be {json_comment.property("approved_by").toString}
    comment.author should be {json_comment.property("author").toString}
    comment.author_flair_css_class should be {json_comment.property("author_flair_css_class").toString}
    comment.author_flair_text should be {json_comment.property("author_flair_text").toString}
    comment.banned_by should be {json_comment.property("banned_by").toString}
    comment.body should be {json_comment.property("body").toString}
    comment.body_html should be {json_comment.property("body_html").toString}
    comment.edited should be {json_comment.property("edited").toBoolean}
    comment.gilded should be {json_comment.property("gilded").toInt}
    comment.likes should be {json_comment.property("likes").toBoolean}
    comment.link_author should be {json_comment.property("link_author").toOptionalString}
    comment.link_id should be {json_comment.property("link_id").toString}
    comment.link_title should be {json_comment.property("link_title").toOptionalString}
    comment.link_url should be {json_comment.property("link_url").toOptionalString}
    comment.num_reports should be {json_comment.property("num_reports").toInt}
    comment.parent_id should be {json_comment.property("parent_id").toString}
    comment.saved should be {json_comment.property("saved").toBoolean}
    comment.score should be {json_comment.property("score").toInt}
    comment.score_hidden should be {json_comment.property("score_hidden").toBoolean}
    comment.subreddit should be {json_comment.property("subreddit").toString}
    comment.subreddit_id should be {json_comment.property("subreddit_id").toString}
    comment.distinguished should be {json_comment.property("distinguished").toString}
    comment.depth should be {depth}
  }

  //this is insufficient because it doesnt handle "more" links in replies
  //it might not need to change because these are just tests. i can have a seperate facility for comparing more links
  def compareThreads(comment : Comment,json_comment : JsonValue, depth : Int) : Unit = {
    compareComment(comment, json_comment, depth)

    val repliesAndRaw = comment.replies match {
      case Some(_) => comment.replies.get.map{ x => x.asInstanceOf[Comment]} zip (json_comment \ "replies" \ "data" \ "children").children.map{ x => x \ "data"}
      case None => Nil
    }

    for( (comment,json) <- repliesAndRaw ){
      compareThreads(comment, json, depth +1)
    }
  }

  implicit val instance : Reddit = Reddit("Username","Password","AccessToken")

  behavior of "Comment Factory"

  it must "handle comment thread consisting of a single comment with no replies" in {

    val source = Source.fromURL(getClass.getResource("/singleComment.json"))

    val content = try { source.mkString } finally { source.close() }

    val comments_json : List[JsonValue] = parse(content).children

    val comments : List[Commentifiable] = CommentsFactory.buildCommentForrest(
      comments_json,
      LinkId((comments_json(0) \ "data" \ "link_id").toString),
      instance
    )

    comments.length should be {1}

    val comment : Comment = comments(0).asInstanceOf[Comment]

    val json_comment = comments_json(0) \ "data"

    compareComment(comment,json_comment,0)
  }

  it must "handle multiple a nested comments" in {
    val source = Source.fromURL(getClass.getResource("/nestedComments.json"))

    val content = try { source.mkString } finally { source.close() }

    val comments_json : List[JsonValue] = parse(content).children

    val comments : List[Commentifiable] = CommentsFactory.buildCommentForrest(
      comments_json,
      LinkId((comments_json(0) \ "data" \ "link_id").toString),
      instance)

    comments.length should be {comments_json.size}

    val commentsAndRaw =  (comments.map{x => x.asInstanceOf[Comment]} zip comments_json.map{x => x \ "data"})

    for( (comment,json) <- commentsAndRaw ){
      compareThreads(comment, json, 0)
    }

  }

  it must "handle a comment thread consisting of single more comments link and fetch the sub comments" in {

    val input_source = Source.fromURL(getClass.getResource("/singleMoreComments.json"))
    val input_content = try { input_source.mkString } finally { input_source.close() }
    val input_json : List[JsonValue] = parse(input_content).children

    val expected_source = Source.fromURL(getClass.getResource("/singleMoreCommentsExpanded.json"))
    val expected_content = try { expected_source.mkString } finally { expected_source.close() }
    val expected_json : List[JsonValue] = parse(expected_content).children

    val mock = new CommentServiceMock{
      override def getMoreCommentsFlat(
        link_id : LinkId,
        children : List[String],
        reddit : Reddit) : CommentStream = {

        println("Using Mock Comments Service")

        val comments = CommentsFactory.buildCommentsList(expected_json, link_id, reddit)

        val comment_stream = CommentStream("","","",comments)

        return comment_stream
      }
    }

    CommentService.implementation = Some(mock)

    val link_json : JsonValue = input_json(0) \ "data"

    val comments : List[Commentifiable] = CommentsFactory.buildCommentForrest(
      input_json,
      LinkId((link_json \ "testing_linkid").toString),
      instance
    )

    comments.length should be {1}

    val link : CommentsLink = comments(0).asInstanceOf[CommentsLink]

    link.count should be {expected_json.size}

    link.children.mkString(",") should be { link_json.property("children").children.mkString(",") }


    val result_stream = link.getFlattened

    //really need to stop using itterators to store lists you idiot
    // result_stream.size should be {link.count}

    var actually_itterated = false

    for( (comment,json) <- result_stream zip expected_json.map( x => x \ "data") ){
      compareComment(comment.asInstanceOf[Comment], json, 0)
      actually_itterated = true
    }

    actually_itterated should be {true}

    // have the factory expose a method to generate the flat list structure
    // compare the flat list structure with the json in file
    // then have the factory generate the nested structure
    // factory ultimately returns another CommentStream

  }

  it must "handle a nested comments with more links" in {

  }

  it must "handle more links returned within more links" in {

  }

}
