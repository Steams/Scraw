# Scraw
Scala Reddit API Wrapper library (Work in progress). Inspired by [PRAW](https://github.com/praw-dev/praw) and [JRAW](https://github.com/thatJavaNerd/JRAW).

The aim of this project is to provide a wrapper for Reddit's API which exposes an easy to use interface in idiomatic, functional scala. 

This library is intended as a convinience to those wishing to build reddit bots, scrapers or applications using the Scala language and tooling.

### Current API and Implemented Functionality (as of this commit)

###### Oauth Authentication

```scala
import com.steams.scraw._

val configs = InstanceConfig("username","password","client_id","client_secret","user_agent")
implicit val instance : Reddit = Reddit(configs)  
//declaring your reddit instance as implicit adds some convenience when using other methods

println("Your access token is : " + instance.access_token)
println("Your user name is : " + instance.owner.name)
println("Your comment karma is : " + instance.owner.comment_karma)
```

###### Get Subreddit

```scala
val cscq = Subreddit("cscareerquestions")  
//the Subreddit apply method takes a name and an implicit Reddit Instance declared above. 
//It can also be called like this : Subreddit("cscareerquestions")(instance) if you'd rather not use implicits

println(cscq.title)
println("Number of subscribers : " + cscq.subscribers )

```
###### Get Posts

```scala
    val poe = Subreddit("pathofexile")

    val posts = poe.top().time("all").limit(2)
    //posts here is of type PostStreamSlice, a type returned by Subreddit.top,Subreddit.newest etc.
    //this type exposes an interface for refining your query by count, limit, before, after etc.
    //the http request is not made until you attempt to call an Iterator method on the StreamSlice
    
    //here the PostStreamSlice is implicitly converted into a PostStream...
    //...which extends Iterable and exposes an iterator to the retrieved posts
    
    for(x <- posts){
      println(x.title + " : Score : " + x.score)
    }
    
    val single_post = Post("5l2xs1")
    println("Post title is : " + test_post.title )
   
```

###### Get user
```scala
    val user = User("lordtuts")
    println(user.name + "'s link karma is : " + user.link_karma)
```

###### Get Comments From Post
````scala
    val test_post = Post("5l2xs1")
    println("Post title is : " + test_post.title )
    val comments = test_post.comments().newest()

    for(x <- comments){
      x match {
        case comment : Comment => println(" " + x.author + " :> " + x.body + " ")
        case link : CommentsLink => println("Load more comments : " + link.name)
      }
    }
    //these are top level comments which each contain a list of replies
    //a function to flatten the tree will be provided

````

