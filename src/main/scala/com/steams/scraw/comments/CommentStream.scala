package com.steams.scraw.comments

import com.steams.scraw.http.Endpoint
import scala.collection.mutable.Map

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.StreamSlice

//override itterator to return a list of commentNodes converted to Comments or MoreLink

// so moreLinks should be a subclass of both Comment and CommentNode or all should have same base

case class CommentStream(
  val modhash: String,
  val after: String,
  val before: String,
  val comments: List[Commentifiable]
) extends Iterable[Commentifiable] {

  lazy val iterator = comments.iterator

  // def flatten()
  //return itterator to flattened list
}


case class CommentStreamSlice(val post_id : String, val subreddit : String, val reddit : Reddit )
    extends StreamSlice[CommentStream]{

  val params : Map[String,Option[String]] = Map("sort" -> None)

  override def getStream() : CommentStream = {
    CommentService.getStream(Endpoint.post_comment_stream(subreddit,post_id),params.toMap,reddit)
  }

  def top() : CommentStreamSlice = {
    params("sort") = Some("top")
    return this
  }

  def newest() : CommentStreamSlice = {
    params("sort") = Some("new")
    return this
  }

  def oldest() : CommentStreamSlice = {
    params("sort") = Some("old")
    return this
  }

  def controversial() : CommentStreamSlice = {
    params("sort") = Some("controversial")
    return this
  }

  def qa() : CommentStreamSlice = {
    params("sort") = Some("qa")
    return this
  }

}
