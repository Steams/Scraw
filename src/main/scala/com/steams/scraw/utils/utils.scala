package com.steams.scraw

import net.liftweb.json._
import net.liftweb.json.JsonAST.{JValue}

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
    implicit def JvalToString( x:JValue) : String = x.extractOrElse[String]("")
  }

}
