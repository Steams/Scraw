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

    val test_post = Post("61orqk")
    println("Post title is : " + test_post.title )
    test_post.upvote
    test_post.unsave

    val comments = test_post.comments().toList

    comments.foreach( x => printComments(x,1))
    for( comment <- comments; if comment.isInstanceOf[Comment]) {
      comment.asInstanceOf[Comment].clearvote
    }

    println("Full Comment count : " + printCount)

  }

  def printComments( comment : Commentifiable, indent : Int)(implicit instance: Reddit) : Unit = {
    comment match {
      case x : Comment => {
        println("")
        (1 to indent).foreach( _ => print("\t"))

        println(" " + x.author + " :> " + x.body + "\n")
        println("Authors karma is " + x.user.link_karma)
        println("Post title is" + x.post.title)

        printCount += 1

        x.replies match {
          case Some(replies : List[Commentifiable]) => replies.foreach( r => printComments(r,indent+1))
          case None => ()
        }
      }
      case link : CommentsLink => {
        println("loading comments ["+link.count+"]")
        link.get.map( c => printComments(c,indent))
      }
    }

  }
}
