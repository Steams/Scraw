package  com.steams.scraw.posts

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.apiObjects.{ BaseObject, Votable, Repliable}
import com.steams.scraw.comments.CommentStreamSlice


case class Post(
  override val id : String,
  override val name : String,
  val author : String,
  val author_flair_css_class : String,
  val author_flair_text : String,
  val clicked : Boolean,
  val domain : String,
  val hidden : Boolean,
  val is_self : Boolean,
  val link_flair_css_class : String,
  val link_flair_text : String,
  val locked : Boolean,
  val num_comments : Int,
  val over_18 : Boolean,
  val permalink : String,
  val saved : Boolean,
  val score : Int,
  val selftext : String,
  val subreddit : String,
  val subreddit_id : String,
  val thumbnail : String,
  val title : String,
  val url : String,
  val distinguished : String,
  val stickied : Boolean
    //problematic values below
    // val edited : String,
    // val selftext_html : String,
    // val media : Object,
    // val media_embed : Object,
    // val likes : String, //this is NOT the score, best leave this out
) extends BaseObject(id,name) with Votable with Repliable {

  var instance : Option[Reddit] = None

  private def setInstance(reddit : Reddit) = {
    instance = Some(reddit)
  }

  //comments should be able to be flattened
  def comments() : CommentStreamSlice  = {
    return CommentStreamSlice(id,subreddit,instance.get)
  }
}

object Post {

  def apply(id : String)(implicit reddit : Reddit ) : Post = {
    val post = PostService.getPost(id,reddit)
    post.setInstance(reddit)
    return post
  }

}
