package scraw.subreddits

import net.liftweb.json._

import scraw.{RedditInstance}
import scraw.utils.HandleJson
import scraw.http.{Endpoint,HttpService}
import scraw.posts.PostListing


object SubredditService extends HandleJson {

  def getSubreddit(name : String, reddit : RedditInstance) : Subreddit = {

    val response_body = HttpService.get(Endpoint("about_subreddit",name),reddit.access_token)

    return parse(response_body).\("data").extract[Subreddit]
  }

  def getListing(endpoint : String, params : Map[String,Option[String]], reddit : RedditInstance) : PostListing = {

    val response_body = HttpService.get(endpoint,reddit.access_token,params)

    return parse(response_body).\("data").extract[PostListing]
  }
}
