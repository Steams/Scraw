package com.steams

import scala.language.implicitConversions
import com.steams.scraw.posts.{PostStream, PostStreamSlice}

package object scraw {

  type InstanceConfig = com.steams.scraw.utils.InstanceConfig

  type Reddit = reddit.Reddit
  val Reddit = com.steams.scraw.reddit.Reddit

  type Subreddit = com.steams.scraw.subreddits.Subreddit
  val Subreddit = com.steams.scraw.subreddits.Subreddit

  //rename this from User
  type User = com.steams.scraw.users.User
  val User = com.steams.scraw.users.User


  implicit def PostListingBuilderToPostListing( slice : PostStreamSlice ) : PostStream =  slice.get()

}
