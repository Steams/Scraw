package com.steams.scraw.comments

import com.steams.scraw.utils.JsonHandler

object CommentsFactory extends JsonHandler {

  def buildCommentForrest( comments_list : List[JsonValue]) : List[Commentifiable] = {

     val comments = for(root_comment <- comments_list) yield {

      if(root_comment.property("kind").toString == "t1" ){

        CommentsFactory.buildCommentThread(root_comment.property("data"), 0)

      } else {

        CommentsFactory.buildCommentsLink(root_comment, 0)
      }

    }

    return comments

  }

  def buildCommentThread(root_comment : JsonValue, depth : Int) : Comment = {
    if(root_comment.property("replies").children.size <= 0){
      val comment = buildComment(root_comment, depth)
      return comment
    } else {
      val sub_comments = for(x <- root_comment.property("replies").property("data").property("children").children) yield {
        if(x.property("kind").toString == "t1" ){
          buildCommentThread(x.property("data"), depth +1)
        } else {
          buildCommentsLink(x.property("data"), depth +1)
        }
      }

      //now u need to construct the comment and just set the sub comments
      val comment = buildComment(root_comment, depth, Some(sub_comments))
      return comment
    }
  }

  def buildComment(root_comment : JsonValue, depth: Int, sub_comments : Option[List[Commentifiable]] = None)
      : Comment = {
      val comment = Comment.builder()
        .id(root_comment \ "id")
        .name(root_comment \ "name")
        .approved_by(root_comment \ "approved_by")
        .author(root_comment \ "author")
        .author_flair_css_class(root_comment \ "author_flair_css_class")
        .author_flair_text(root_comment \ "author_flair_text")
        .banned_by(root_comment \ "banned_by")
        .body(root_comment \ "body")
        .body_html(root_comment \ "body_html")
        // .edited((root_comment \ "edited").extract[Boolean])
        .gilded(root_comment \ "gilded")
        .likes(root_comment \ "likes")
        .link_author(root_comment \ "link_author")
        .link_id(root_comment \ "link_id")
        .link_title(root_comment \ "link_title")
        .link_url(root_comment \ "link_url")
        .num_reports(root_comment \ "num_reports")
        .parent_id(root_comment \ "parent_id")
        .saved(root_comment \ "saved")
        .score(root_comment \ "score")
        .score_hidden(root_comment \ "score_hidden")
        .subreddit(root_comment \ "subreddit")
        .subreddit_id(root_comment \ "subreddit_id")
        .distinguished(root_comment \ "distinguished")
        .replies(sub_comments)
        .depth(depth)
        .build()

    // these use \ because it was faster to define the \ method on my JsonValue type than to change these to .property calls IE purely Lazyness, please refactor. TODO

      return comment
  }

  def buildCommentsLink(link_data : JsonValue, depth : Int) : CommentsLink = {
    return CommentsLink(
      link_data.property("name"),
      link_data.property("parent_id"),
      link_data.property("count"),
      depth
    )
  }
}
