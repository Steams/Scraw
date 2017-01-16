package com.steams.scraw.comments

import net.liftweb.json._

import com.steams.scraw.utils.JsonHandler

object CommentExtractor extends JsonHandler {

  def buildCommentForrest( comments_list : List[JValue]) : List[Commentifiable] = {

     val comments = for(root_comment <- comments_list) yield {

      if((root_comment \ "kind").extract[String] == "t1" ){

        CommentExtractor.buildCommentThread(root_comment \ "data", 0)

      } else {

        CommentExtractor.buildCommentsLink(root_comment, 0)
      }

    }

    return comments

  }

  def buildCommentThread(root_comment : JValue, depth : Int) : Comment = {
    if((root_comment \"replies").children.size <= 0){
      val comment = buildComment(root_comment, depth)
      return comment
    } else {
      val sub_comments = for(x <- (root_comment \ "replies" \ "data" \ "children").children) yield {
        if((x \ "kind").extract[String] == "t1" ){
          buildCommentThread(x \ "data", depth +1)
        } else {
          buildCommentsLink(x \ "data", depth +1)
        }
      }

      //now u need to construct the comment and just set the sub comments
      val comment = buildComment(root_comment, depth, Some(sub_comments))
      return comment
    }
  }

  def buildComment(root_comment : JValue, depth: Int, sub_comments : Option[List[Commentifiable]] = None)
      : Comment = {
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
        .depth(depth)
        .build()
      return comment
  }

  def buildCommentsLink(link_data : JValue, depth : Int) : CommentsLink = {
    return CommentsLink(
      (link_data \ "name").extract[String],
      (link_data \ "parent_id").extract[String],
      (link_data \ "count").extract[Int],
      depth
    )
  }
}
