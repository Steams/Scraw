package com.github.steams

import scala.language.implicitConversions
import com.github.steams.scraw.utils.StreamSlice

package object scraw {

  type InstanceConfig = com.github.steams.scraw.utils.InstanceConfig

  type Reddit = reddit.Reddit
  val Reddit = com.github.steams.scraw.reddit.Reddit

  type Subreddit = com.github.steams.scraw.subreddits.Subreddit
  val Subreddit = com.github.steams.scraw.subreddits.Subreddit

  type Post = com.github.steams.scraw.posts.Post
  val Post = com.github.steams.scraw.posts.Post

  //rename this from User
  type User = com.github.steams.scraw.users.User
  val User = com.github.steams.scraw.users.User

  type Commentifiable = com.github.steams.scraw.comments.Commentifiable

  type Comment = com.github.steams.scraw.comments.Comment
  val Comment = com.github.steams.scraw.comments.Comment

  type CommentsLink = com.github.steams.scraw.comments.CommentsLink

  type Message = com.github.steams.scraw.messages.Message
  val Message = com.github.steams.scraw.messages.Message

  implicit def StreamSliceToStream[A <: Iterable[Any] ]( slice : StreamSlice[A] ) : A =  slice.getStream()

}
