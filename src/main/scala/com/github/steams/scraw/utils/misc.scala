package com.github.steams.scraw.utils

import scala.language.implicitConversions


case class InstanceConfig(
  username : String,
  password : String,
  client_id : String,
  client_secret : String,
  user_agent : String
)

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

