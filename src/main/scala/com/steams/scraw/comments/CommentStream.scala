package com.steams.scraw.comments

import scala.collection.mutable.Map

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.StreamSlice


case class CommentStream(
  val modhash: String,
  val after: String,
  val before: String,
  val comments: List[Comment]
) extends Iterable[Comment] {

  // lazy val posts = children

  def iterator = comments.iterator
}


case class CommentStreamSlice(val post_id : String, val subreddit : String, val reddit : Reddit )
    extends StreamSlice[CommentStream]{

  val params : Map[String,Option[String]] = Map("sort" -> None)

  override def getStream() : CommentStream = {
    CommentService.getStream(" https://oauth.reddit.com/r/"+subreddit+"/comments/"+post_id,params.toMap,reddit)
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
