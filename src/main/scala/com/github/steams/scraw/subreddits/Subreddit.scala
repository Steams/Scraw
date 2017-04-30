package com.github.steams.scraw.subreddits

import com.github.steams.scraw.utils.apiObjects.{BaseObject}
import com.github.steams.scraw.reddit.Reddit
import com.github.steams.scraw.http.Endpoint
import com.github.steams.scraw.http.Sorting
import com.github.steams.scraw.posts.PostStreamSlice

case class Subreddit (
    override val id : String,
    override val name : String,
    val accounts_active : Option[Int],
    val comment_score_hide_mins : Int,
    val description : String,
    val description_html : String,
    val display_name : String,
    val header_img : String,
    val header_size : Array[Int],
    val header_title : String,
    val over18 : Boolean,
    val public_description : String,
    val public_traffic : Boolean,
    val subscribers : Long,
    val submission_type : String,
    val submit_link_label : String,
    val submit_text_label : String,
    val subreddit_type : String,
    val title : String,
    val url : String,
    val user_is_banned : Boolean,
    val user_is_contributor : Boolean,
    val user_is_moderator : Boolean,
    val user_is_subscriber : Boolean
  ) extends BaseObject(id,name) {

  var instance : Option[Reddit] = None

  def subscribe()(implicit instance : Reddit) = SubredditService.subscribe(display_name, instance)

  def unsubscribe()(implicit instance : Reddit) = SubredditService.unsubscribe(display_name, instance)

  def submitLink(title: String, url : String, resubmit: Boolean, sendNotifications: Boolean)(implicit instance: Reddit) = SubredditService.submit(
    title,
    "",
    url,
    display_name,
    "link",
    resubmit,
    sendNotifications,
    instance
  )

  def submitImage(title: String, url : String, resubmit: Boolean, sendNotifications: Boolean)(implicit instance: Reddit) = SubredditService.submit(
    title,
    "",
    url,
    display_name,
    "image",
    resubmit,
    sendNotifications,
    instance
  )

  def selfPost(title: String, content: String, sendNotifications : Boolean)(implicit instance : Reddit) = SubredditService.submit(
    title,
    content,
    "http://reddit.com",
    display_name,
    "self",
    true,
    sendNotifications,
    instance
  )

  private def setInstance(reddit : Reddit) = {
    instance = Some(reddit)
  }

  // def comments()

  def hot() : PostStreamSlice = {
    return PostStreamSlice(Endpoint.subreddit_listing(display_name, Sorting.Hot),instance.get)
  }

  def top() : PostStreamSlice = {
    return PostStreamSlice(Endpoint.subreddit_listing(display_name, Sorting.Top),instance.get)
  }

  def newest() : PostStreamSlice = {
    return PostStreamSlice(Endpoint.subreddit_listing(display_name, Sorting.Newest),instance.get)
  }

  def rising() : PostStreamSlice = {
    return PostStreamSlice(Endpoint.subreddit_listing(display_name, Sorting.Rising),instance.get)
  }

  def random() : PostStreamSlice = {
    return PostStreamSlice(Endpoint.subreddit_listing(display_name, Sorting.Random),instance.get)
  }

  def controvertial() : PostStreamSlice = {
    return PostStreamSlice(Endpoint.subreddit_listing(display_name, Sorting.Controversial),instance.get)
  }
}

object Subreddit {

  def apply(name : String)(implicit reddit : Reddit ) : Subreddit = {
    val sub = SubredditService.getSubreddit(name, reddit)
    sub.setInstance(reddit)
    return sub
  }

}
