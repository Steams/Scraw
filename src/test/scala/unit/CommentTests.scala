package com.steams.scraw.tests

import scala.io.Source

import org.scalatest._
import net.liftweb.json._

import com.steams.scraw.utils.JsonHandler
import com.steams.scraw.comments._

class CommentSpec extends FlatSpec with Matchers with JsonHandler {

  behavior of "Comment Factory"

  it must "parse a single comments without replies" in {

    val source = Source.fromURL(getClass.getResource("/singleComment.json"))

    val content = try source.mkString finally source.close()

    val comment_json : JValue = parse(content)

    val comment : Comment = CommentExtractor.buildCommentThread(comment_json,0)

    comment.name should be {(comment_json \ "name").extract[String]}
    comment.author should be {(comment_json \ "author").extract[String]}
    comment.id should be {(comment_json \ "id").extract[String]}
    comment.depth should be (0)
  }
}

/*
 override val id : String,
 override val name : String,
 val approved_by : String,
 val author : String,
 val author_flair_css_class : String,
 val author_flair_text : String,
 val banned_by : String,
 val body : String,
 val body_html : String,
 val edited : Boolean,
 val gilded : Int,
 val likes : Boolean,
 val link_author : Option[String],
 val link_id : String,
 val link_title : Option[String],
 val link_url : Option[String],
 val num_reports : Int,
 val parent_id : String,
 val replies : Option[List[Commentifiable]],
 val saved : Boolean,
 val score : Int,
 val score_hidden : Boolean,
 val subreddit : String,
 val subreddit_id : String,
 val distinguished : String,
 val depth : Int
 */
// test for multiple comment depths
