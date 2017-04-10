package com.steams.scraw.comments

import scala.collection.mutable.Map
import scala.util.matching.Regex
import scala.collection.mutable.MutableList

import com.steams.scraw.http.Endpoint
import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.StreamSlice

case class CommentStream(
  val modhash: String,
  val after: String,
  val before: String,
  val comments: List[Commentifiable]
) extends Iterable[Commentifiable] {

  lazy val iterator = comments.iterator

  def matching(regex: Regex) : Iterable[Comment] = {
    var comments : MutableList[Comment] = MutableList()

    for( c <- iterator ) {
      comments ++= getComments(c)
    }

    def getComments(x : Commentifiable) : TraversableOnce[Comment] = {
      x match {
        case comment : Comment => {
          comments += comment

          comment.replies match {
            case Some(replies : List[Commentifiable]) => replies.flatMap( r => getComments(r))
            case None => MutableList()
          }
        }

        case link : CommentsLink => {
          link.get.flatMap( c => getComments(c))
        }
      }
    }

    return comments.filter( x => regex.pattern.matcher(x.body).matches()).toIterable
  }

  def containing(string: String) : Iterable[Comment] = {
    var comments : MutableList[Comment] = MutableList()

    for( c <- iterator ) {
      comments ++= getComments(c)
    }

    def getComments(x : Commentifiable) : TraversableOnce[Comment] = {
        x match {
          case comment : Comment => {
            comments += comment

            comment.replies match {
              case Some(replies : List[Commentifiable]) => replies.flatMap( r => getComments(r))
              case None => MutableList()
            }
          }

          case link : CommentsLink => {
            link.get.flatMap( c => getComments(c))
          }
        }
    }

    return comments.filter( x => x.body contains string).toIterable
  }
  // def flatten()
  // TODO : return itterator to flattened list
}


case class CommentStreamSlice(endpoint : String, val reddit : Reddit )
    extends StreamSlice[CommentStream]{

  val params : Map[String,Option[String]] = Map("sort" -> None)

  override def getStream() : CommentStream = {
    CommentService.getStream(endpoint,params.toMap,reddit)
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
