package com.steams.scraw.http

import scalaj.http.Http

//should probably define restRequest and Response types
//make and HttpProvider interface so u cna stub it for tests

object HttpService {

  def get(endpoint : String,token:String) : String = {
    val response  = Http(endpoint)
      .header("Authorization","bearer " + token)
      .asString
    return response.body
  }

  def get(endpoint : String,token:String, params:Map[String,Option[String]]) : String = {
    val valid = params.collect{ case (k,Some(v)) => (k,v) }

    val response  = Http(endpoint)
      .params(valid)
      .header("Authorization","bearer " + token)
      .asString

    // println(response)
    return response.body
  }

}
