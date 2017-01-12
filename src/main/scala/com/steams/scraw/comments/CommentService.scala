package com.steams.scraw.comments

import net.liftweb.json._

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.JsonHandler
import com.steams.scraw.http.HttpService

object CommentService extends JsonHandler {

  def buildComment(root_comment : JValue, sub_comments : Option[List[Comment]] = None) : Comment = {
      val comment = Comment.builder()
        .id((root_comment \ "id").extract[String])
        .name((root_comment \ "name").extract[String])
        .approved_by((root_comment \ "approved_by").extract[String])
        .author((root_comment \ "author").extract[String])
        .author_flair_css_class((root_comment \ "author_flair_css_class").extract[String])
        .author_flair_text((root_comment \ "author_flair_text").extract[String])
        .banned_by((root_comment \ "banned_by").extract[String])
        .body((root_comment \ "body").extract[String])
        .body_html((root_comment \ "body_html").extract[String])
        // .edited((root_comment \ "edited").extract[Boolean])
        .gilded((root_comment \ "gilded").extract[Int])
        .likes((root_comment \ "likes").extract[Boolean])
        .link_author((root_comment \ "link_author").extract[Option[String]])
        .link_id((root_comment \ "link_id").extract[String])
        .link_title((root_comment \ "link_title").extract[Option[String]])
        .link_url((root_comment \ "link_url").extract[Option[String]])
        .num_reports((root_comment \ "num_reports").extract[Int])
        .parent_id((root_comment \ "parent_id").extract[String])
        .saved((root_comment \ "saved").extract[Boolean])
        .score((root_comment \ "score").extract[Int])
        .score_hidden((root_comment \ "score_hidden").extract[Boolean])
        .subreddit((root_comment \ "subreddit").extract[String])
        .subreddit_id((root_comment \ "subreddit_id").extract[String])
        .distinguished((root_comment \ "distinguished").extract[String])
        .replies(sub_comments)
        .build()
      return comment
  }

  def buildCommentThread(root_comment : JValue) : Comment = {
    if((root_comment \"replies").children.size <= 0){
      val comment = buildComment(root_comment)
      return comment
    } else {
      val sub_comments = for(
        x <- (root_comment \ "replies" \ "data" \ "children").children
        if (x \ "kind").extract[String] == "t1"
      ) yield { buildCommentThread(x \ "data") }

      //now u need to construct the comment and just set the sub comments
      val comment = buildComment(root_comment,Some(sub_comments))
      return comment
    }
  }

  def getStream( endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : CommentStream = {

    val response_body = HttpService.get(endpoint,reddit.access_token,params)

    val jval = parse(response_body)

    //get the second child of the returned array. This structure contains the comments for the post
    val comments_listing = jval.children(1) \ "data"
    val comments_list = (comments_listing \ "children").children

    val comments = for(root_comment <- comments_list) yield {
      buildCommentThread(root_comment \ "data")
    }

    val comment_stream = CommentStream(
      (comments_listing \ "modhash"),
      (comments_listing \ "after"),
      (comments_listing \ "before"),
      comments)

    return comment_stream
  }
}
