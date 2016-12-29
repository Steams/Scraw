package com.steams.scraw.subreddits

import com.steams.scraw.utils.apiObjects.{BaseObject}
import com.steams.scraw.reddit.Reddit
import com.steams.scraw.http.Endpoint
import com.steams.scraw.posts.PostStreamSlice

case class Subreddit (
    override val id : String,
    override val name : String,
    val accounts_active : Int,
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

  private def setInstance(reddit : Reddit) = {
    instance = Some(reddit)
  }

  def hot() : PostStreamSlice = {
    return PostStreamSlice(Endpoint("subreddit_listing_hot")(display_name),instance.get)
  }

  def top() : PostStreamSlice = {
    return PostStreamSlice(Endpoint("subreddit_listing_top")(display_name),instance.get)
  }

  def newest() : PostStreamSlice = {
    return PostStreamSlice(Endpoint("subreddit_listing_new")(display_name),instance.get)
  }

  def rising() : PostStreamSlice = {
    return PostStreamSlice(Endpoint("subreddit_listing_rising")(display_name),instance.get)
  }

  def random() : PostStreamSlice = {
    return PostStreamSlice(Endpoint("subreddit_listing_random")(display_name),instance.get)
  }

  def controvertial() : PostStreamSlice = {
    return PostStreamSlice(Endpoint("subreddit_listing_controvertial")(display_name),instance.get)
  }

  // def comments()

}

object Subreddit {

  def apply(name : String)(implicit reddit : Reddit ) : Subreddit = {
    val sub = SubredditService.getSubreddit(name, reddit)
    sub.setInstance(reddit)
    return sub
  }

}
