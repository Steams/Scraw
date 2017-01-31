package com.steams.scraw

import pureconfig.loadConfig

object scrawbot{

  def main(args: Array[String]){

    val config : InstanceConfig = loadConfig[InstanceConfig]("bot").get

    implicit val instance : Reddit = Reddit(config)

    // println("Your user name is : " + instance.owner.name)

    val cscq = Subreddit("cscareerquestions")
    println(cscq.title)

    val cscq_listing = cscq.hot().limit(5)

    val poe = Subreddit("pathofexile")

    val posts = poe.top().time("all").limit(2)

    // this is implicitly converted from StreamSlice to PostStream when you try to use it like an itterable
    for(x <- posts){
      println(x.url + " : Score : " + x.score)
      println("Comments should be at url : " + x.id)
    }

    val user = User("lordtuts")
    println(user.name + "'s link karma is : " + user.link_karma)

    val test_post = Post("5iv0hf")
    println("Post title is : " + test_post.title )

    val comments = test_post.comments()


    for( x <- comments){
      x match {
        case comment : Comment => printComments(comment,1)
        case link : CommentsLink => println("Load more comments : " + link.name)
      }
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
      case Some(_) => for(x <- comment.replies.get){
        x match {
          case comment : Comment => printComments(comment,indent+1)
          case link : CommentsLink => println("Load more comments : " + link.name)
        }
      }
      // case Some(_) => for(x <- comment.replies.get){
      //   x match {
      //     case Comment => printComments(x,indent+1)
      //     case MoreLink => for(p <- x.getComments){ printComments(x,indent+1)}
      //   }
      // }
      case None =>
    }
  }
}
