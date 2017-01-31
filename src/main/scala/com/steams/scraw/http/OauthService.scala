package com.steams.scraw.http

import scala.util.Try

import scalaj.http.{Base64, Http}

import com.steams.scraw.utils.JsonHandler


object OauthService extends JsonHandler {

  def authenticate(
    client_id : String,
    client_secret : String,
    user_agent : String,
    username : String,
    password : String
  ) : Try[String] = {

    val response  = Http(Endpoint.access_token)
      .postForm(Seq(
                  "grant_type" -> "password",
                  "username" -> username,
                  "password" -> password
                ))
      .header("Authorization","Basic " + genSignature(client_id,client_secret))
      .header("User-Agent",user_agent)
      .asString

    println(response.body)
    val jval = parse(response.body) \ "access_token"

    return Try(jval)

  }

  def genSignature(key : String, secret : String) : String = { Base64.encodeString(key + ":" + secret) }
}
