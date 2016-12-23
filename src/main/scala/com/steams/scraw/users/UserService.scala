package com.steams.scraw.users

import net.liftweb.json.{parse}

import com.steams.scraw.utils.HandleJson
import com.steams.scraw.reddit.Reddit
import com.steams.scraw.http.{Endpoint,HttpService}


object UsersService extends HandleJson {

  def getUser(name : String, reddit : Reddit) : User = name match {
    case "me" => {
        val response_body = HttpService.get(Endpoint("me"),reddit.access_token)
        return parse(response_body).extract[User]
      }
    case _ => {

        val response_body = HttpService.get(Endpoint("about_user",name),reddit.access_token)
        return parse(response_body).\("data").extract[User]
      }
  }
}
