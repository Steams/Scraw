package com.steams.scraw.http



object Endpoint {

  //why not just define each of these as their own functions
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
    "about_user" -> ((name : String) => "https://oauth.reddit.com/user/"+name+"/about"),
    "about_post" -> ((name : String) => "https://oauth.reddit.com/by_id/t3_"+name),
    "post_comments" -> ((name : String) => "https://oauth.reddit.com/r/_"+name),
    "more_comments" -> ((_: String) => "https://oauth.reddit.com/api/morechildren")
  )


  def apply(title : String)(implicit arg: String = "") : String = {
    endpoints.get(title) match {
      case Some(func) => func(arg)
      case None => ""
    }
  }
}
