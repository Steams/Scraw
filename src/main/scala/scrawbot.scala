package scraw

import pureconfig.loadConfig

import scraw.subreddits.Subreddit
import scraw.users.User
import scraw.utils.InstanceConfig


object scrawbot{

  def main(args: Array[String]){

    val config : InstanceConfig = loadConfig[InstanceConfig]("bot").get

    implicit val instance : RedditInstance = RedditInstance(config)

    println("Your user name is : " + instance.owner.name)

    val cscq = Subreddit("cscareerquestions")

    println(cscq.title)

    val user = User("lordtuts")

    println(user.name + "'s link karma is : " + user.link_karma)

  }
}
