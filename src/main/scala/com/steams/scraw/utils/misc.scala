package com.steams.scraw.utils

import scala.language.implicitConversions


case class InstanceConfig(
  username : String,
  password : String,
  client_id : String,
  client_secret : String,
  user_agent : String
)

trait JsonHandler {
  type JsonValue = com.steams.scraw.utils.JsonParsing.JsonValue
  val Jsonator = com.steams.scraw.utils.JsonParsing.Jsonator
  def parse(json:String) : JsonValue = Jsonator.parse(json)

  implicit def JsonValueToString( json : JsonValue ) : String = json.toString
  implicit def JsonValueToBool( json : JsonValue ) : Boolean = json.toBoolean
  implicit def JsonValueToInt( json : JsonValue ) : Int = json.toInt
  implicit def JsonValueToOptionalString( json : JsonValue ) : Option[String] = json.toOptionalString
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

