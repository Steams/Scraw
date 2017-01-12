package com.steams.scraw

import pureconfig.loadConfig

object scrawbot{

  def main(args: Array[String]){

    val config : InstanceConfig = loadConfig[InstanceConfig]("bot").get

    implicit val instance : Reddit = Reddit(config)

    println("Your user name is : " + instance.owner.name)

    // val cscq = Subreddit("cscareerquestions")
    // println(cscq.title)

    // val cscq_listing = cscq.hot().limit(5)
    // println(cscq_listing.children.size)

    // val poe = Subreddit("pathofexile")

    // val posts = poe.top().time("all").limit(2)

    //this is implicitly converted from StreamSlice to PostStream when you try to use it like an itterator
    // for(x <- posts){
    //   println(x.url + " : Score : " + x.score)
    //   println("Comments should be at url : " + x.id)
    // }

    // val user = User("lordtuts")
    // println(user.name + "'s link karma is : " + user.link_karma)

    val test_post = Post("5nczpg")
    println("Post title is : " + test_post.title )

    val comments = test_post.comments().top()


    for( x <- comments){
      printComments(x,1)
    }
  }

  def printComments( comment : Comment, indent : Int) : Unit = {
    println("")
    for(x <- (1 to indent)){
      print("\t")
    }
    println(" " + comment.author + " :> " + comment.body + " ")
    println("")

    comment.replies match {
      case Some(_) => for(x <- comment.replies.get){ printComments(x,indent+1) }
      case None =>
    }
  }
}
