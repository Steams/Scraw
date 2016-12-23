package scraw.http

import scala.util.Try

import net.liftweb.json._
import scalaj.http.{Http,Base64}

import scraw.utils.HandleJson


object OauthService extends HandleJson {

  def authenticate(
    client_id : String,
    client_secret : String,
    user_agent : String,
    username : String,
    password : String
  ) : Try[String] = {

    val response  = Http(Endpoint("access_token"))
      .postForm(Seq(
                  "grant_type" -> "password",
                  "username" -> username,
                  "password" -> password))
      .header("Authorization","Basic " + genSignature(client_id,client_secret))
      .header("User-Agent",user_agent)
      .asString

    val jval = parse(response.body).\("access_token").extractOpt[String]

    return Try(jval.get)
  }

  def genSignature(key : String, secret : String) : String = { Base64.encodeString(key+":"+secret) }
}
