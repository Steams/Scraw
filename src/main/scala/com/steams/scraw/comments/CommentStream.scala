package com.steams.scraw.comments

case class CommentStream(
  val modhash: String,
  val after: String,
  val before: String,
  val comments: List[Comment]
) extends Iterable[Comment] {

  // lazy val posts = children

  def iterator = comments.iterator
}
