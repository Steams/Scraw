package scraw.http

object Endpoint {

  val endpoints : Map[String, (String) => String] = Map(
    "access_token" -> ((_:String) => "https://www.reddit.com/api/v1/access_token"),
    "me" -> ((_:String) => "https://oauth.reddit.com/api/v1/me"),
    "about_subreddit" -> ((name : String) => "https://oauth.reddit.com/r/"+name+"/about"),
    "subreddit_listing" -> ((name : String) => "https://oauth.reddit.com/r/"+name),
    "about_user" -> ((name : String) => "https://oauth.reddit.com/user/"+name+"/about")
  )

  def apply(title : String, arg: String = "") : String = {
    endpoints.get(title) match {
      case Some(func) => func(arg)
      case None => ""
    }
  }
}
