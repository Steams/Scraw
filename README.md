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

###### Get/Subscribe/Unsubscribe Subreddit

```scala
  val cscq = Subreddit("cscareerquestions")
  //the Subreddit apply method takes a name and an implicit Reddit Instance declared above. 
  //It can also be called like this : Subreddit("cscareerquestions")(instance) if you'd rather not use implicits

  println(cscq.title)
  println("Number of subscribers : " + cscq.subscribers )

  cscq.subscribe
  Subreddit("creepy").unsubscribe
```
###### Get Posts

```scala
    val poe = Subreddit("pathofexile")

    val posts = poe.top.time("all").limit(2)
    //posts here is of type PostStreamSlice, a type returned by Subreddit.top,Subreddit.newest etc.
    //this type exposes an interface for refining your query by count, limit, before, after etc.
    //the http request is not made until you attempt to call an Iterator method on the StreamSlice
    
    //here the PostStreamSlice is implicitly converted into a PostStream...
    //...which extends Iterable and exposes an iterator to the retrieved posts
    for(x <- posts){
      println(x.title + " : Score : " + x.score)
    }
    
    val single_post = Post("5l2xs1")
    println("Post title is : " + single_post.title )
    single_post.upvote
    single_post.save
   
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

    //these are top level comments which each contain a list of replies
    //a function to flatten the tree will be provided
    val comments = test_post.comments.newest

    comments.foreach( x => printComments(x,1))

    //comments can be either comments or "more comments" links
    //The Commentifiable base class abstracts over both of these

    def printComments( x : Commentifiable, indent : Int) : Unit = {
      x match {
        case comment : Comment => {
          println("")
          (1 to indent).foreach( _ => print("\t"))

          println(" " + comment.author + " :> " + comment.body + "\n")
          comment.clearvote
          comment.unsave

          comment.replies match {
            case Some(replies : List[Commentifiable]) => replies.foreach( r => printComments(r,indent+1))
            case None =>
          }
        }

        case link : CommentsLink => {
          println("loading comments ["+link.count+"]")
          link.get.map( c => printComments(c,indent))
        }
      }
    }
````

###### Filtering Comments
````scala
    val test_post = Post("5l2xs1")
    println("Post title is : " + test_post.title )

    // You can filter comments by a string they contain or matching on a regex of your own
    // These functions will load all the comments in the thread (including those hidden 
    // behind "more comments" links) and return them filtered by your criteria

    test_post.comments.containing("explosion").foreach( x => printComments(x,1) )

    val comments = test_post.comments.matching(".*(r|R)ust.*".r)
````

###### Reply to Comments and Posts
````scala
    val test_post = Post("5l2xs1")
    
    //remember, the reddit instance is implicitly passed here if it is in scope
    // the actual function is reply(content: String)(implicit instance: Reddit)
    test_post.reply("Test comment number 2")
    test_post.comments.containing("normie").foreach( x => x.reply("REEEEEEEEEEEeEEE"))

````

###### Vote on,Delete,Edit,Report,Save/Unsave Posts and Comments
````scala
    test_post.save

    val comments = test_post.comments.matching(".*(r|R)ust.*".r).toList

     for( comment <- comments) {
       comment.upvote //or downvote or clearvote
     }
     
     comments.filter( x => x.author == instance.username).foreach(x => x.edit(x.body ++ "\n added some text here"))

     comments.filter( x => x.author == instance.username).contains("anime").foreach(x => x.delete)

````


###### Get/Send private messages (messages,comment replies, post replies, sent, unread, mentions)
````scala
    //all messages
    val my_messages = instance.owner.inbox
    for(x <- my_messages){
      println(x.body)
    }

    //unread messages
    val unread_messages = instance.owner.unread
    for(x <- unread_messages){
      println(x.body)
    }

    //sent messages
    val unread_messages = instance.owner.sent
    for(x <- unread_messages){
      println(x.body)
    }

    //etc...
    val messages = instance.owner.post_replies
    for(x <- messages){
      println(x.body)
    }
    
    User("Gallowboob").send_pm("Subject: reposting","stop doing it")

````

###### Get saved/submitted posts/comments and subscribed subreddits
```scala

    for(x <- instance.owner.saved_posts.limit(2)){
      println(x.title)
    }

    for(x <- instance.owner.saved_comments){
      println(x.asInstanceOf[Comment].body)
    }

    for(x <- instance.owner.comments){
      println(x.asInstanceOf[Comment].body)
    }

    for(x <- instance.owner.submitted.count(10)){
      println(x.title)
    }

    for(x <- instance.owner.subscribed){
      println(x.display_name)
    }
```
