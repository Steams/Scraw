package com.steams.scraw.messages

import com.steams.scraw.utils.apiObjects.{ BaseObject, Repliable, Creatable}
// import com.steams.scraw.reddit.Reddit

case class Message (
  override val id : String,
  override val name : String,
  val author : String,
  val body : String,
  val body_html : String,
  val context : String,
  val link_title : Option[String],
  // val new : Boolean,
  val parent_id : Option[String],
  val replies : String,
  val subject : Option[String],
  val subreddit : String
) extends BaseObject(id,name) with Repliable with Creatable {

}
