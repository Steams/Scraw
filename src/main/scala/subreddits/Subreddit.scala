package scraw.subreddits

import scraw.utils.apiObjects.{BaseObject}
import scraw.{RedditInstance}

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

}

object Subreddit {

  def apply(name : String)(implicit reddit : RedditInstance ) : Subreddit = {
    SubredditService.getSubreddit(name, reddit)
  }

}
