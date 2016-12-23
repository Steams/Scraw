package scraw.subreddits

import net.liftweb.json._

import scraw.{RedditInstance}
import scraw.utils.HandleJson
import scraw.http.{Endpoint,HttpService}


object SubredditService extends HandleJson {

  def getSubreddit(name : String, reddit : RedditInstance) : Subreddit = {

    val response_body = HttpService.get(Endpoint("about_subreddit",name),reddit.access_token)

    return parse(response_body).\("data").extract[Subreddit]
  }
}
