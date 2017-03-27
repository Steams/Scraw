package com.steams.scraw.utils
import com.steams.scraw.http.HttpService
import com.steams.scraw.http.Endpoint
import com.steams.scraw.reddit.Reddit

package apiObjects {

  class BaseObject(val id : String, val name : String)

  class Listing(val data : List[BaseObject])

  trait Votable {
    // def ups : Int
    // def downs : Int
    // def likes : Boolean
    def name : String

    def upvote()( implicit instance: Reddit) = HttpService.post(Endpoint.vote, Seq("dir" -> "1", "id" -> name, "rank" -> "1"), instance.access_token)

    def downvote()( implicit instance: Reddit) = HttpService.post(Endpoint.vote, Seq("dir" -> "-1", "id" -> name, "rank" -> "1"), instance.access_token)

    def clearvote()( implicit instance: Reddit) = HttpService.post(Endpoint.vote, Seq("dir" -> "0", "id" -> name, "rank" -> "1"), instance.access_token)

    def save()( implicit instance: Reddit) = HttpService.post(Endpoint.save, Seq("category" -> "", "id" -> name), instance.access_token)

    def unsave()( implicit instance: Reddit) = HttpService.post(Endpoint.unsave, Seq("id" -> name), instance.access_token)
  }

  trait Repliable {
    def name : String

    def reply(content: String)( implicit instance: Reddit) = HttpService.post(Endpoint.reply, Seq("api_type" -> "json", "text" -> content, "thing_id" -> name), instance.access_token)

  }


  trait Created {
    def created : Long
    def created_utc : Long
  }

}
