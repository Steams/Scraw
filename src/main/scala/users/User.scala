package scraw.users

import scraw.utils.apiObjects.{BaseObject,Created}
import scraw.RedditInstance

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
) extends BaseObject(id,name) with Created { }

object User {

  def apply(name : String)(implicit reddit : RedditInstance ) : User = {
    UsersService.getUser(name,reddit)
  }

}
