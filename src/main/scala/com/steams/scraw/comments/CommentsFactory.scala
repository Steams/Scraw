package com.steams.scraw.comments

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.JsonHandler

object CommentsFactory extends JsonHandler {

  def fromForrest( comments_list : List[JsonValue], link_id : LinkId, reddit : Reddit) : List[Commentifiable] = {

     val comments = for(root_comment <- comments_list) yield {

      if(root_comment.property("kind").toString == "t1" ){

        CommentsFactory.buildCommentThread(root_comment \"data", 0, link_id, reddit)

      } else {

        CommentsFactory.buildCommentsLink(root_comment \ "data", 0, link_id, reddit)
      }

    }

    return comments

  }

  //for building nested comments from flat lists of comments
  // TODO ERROR : ur adding morecomments links to all comments as replies when there are no replies. fix this
  def fromList( comments_list : List[JsonValue], link_id : LinkId, reddit : Reddit) : List[Commentifiable] = {

    def buildCommentFromList(
      root : JsonValue,
      potential_children : Map[String,List[JsonValue]],
      depth : Int,
      link_id : LinkId,
      reddit : Reddit
    ) : Comment = {

      // val replies = for(children <- potential_children.get(root \ "name")) yield {
      //   children.map{ reply =>
      //     if(reply.property("kind") == "t1") {
      //       buildCommentFromList(reply,potential_children,depth +1,link_id,reddit)
      //     } else {
      //       buildCommentsLink(reply, depth + 1: Int, link_id: LinkId, reddit: Reddit)
      //     }
      //   }
      // }

      val replies = potential_children.get(root \ "name") match {
        case None => None
        case Some(x) => Some(
          x.map{ reply =>
            if(reply.property("kind").toString() == "t1") {
              buildCommentFromList(reply \ "data",potential_children,depth +1,link_id,reddit)
            } else {
              buildCommentsLink(reply \ "data", depth + 1: Int, link_id: LinkId, reddit: Reddit)
            }
          })
      }

      return buildComment(root, depth, replies)
    }

    //map comment IDs(full name) to values
    val comments : Map[String,JsonValue] = comments_list.map( c => ((c \ "data" \ "name").toString, c)).toMap

    //map parent_id's to list of their children
    val potential_children : Map[String,List[JsonValue]] = comments_list.groupBy( _ \ "data" \ "parent_id" )

    val roots = comments.filter { case (name,comment) => comments.get( comment \ "data" \ "parent_id") == None}
    // ^above gets comments that have no parent in the given list, these are the root comments

    val results = roots.map {case (name,root) =>
      if(root.property("kind").toString() == "t1") {
          buildCommentFromList(root \ "data", potential_children, 0, link_id, reddit)
        } else {
          buildCommentsLink(root \ "data", 0, link_id: LinkId, reddit: Reddit)
        }
    }

    return results.asInstanceOf[List[Commentifiable]]
  }



  def buildCommentThread(
    root_comment : JsonValue,
    depth : Int,
    link_id : LinkId,
    reddit : Reddit
  ) : Comment = {

    if(root_comment.property("replies").children.size <= 0){

      val comment = buildComment(root_comment, depth)
      return comment
    } else {

      val sub_comments = for(x <- root_comment.property("replies").property("data").property("children").children) yield {
        if(x.property("kind").toString == "t1" ){

          buildCommentThread(x.property("data"), depth +1, link_id, reddit)
        } else {

          buildCommentsLink(x.property("data"), depth +1, link_id, reddit)
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

  def buildCommentsLink(link_data : JsonValue, depth : Int, link_id : LinkId, reddit : Reddit) : CommentsLink = {
    return CommentsLink(
      name = link_data \ "name",
      parent_id = link_data \ "parent_id",
      count = link_data \ "count",
      children = (link_data \ "children").children.map(x => x.toString),
      link_id = link_id,
      reddit = reddit,
      depth = depth
    )
  }

  def buildCommentsList( comments_list : List[JsonValue], link_id : LinkId, reddit : Reddit) : List[Commentifiable] = {
    val comments = for(root_comment <- comments_list) yield {

      if(root_comment.property("kind").toString == "t1" ){

        buildComment(root_comment.property("data"), 0)

      } else {
        CommentsFactory.buildCommentsLink(root_comment, 0, link_id, reddit)
      }

    }

    return comments
  }
}
