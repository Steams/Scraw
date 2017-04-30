package com.github.steams.scraw.users

import com.github.steams.scraw.reddit.Reddit
import com.github.steams.scraw.utils.JsonHandler
import com.github.steams.scraw.http.{Endpoint,HttpService}
import com.github.steams.scraw.posts.Post

object UsersService extends JsonHandler {

  def getUser(name : String, reddit : Reddit) : User = name match {
    case "me" => {
      val response_body = HttpService.get(Endpoint.me,reddit.access_token)
      return parse(response_body).extract[User]
    }

    case _ => {
      val response_body = HttpService.get(Endpoint.about_user(name),reddit.access_token)
      println(parse(response_body))
      return parse(response_body).property("data").extract[User]
    }
  }

  def block(fullname : String, instance : Reddit) = HttpService.post(Endpoint.block, Seq("id" -> fullname), instance.access_token)
}
