package scraw.http

import scalaj.http.Http

object HttpService {

  def get(endpoint : String,token:String) : String = {
    val response  = Http(endpoint)
      .header("Authorization","bearer " + token)
      .asString
    return response.body
  }

}
