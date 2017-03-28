package com.steams.scraw.http

object Sorting extends Enumeration {
  val Top,Hot,Newest,Random,Rising,Controversial = Value
}

object Endpoint {

  val access_token = "https://www.reddit.com/api/v1/access_token"
  val me = "https://www.reddit.com/api/v1/me"
  val more_children = "https://oauth.reddit.com/api/morechildren"
  val vote = "https://oauth.reddit.com/api/vote"
  val save = "https://oauth.reddit.com/api/save"
  val unsave = "https://oauth.reddit.com/api/unsave"
  val reply = "https://oauth.reddit.com/api/comment"
  val delete = "https://oauth.reddit.com/api/del"
  val report = "https://oauth.reddit.com/api/report"
  val edit = "https://oauth.reddit.com/api/editusertext"
  val submit = "https://oauth.reddit.com/api/submit"

  def about_subreddit( name : String ) = { "https://oauth.reddit.com/r/"+name+"/about" }

  def subreddit_listing( name : String, sort : Sorting.Value = Sorting.Hot) : String = {
    sort match {
      case Sorting.Top => "https://oauth.reddit.com/r/"+name+"/top"
      case Sorting.Hot => "https://oauth.reddit.com/r/"+name+"/hot"
      case Sorting.Newest => "https://oauth.reddit.com/r/"+name+"/new"
      case Sorting.Random => "https://oauth.reddit.com/r/"+name+"/random"
      case Sorting.Rising => "https://oauth.reddit.com/r/"+name+"/rising"
      case Sorting.Controversial => "https://oauth.reddit.com/r/"+name+"/controversial"
      case _ => ""
    }
  }

  def about_user(name : String) = { "https://oauth.reddit.com/user/"+name+"/about" }

  def about_post(name: String) = { "https://oauth.reddit.com/by_id/t3_"+name }

  def post_comment_stream(subreddit : String, post_id : String ) = {
    "https://oauth.reddit.com/r/"+subreddit+"/comments/"+post_id
  }

}
