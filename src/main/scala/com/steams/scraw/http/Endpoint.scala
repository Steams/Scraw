package com.steams.scraw.http

object Endpoint {

  val endpoints : Map[String, (String) => String] = Map(
    "access_token" -> ((_:String) => "https://www.reddit.com/api/v1/access_token"),
    "me" -> ((_:String) => "https://oauth.reddit.com/api/v1/me"),
    "about_subreddit" -> ((name : String) => "https://oauth.reddit.com/r/"+name+"/about"),
    "subreddit_listing" -> ((name : String) => "https://oauth.reddit.com/r/"+name),
    "subreddit_listing_hot" -> ((name : String) => "https://oauth.reddit.com/r/"+name+"/hot"),
    "subreddit_listing_top" -> ((name : String) => "https://oauth.reddit.com/r/"+name+"/top"),
    "subreddit_listing_new" -> ((name : String) => "https://oauth.reddit.com/r/"+name+"/new"),
    "subreddit_listing_random" -> ((name : String) => "https://oauth.reddit.com/r/"+name+"/random"),
    "subreddit_listing_rising" -> ((name : String) => "https://oauth.reddit.com/r/"+name+"/rising"),
    "subreddit_listing_controversial" -> ((name : String) => "https://oauth.reddit.com/r/"+name+"/controvertial"),
    "about_user" -> ((name : String) => "https://oauth.reddit.com/user/"+name+"/about")
  )

  def apply(title : String, arg: String = "") : String = {
    endpoints.get(title) match {
      case Some(func) => func(arg)
      case None => ""
    }
  }
}
