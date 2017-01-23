package com.steams.scraw.tests

import scala.io.Source

import org.scalatest._

import com.steams.scraw.utils.JsonHandler
import com.steams.scraw.comments._


class CommentSpec extends FlatSpec with Matchers with JsonHandler {

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

  behavior of "Comment Factory"

  it must "parse comment thread consisting of a single comment with no replies" in {

    val source = Source.fromURL(getClass.getResource("/singleComment.json"))

    val content = try { source.mkString } finally { source.close() }

    val comments_json : List[JsonValue] = parse(content).children

    val comments : List[Commentifiable] = CommentsFactory.buildCommentForrest(comments_json)

    comments.length should be {1}

    val comment : Comment = comments(0).asInstanceOf[Comment]

    val json_comment = comments_json(0) \ "data"

    compareComment(comment,json_comment,0)
  }

  it must "parse a nested comments" in {
    val source = Source.fromURL(getClass.getResource("/nestedComments.json"))

    val content = try { source.mkString } finally { source.close() }

    val comments_json : List[JsonValue] = parse(content).children

    val comments : List[Commentifiable] = CommentsFactory.buildCommentForrest(comments_json)

    comments.length should be {comments_json.size}

    val commentsAndRaw =  (comments.map{x => x.asInstanceOf[Comment]} zip comments_json.map{x => x \ "data"})

    for( (comment,json) <- commentsAndRaw ){
      compareThreads(comment, json, 0)
    }

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
  }

  it must "parse a comment thread consisting of single more comments link and fetch the sub comments" in {

  }

  it must "parse a nested comments with more links" in {

  }

}
