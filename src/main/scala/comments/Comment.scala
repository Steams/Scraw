package scraw.comments

import scraw.utils.apiObjects.{BaseObject}

case class Comment (
  override val id : String,
  override val name : String
  ) extends BaseObject(id,name) {

}
