package com.steams.scraw.subreddits

import net.liftweb.json._

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.HandleJson
import com.steams.scraw.http.{Endpoint,HttpService}
import com.steams.scraw.posts.{PostStream,Post}


object SubredditService extends HandleJson {

  def getSubreddit(name : String, reddit : Reddit) : Subreddit = {

    val response_body = HttpService.get(Endpoint("about_subreddit",name),reddit.access_token)

    return parse(response_body).\("data").extract[Subreddit]
  }

  def getStream(endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : PostStream = {

    val response_body = HttpService.get(endpoint,reddit.access_token,params)

    val jval = parse(response_body)

    val posts = for(post <- (jval \ "data" \ "children").children) yield {(post \ "data").extract[Post]}

    return PostStream((jval \ "data" \ "modhash"), (jval \ "data" \ "after"), (jval \ "data" \ "before"), posts)
  }
}
