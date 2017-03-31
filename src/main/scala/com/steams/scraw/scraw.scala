package com.steams

import scala.language.implicitConversions
import com.steams.scraw.utils.StreamSlice

package object scraw {

  type InstanceConfig = com.steams.scraw.utils.InstanceConfig

  type Reddit = reddit.Reddit
  val Reddit = com.steams.scraw.reddit.Reddit

  type Subreddit = com.steams.scraw.subreddits.Subreddit
  val Subreddit = com.steams.scraw.subreddits.Subreddit

  type Post = com.steams.scraw.posts.Post
  val Post = com.steams.scraw.posts.Post

  //rename this from User
  type User = com.steams.scraw.users.User
  val User = com.steams.scraw.users.User

  type Commentifiable = com.steams.scraw.comments.Commentifiable

  type Comment = com.steams.scraw.comments.Comment
  val Comment = com.steams.scraw.comments.Comment

  type CommentsLink = com.steams.scraw.comments.CommentsLink

  type Message = com.steams.scraw.messages.Message
  val Message = com.steams.scraw.messages.Message

  implicit def StreamSliceToStream[A <: Iterable[Any] ]( slice : StreamSlice[A] ) : A =  slice.getStream()

}
