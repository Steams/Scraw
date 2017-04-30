package com.github.steams.scraw.utils

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

trait JsonHandler {
  type JsonValue = com.github.steams.scraw.utils.JsonParsing.JsonValue
  val Jsonator = com.github.steams.scraw.utils.JsonParsing.Jsonator
  def parse(json:String) : JsonValue = Jsonator.parse(json)

  implicit def JsonValueToString( json : JsonValue ) : String = json.toString
  implicit def JsonValueToBool( json : JsonValue ) : Boolean = json.toBoolean
  implicit def JsonValueToInt( json : JsonValue ) : Int = json.toInt
  implicit def JsonValueToOptionalString( json : JsonValue ) : Option[String] = json.toOptionalString
}

