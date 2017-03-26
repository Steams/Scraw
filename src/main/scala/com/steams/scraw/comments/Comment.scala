package com.steams.scraw.comments

import com.steams.scraw.utils.apiObjects.BaseObject
import com.steams.scraw.reddit.Reddit


// case class CommentNode( id : String )


case class LinkId(private val raw_id : String) {

  def id : String = toString

  override def toString = "t3_" + raw_id

}

trait Commentifiable {
  def name : String
  def parent_id : String
}

case class CommentsLink (
  name : String,
  parent_id: String,
  count : Int,
  children : List[String],
  link_id : LinkId,
  reddit : Reddit,
  depth : Int
) extends Commentifiable {
  //extend itterable

  def get : CommentStream = CommentService.getMoreComments(link_id,children,reddit)
  def getFlattened : CommentStream = CommentService.getMoreCommentsFlat(link_id,children,reddit)

}

case class Comment (
  override val id : String,
  override val name : String,
  val approved_by : String,
  val author : String,
  val author_flair_css_class : String,
  val author_flair_text : String,
  val banned_by : String,
  val body : String,
  val body_html : String,
  val edited : Boolean,
  val gilded : Int,
  val likes : Boolean,
  val link_author : Option[String],
  val link_id : String,
  val link_title : Option[String],
  val link_url : Option[String],
  val num_reports : Int,
  val parent_id : String,
  val replies : Option[List[Commentifiable]],
  val saved : Boolean,
  val score : Int,
  val score_hidden : Boolean,
  val subreddit : String,
  val subreddit_id : String,
  val distinguished : String,
  val depth : Int
) extends BaseObject(id,name) with Commentifiable{}


object Comment {
  def builder() : CommentBuilder = new CommentBuilder()
}

class CommentBuilder(){

  var id : String = _
  def id(parm : String) : CommentBuilder = {
    this.id = parm
    return this
  }

  var name : String = _
  def name(parm : String) : CommentBuilder = {
    this.name = parm
    return this
  }

  var approved_by : String = _
  def approved_by(parm : String) : CommentBuilder = {
    this.approved_by = parm
    return this
  }

  var author : String = _
  def author(parm : String) : CommentBuilder = {
    this.author = parm
    return this
  } 
  var author_flair_css_class : String = _
  def author_flair_css_class(parm : String) : CommentBuilder = {
    this.author_flair_css_class = parm
    return this
  }
  var author_flair_text : String = _
  def author_flair_text(parm : String) : CommentBuilder = {
    this.author_flair_text = parm
    return this
  }
  var banned_by : String = _
  def banned_by(parm : String) : CommentBuilder = {
    this.banned_by = parm
    return this
  }
  var body : String = _
  def body(parm : String) : CommentBuilder = {
    this.body = parm
    return this
  }
  var body_html : String = _
  def body_html(parm : String) : CommentBuilder = {
    this.body_html = parm
    return this
  }
  var edited : Boolean = _
  def edited(parm : Boolean) : CommentBuilder = {
    this.edited = parm
    return this
  }
  var gilded : Int = _
  def gilded(parm : Int) : CommentBuilder = {
    this.gilded = parm
    return this
  }
  var likes : Boolean = _
  def likes(parm : Boolean) : CommentBuilder = {
    this.likes = parm
    return this
  }
  var link_author : Option[String] = _
  def link_author(parm : Option[String]) : CommentBuilder = {
    this.link_author = parm
    return this
  }
  var link_id : String = _
  def link_id(parm : String) : CommentBuilder = {
    this.link_id = parm
    return this
  }
  var link_title : Option[String] = _
  def link_title(parm : Option[String]) : CommentBuilder = {
    this.link_title = parm
    return this
  }
  var link_url : Option[String] = _
  def link_url(parm : Option[String]) : CommentBuilder = {
    this.link_url = parm
    return this
  }
  var num_reports : Int = _
  def num_reports(parm : Int) : CommentBuilder = {
    this.num_reports = parm
    return this
  }
  var parent_id : String = _
  def parent_id(parm : String) : CommentBuilder = {
    this.parent_id = parm
    return this
  }
  var saved : Boolean = _
  def saved(parm : Boolean) : CommentBuilder = {
    this.saved = parm
    return this
  }
  var score : Int = _
  def score(parm : Int) : CommentBuilder = {
    this.score = parm
    return this
  }
  var score_hidden : Boolean = _
  def score_hidden(parm : Boolean) : CommentBuilder = {
    this.score_hidden = parm
    return this
  }
  var subreddit : String = _
  def subreddit(parm : String) : CommentBuilder = {
    this.subreddit = parm
    return this
  }
  var subreddit_id : String = _
  def subreddit_id(parm : String) : CommentBuilder = {
    this.subreddit_id = parm
    return this
  }
  var distinguished : String = _
  def distinguished(parm : String) : CommentBuilder = {
    this.distinguished = parm
    return this
  }
  var replies : Option[List[Commentifiable]] = _
  def replies(parm : Option[List[Commentifiable]]) : CommentBuilder = {
    this.replies = parm
    return this
  }

  var depth : Int = _
  def depth(parm : Int) : CommentBuilder = {
    this.depth = parm
    return this
  }

  def build() : Comment = {
    Comment(id, name, approved_by, author, author_flair_css_class, author_flair_text, banned_by, body, body_html, edited, gilded, likes, link_author, link_id, link_title, link_url, num_reports, parent_id, replies, saved, score, score_hidden, subreddit, subreddit_id, distinguished, depth)
  }
}

