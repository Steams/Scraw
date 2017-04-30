package com.github.steams.scraw.comments

import com.github.steams.scraw.reddit.Reddit
import com.github.steams.scraw.utils.JsonHandler
import com.github.steams.scraw.http.HttpService
import com.github.steams.scraw.http.Endpoint

private[scraw] trait CommentServiceStub {
  def getMoreCommentsFlat( link_id : LinkId, children : List[String], reddit : Reddit) : CommentStream
}

private[scraw] object CommentService extends JsonHandler {

  //this exists for mock testint purposes
  var implementation : Option[CommentServiceStub] = None
  // def setImplementation( name :String) : Unit = {implementation = name }

  def getStreamFromProfile( endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : CommentStream = {

    val response_body = HttpService.get(endpoint,reddit.access_token,params)

    val jval = parse(response_body)

    val comments_raw = jval \ "data" \ "children"

    val comments = CommentsFactory.fromForrest(comments_raw.children, LinkId(""), reddit)

    val comment_stream = CommentStream("", "", "", comments)

    return comment_stream
  }

  def getStream( endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : CommentStream = {

    val response_body = HttpService.get(endpoint,reddit.access_token,params)

    val jval = Jsonator.parse(response_body)

    //get the second child of the returned array. This structure contains the comments for the post
    val comments_listing = jval.children(1).property("data")

    val link_id = LinkId((jval.children(0) \ "data" \ "children").children(0) \ "data" \ "id" toString())

    val comments_list = comments_listing.property("children").children

    val comments = CommentsFactory.fromForrest(comments_list, link_id, reddit)

    val comment_stream = CommentStream(
      (comments_listing \ "modhash"),
      (comments_listing \ "after"),
      (comments_listing \ "before"),
      comments)

    return comment_stream
  }

  def getMoreComments( link : LinkId, children : List[String], reddit : Reddit) : CommentStream = {

    println("Getting more comments from service")

    println(children.mkString(","))

    val response_body = HttpService.post(
      Endpoint.more_children,
      Seq(
        "link_id" -> link.toString,
        "children" -> children.mkString(","),
        "api_type" -> "json"
      ),
      reddit.access_token
    )

    val json = Jsonator.parse(response_body)

    println(response_body)

    val comments_json = (json \ "json" \ "data" \ "things").children

    val comments = CommentsFactory.fromList(comments_json, link, reddit)

    val comment_stream = CommentStream("","","",comments)

    return comment_stream
  }

  def getMoreCommentsFlat( link_id : LinkId, children : List[String], reddit : Reddit) : CommentStream = {

    implementation match {
      case Some(x) => {
        val comments_stream = x.getMoreCommentsFlat(link_id, children, reddit)
        implementation = None
        return comments_stream
      }
      case None =>
    }

    val response_body = HttpService.post(
      Endpoint.more_children,
      Seq(
        "link_id" -> link_id.toString(),
        "children" -> children.mkString(","),
        "api_type" -> "json"
      ),
      reddit.access_token
    )

    val json = Jsonator.parse(response_body)

    //get the second child of the returned array. This structure contains the comments for the post
    val comments_json = (json \ "json" \ "data" \ "things").children

    val comments = CommentsFactory.buildCommentsList(comments_json, link_id, reddit)

    val comment_stream = CommentStream("","","",comments)

    return comment_stream
  }
}
