package com.steams.scraw

import net.liftweb.json.{DefaultFormats}
import net.liftweb.json.JsonAST.{JValue}
import scala.language.implicitConversions

package utils {

  case class InstanceConfig(
    username : String,
    password : String,
    client_id : String,
    client_secret : String,
    user_agent : String
  )

  trait JsonHandler {
    implicit val formats = DefaultFormats
    implicit def JvalToString( x:JValue) : String = x.extractOrElse[String]("")

    implicit val filler = "" //this stupid and dangerous. Please refactor
  }

  object TimePeriod extends Enumeration {
    type TimePeriod = Value

    val All = Value("all")
    val Year = Value("Year")
    val Month = Value("month")
    val Week = Value("week")
    val Day = Value("day")
    val Hour = Value("hour")
  }

  trait StreamSlice[T <: Iterable[Any]] {
    def getStream() : T
  }

}
