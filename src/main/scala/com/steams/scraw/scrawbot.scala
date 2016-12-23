package com.steams.scraw

import pureconfig.loadConfig

object scrawbot{

  def main(args: Array[String]){

    val config : InstanceConfig = loadConfig[InstanceConfig]("bot").get

    implicit val instance : Reddit = reddit.Reddit(config)

    println("Your user name is : " + instance.owner.name)

    val cscq = Subreddit("cscareerquestions")
    println(cscq.title)

    val cscq_listing = cscq.hot().limit(5)
    //this should implicitly convert from PostListingBuilder to PostListing
    println(cscq_listing.children.size)


    val user = User("lordtuts")

    println(user.name + "'s link karma is : " + user.link_karma)

  }
}
