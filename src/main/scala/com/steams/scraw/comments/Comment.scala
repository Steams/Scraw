package com.steams.scraw.comments

import com.steams.scraw.utils.apiObjects.BaseObject

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
  val edited : String,
  val gilded : Int,
  val likes : Boolean,
  val link_author : String,
  val link_id : String,
  val link_title : String,
  val link_url : String,
  val num_reports : Int,
  val parent_id : String,
  val replies : List[Comment],
  val saved : Boolean,
  val score : Int,
  val score_hidden : Boolean,
  val subreddit : String,
  val subreddit_id : String,
  val distinguished : String
  ) extends BaseObject(id,name) {}

// replies have kind_t1 but may have kind "more" and be links to other child comments
