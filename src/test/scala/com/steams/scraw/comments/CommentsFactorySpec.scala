package com.steams.scraw.comments

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.JsonHandler
import org.scalatest._
import scala.io.Source

/**
  * What to test
  * - Build single comment
  * - Build comment thread
  * - Plain comment parsing -> parse a comment thread with 1 comment
  * - Multiple Flat Plain comment parsing -> parse a comment thread with multiple top level comments
  * - Nested plain comment parsing -> Factory should properly parse a comment thread with 1 comment
  * - single "more" link parsing -> resolve a comment thread with 1 "more comments" link
  * - multiple "more" link parsing -> resolve a comment thread with multiple top level comments which each have a more link in their replies
  * - nested "more" link parsing -> resolve a comment thread with 1 "more comments" whose response contains another more link
  * - multiple comments and "more links"
  */

class CommentsFactorySpec extends FlatSpec with Matchers with JsonHandler{

  implicit val instance : Reddit = Reddit("Username","Password","AccessToken")

  behavior of "CommentsFactory"

  it must "Properly build a single comment with no replies from Json" in {
    val source = Source.fromURL(getClass.getResource("/singleComment.json"))

    val content = try { source.mkString } finally { source.close() }

    val comment_json : JsonValue = parse(content) \ "data"

    val comment = CommentsFactory.buildComment(comment_json, 0, None)

    compareComment(comment,comment_json,0)
  }

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
