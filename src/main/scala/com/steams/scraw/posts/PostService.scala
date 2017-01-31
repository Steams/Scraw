package com.steams.scraw.posts

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.JsonHandler
import com.steams.scraw.http.{Endpoint,HttpService}


object PostService extends JsonHandler {

  def getPost(id : String, reddit : Reddit) : Post = {

    val response_body = HttpService.get(Endpoint.about_post(id),reddit.access_token)

    //the request will return with an array "children" with only 1 child
    val stuff = parse(response_body) \ "data" \ "children"

    //children(0) gets the first child of the node which happens to be NAMED "children" 
    return stuff.children(0).property("data").extract[Post]
  }

  // def getComments(endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : PostStream = {

  //   val response_body = HttpService.get(endpoint,reddit.access_token,params)

  //   val jval = parse(response_body)

  //   val posts = for(post <- (jval \ "data" \ "children").children) yield {(post \ "data").extract[Post]}

  //   return PostStream((jval \ "data" \ "modhash"), (jval \ "data" \ "after"), (jval \ "data" \ "before"), posts)
  // }
}
