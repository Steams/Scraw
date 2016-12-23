package com.steams.scraw.subreddits

import net.liftweb.json._

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.HandleJson
import com.steams.scraw.http.{Endpoint,HttpService}
import com.steams.scraw.posts.PostStream


object SubredditService extends HandleJson {

  def getSubreddit(name : String, reddit : Reddit) : Subreddit = {

    val response_body = HttpService.get(Endpoint("about_subreddit",name),reddit.access_token)

    return parse(response_body).\("data").extract[Subreddit]
  }

  def getListing(endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : PostStream = {

    val response_body = HttpService.get(endpoint,reddit.access_token,params)

    return parse(response_body).\("data").extract[PostStream]
  }
}
