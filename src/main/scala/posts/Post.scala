package  scraw.posts

import scala.collection.mutable.Map

import scraw.utils.apiObjects.{BaseObject,Listing}
import scraw.subreddits.SubredditService
import scraw.RedditInstance

case class Post(val kind:String, val data:PostData)

case class PostData(
  override val id : String,
  override val name : String,
  // val author : String,
  // val author_flair_css_class : String,
  // val author_flair_text : String,
  // val clicked : Boolean,
  // val domain : String,
  // val hidden : Boolean,
  // val is_self : Boolean,
  // val likes : Boolean,
  // val link_flair_css_class : String,
  // val link_flair_text : String,
  // val locked : Boolean,
  // val media : Object,
  // val media_embed : Object,
  val num_comments : Int,
  // val over_18 : Boolean,
  val permalink : String,
  // val saved : Boolean,
  val score : Int,
  val selftext : String,
  // val selftext_html : String,
  val subreddit : String,
  // val subreddit_id : String,
  // val thumbnail : String,
  val title : String,
  val url : String
  // val edited : String,
  // val distinguished : String,
  // val stickied : Boolean
) extends BaseObject(id,name) {

}

case class PostListing(
  val modhash: String,
  val children: List[Post],
  val after: String,
  val before: String
) extends Iterable[Post] {

  def iterator = children.iterator
}

case class PostListingBuilder(val endpoint : String, val reddit : RedditInstance ) {

  val params : Map[String,Option[String]] = Map(
    "before" -> None,
    "after" -> None,
    "limit" -> None,
    "count" -> None,
    "show" -> None
  )

  def get() : PostListing = {
    SubredditService.getListing(endpoint,params.toMap,reddit)
  }

  def before( name : String) : PostListingBuilder = {
    params("before") = Some(name)
    return this
  }

  def after( name : String) : PostListingBuilder = {
    params("after") = Some(name)
    return this
  }

  def limit( num : Int) : PostListingBuilder = {
    params("limit") = Some(num.toString)
    return this
  }

  def count( num : Int) : PostListingBuilder = {
    params("count") = Some(num.toString)
    return this
  }

  def show( opt : String) : PostListingBuilder = {
    params("show") = Some(opt)
    return this
  }
}
