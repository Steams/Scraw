package com.steams.scraw.tests

import scala.io.Source
import scala.language.implicitConversions

import org.scalatest._

import com.steams.scraw.utils.JsonHandler
import com.steams.scraw.utils.JsonParsing._
import com.steams.scraw.comments._


class CommentSpec extends FlatSpec with Matchers with JsonHandler {
  /* Tests to write
   - parse comment thread consisting of single comment with no replies
   - parse single more comment link
   - parse nested comments
   - parse nested comments with more links
   */
  behavior of "Comment Factory"

  it must "parse comment thread consisting of a single comment with no replies" in {

    val source = Source.fromURL(getClass.getResource("/singleComment.json"))

    val content = try { source.mkString } finally { source.close() }

    val comments_json : List[JsonValue] = parse(content).children

    val comments : List[Commentifiable] = CommentExtractor.buildCommentForrest(comments_json)

    comments.length should be {1}

    val comment : Comment = comments(0).asInstanceOf[Comment]

    val json_comment = comments_json(0)

    comment.name should be {json_comment \"data" \ "name" toString}
    comment.author should be {json_comment \"data" \ "author" toString}
    comment.id should be {json_comment \"data" \ "id" toString}
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
