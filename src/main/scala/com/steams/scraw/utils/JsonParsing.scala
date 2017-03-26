package com.steams.scraw.utils

import net.liftweb.json._

package JsonParsing {

  abstract class JsonParser {
    def parse( json : String) : JsonValue
  }

  abstract class JsonValue {
    def property( name : String ) : JsonValue
    def children : List[JsonValue]
    def toString : String
    def toOptionalString : Option[String]
    def toInt : Int
    def toBoolean : Boolean
    def \(name : String): JsonValue = property(name)
    def extract[T : Manifest] : T
  }

  object Jsonator extends JsonParser {
    def parse( json : String) : JsonValue = LiftJsonValue(net.liftweb.json.parse(json))
  }

  case class LiftJsonValue(val json: JValue) extends JsonValue {
    implicit val formats = DefaultFormats

    def property( name : String ) : JsonValue = LiftJsonValue(json \ name)
    def children : List[JsonValue] = json.children.map { x : JValue => LiftJsonValue(x) }
    override def toString : String = json.extractOrElse[String]("")
    def toInt : Int = json.extractOrElse[Int](0)
    def toBoolean : Boolean = json.extractOrElse[Boolean](false)
    def toOptionalString : Option[String] = json.extract[Option[String]]
    def extract[T : Manifest] : T = json.extract[T]
  }


}


