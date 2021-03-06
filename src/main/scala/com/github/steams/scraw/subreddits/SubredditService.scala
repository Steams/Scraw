package com.github.steams.scraw.subreddits

import com.github.steams.scraw.reddit.Reddit
import com.github.steams.scraw.utils.JsonHandler
import com.github.steams.scraw.http.{Endpoint,HttpService}
import com.github.steams.scraw.posts.{PostStream,Post}


object SubredditService extends JsonHandler {

  def subscribe(name : String, instance : Reddit) = {
    HttpService.post(Endpoint.subscribe,Seq("action" -> "sub","sr_name" -> name), instance.access_token)
  }

  def unsubscribe(name : String, instance : Reddit) = {
    HttpService.post(Endpoint.subscribe,Seq("action" -> "unsub","sr_name" -> name), instance.access_token)
  }

  def getSubreddit(name : String, reddit : Reddit) : Subreddit = {

    val response_body = HttpService.get(Endpoint.about_subreddit(name),reddit.access_token)

    return parse(response_body).property("data").extract[Subreddit]
  }


  def getStream(endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : PostStream = {

    val response_body = HttpService.get(endpoint,reddit.access_token,params)

    val jval = parse(response_body)
    val posts_raw = jval \ "data" \ "children"

    val posts = posts_raw.children.map(x => (x \ "data").extract[Post])

    return PostStream((jval \ "data" \ "modhash"), (jval \ "data" \ "after"), (jval \ "data" \ "before"), posts)
  }

  def submit(title: String, content: String, url : String, sub_name : String, kind : String, resubmit : Boolean, sendreplies : Boolean, instance: Reddit) = {
    val response_body = HttpService.post(
      Endpoint.submit,
      Seq("api_type" -> "json",
          "extension" -> "json",
          "flair_id" -> "",
          "flair_text" -> "",
          "kind" -> kind,
          "location_name" -> "",
          "resubmit" -> resubmit.toString,
          "sendreplies" -> sendreplies.toString,
          "sr" -> sub_name,
          "text" -> content,
          "title" -> title,
          "url" -> url
      ),
      instance.access_token
    )

  }

  def getSubscribed(instance : Reddit) : List[Subreddit] = {
    val response_body = HttpService.get(Endpoint.subscribed_subreddits,instance.access_token)

    (parse(response_body) \ "data" \ "children").children.map( x => (x \ "data").extract[Subreddit])
  }

}
