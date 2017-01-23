package com.steams.scraw.comments

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.JsonHandler
import com.steams.scraw.http.HttpService

object CommentService extends JsonHandler {

  def getStream( endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : CommentStream = {

    val response_body = HttpService.get(endpoint,reddit.access_token,params)

    val jval = Jsonator.parse(response_body)

    //get the second child of the returned array. This structure contains the comments for the post
    val comments_listing = jval.children(1).property("data")

    val comments_list = comments_listing.property("children").children

    val comments = CommentsFactory.buildCommentForrest(comments_list)

    val comment_stream = CommentStream(
      (comments_listing \ "modhash"),
      (comments_listing \ "after"),
      (comments_listing \ "before"),
      comments)

    return comment_stream
  }
}
