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

    // val test_post = Post("61orqk")
    val test_post = Post("61tr7k")
    println("Post title is : " + test_post.title )
    // test_post.reply("Test comment number 2")
    test_post.comments.containing("all").foreach( x => x.edit(x.body++" edited yah") )

    // val comments = test_post.comments().matching(".*(r|R)ust.*".r)

    // comments.foreach( x => println(x.author + " :> " + x.body + "\n"))
    // for( comment <- comments; if comment.isInstanceOf[Comment]) {
    //   comment.asInstanceOf[Comment].clearvote
    // }

    // println("Full Comment count : " + printCount)

  }

  def printComments( comment : Commentifiable, indent : Int)(implicit instance: Reddit) : Unit = {
    comment match {
      case x : Comment => {
        println("")
        (1 to indent).foreach( _ => print("\t"))

        println(" " + x.author + " :> " + x.body + "\n")

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
