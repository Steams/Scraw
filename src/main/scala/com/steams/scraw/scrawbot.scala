package com.steams.scraw

import pureconfig.loadConfig

object scrawbot{

  var printCount = 0

  def main(args: Array[String]) : Unit = {

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

    // val test_post = Post("5iv0hf")
    val test_post = Post("61hsy5")
    println("Post title is : " + test_post.title )

    val comments = test_post.comments()

    comments.foreach( x => printComments(x,1))
    println("Full Comment count : " + printCount)

  }

  def printComments( comment : Commentifiable, indent : Int) : Unit = {
    comment match {
      case x : Comment => {
        println("")
        for(x <- (1 to indent)){
          print("\t")
        }
        println(" " + x.author + " :> " + x.body + " ")
        println("")
        printCount += 1

        x.replies match {
          case Some(_) => for(reply <- x.replies.get){
            printComments(reply,indent+1)
          }
          case None =>
        }
      }
      case x : CommentsLink => {
        println("loading comments ["+x.count+"]")
        for(comment <- x.get ){
          printComments(comment,indent)
        }
      }
    }

  }
}
