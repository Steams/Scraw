package scraw

import net.liftweb.json.DefaultFormats

package utils {

  case class InstanceConfig(
    username : String,
    password : String,
    client_id : String,
    client_secret : String,
    user_agent : String
  )

  trait HandleJson {
    implicit val formats = DefaultFormats
  }

}
