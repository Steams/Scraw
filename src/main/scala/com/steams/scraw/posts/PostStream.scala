package com.steams.scraw.posts

import scala.collection.mutable.Map

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.subreddits.SubredditService
import com.steams.scraw.utils.StreamSlice

case class PostStream(
                       val modhash: String,
                       val after: String,
                       val before: String,
                       val posts: List[Post]
                     ) extends Iterable[Post] {

  // lazy val posts = children

  override def iterator = posts.iterator
}

case class PostStreamSlice(val endpoint : String, val reddit : Reddit ) extends StreamSlice[PostStream]{

  val params : Map[String,Option[String]] = Map(
    "before" -> None,
    "after" -> None,
    "limit" -> None,
    "count" -> None,
    "show" -> None,
    "t" -> None
  )

  override def getStream() : PostStream = {
    SubredditService.getStream(endpoint,params.toMap,reddit)
  }

  def before( name : String) : PostStreamSlice = {
    params("before") = Some(name)
    return this
  }

  def after( name : String) : PostStreamSlice = {
    params("after") = Some(name)
    return this
  }

  def limit( num : Int) : PostStreamSlice = {
    params("limit") = Some(num.toString)
    return this
  }

  def count( num : Int) : PostStreamSlice = {
    params("count") = Some(num.toString)
    return this
  }

  def show( opt : String) : PostStreamSlice = {
    params("show") = Some(opt)
    return this
  }

  def time( period : String) : PostStreamSlice = {
    params("t") = Some(period)
    return this
  }
}
