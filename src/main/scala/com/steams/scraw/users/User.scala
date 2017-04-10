package com.steams.scraw.users

import com.steams.scraw.comments.{ CommentService, CommentStream }
import com.steams.scraw.http.Endpoint
import com.steams.scraw.utils.apiObjects.{BaseObject,Created}
import com.steams.scraw.reddit.Reddit
import com.steams.scraw.messages.{MessageStreamSlice, MessageService}
import com.steams.scraw.posts.PostStreamSlice

case class User (
    override val id : String,
    override val name : String,
    val comment_karma : Int,
    val has_mail : Option[Boolean],
    val has_mod_mail : Option[Boolean],
    val has_verified_email : Option[Boolean],
    val inbox_count : Option[Int],
    val is_friend : Option[Boolean],
    val is_gold : Boolean,
    val is_mod : Boolean,
    val link_karma : Int,
    val modhash : Option[String],
    val created : Long,
    val created_utc : Long
) extends BaseObject(id,name) with Created {

  def inbox()(implicit instance: Reddit) : MessageStreamSlice = MessageStreamSlice(instance)

  def saved_posts()(implicit instance: Reddit) : PostStreamSlice = PostStreamSlice(Endpoint.saved_posts(name),instance)

  def saved_comments()(implicit instance: Reddit) : CommentStream = CommentService.getStreamFromSaved(Endpoint.saved_comments(name),Map(),instance)

  def send_pm( subject: String, message : String )(implicit instance: Reddit) = MessageService.send_pm(subject,message,name,instance)

}

object User {

  def apply(name : String)(implicit reddit : Reddit ) : User = {
    UsersService.getUser(name,reddit)
  }

}
