package com.github.steams.scraw.messages

import scala.collection.mutable.Map
import scala.util.matching.Regex
import scala.collection.mutable.MutableList

import com.github.steams.scraw.http.Endpoint
import com.github.steams.scraw.reddit.Reddit
import com.github.steams.scraw.utils.StreamSlice

case class MessageStream(
  val modhash: String,
  val after: String,
  val before: String,
  val messages: List[Message]
) extends Iterable[Message] {

  lazy val iterator = messages.iterator

  // def matching(regex: Regex) : Iterable[Message] = {
  //   return iterator.withFilter( x => regex.pattern.matcher(x.body).matches())
  // }

  // def containing(string: String) : Iterable[Message] = {
  //   var comments : MutableList[Message] = MutableList()

  //   for( c <- iterator ) {
  //     comments ++= getMessages(c)
  //   }

  //   def getMessages(x : Messageifiable) : TraversableOnce[Message] = {
  //       x match {
  //         case comment : Message => {
  //           comments += comment

  //           comment.replies match {
  //             case Some(replies : List[Messageifiable]) => replies.flatMap( r => getMessages(r))
  //             case None => MutableList()
  //           }
  //         }

  //         case link : MessagesLink => {
  //           link.get.flatMap( c => getMessages(c))
  //         }
  //       }
  //   }

  //   return comments.filter( x => x.body contains string).toIterable
  // }
  // def flatten()
  // TODO : return itterator to flattened list
}


case class MessageStreamSlice(val reddit : Reddit ) extends StreamSlice[MessageStream] {

  val params : Map[String,Option[String]] = Map("endpoint" -> Some(Endpoint.inbox))

  override def getStream() : MessageStream = {
    MessageService.getStream(params("endpoint").get,params.toMap,reddit)
  }

  def unread() : MessageStreamSlice = {
    params("endpoint") = Some(Endpoint.messages_unread)
    return this
  }

  def messages_only() : MessageStreamSlice = {
    params("endpoint") = Some(Endpoint.messages_only)
    return this
  }

  def mentions() : MessageStreamSlice = {
    params("endpoint") = Some(Endpoint.messages_mentions)
    return this
  }

  def post_replies() : MessageStreamSlice = {
    params("endpoint") = Some(Endpoint.messages_post_replies)
    return this
  }

  def comment_replies() : MessageStreamSlice = {
    params("endpoint") = Some(Endpoint.messages_comment_replies)
    return this
  }

  def sent() : MessageStreamSlice = {
    params("endpoint") = Some(Endpoint.messages_sent)
    return this
  }

}
