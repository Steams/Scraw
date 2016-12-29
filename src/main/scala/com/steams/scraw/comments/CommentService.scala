package com.steams.scraw.comments

import net.liftweb.json._

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.JsonHandler
import com.steams.scraw.http.{Endpoint,HttpService}

// object CommentService extends HandleJson {

//   def getCommentStream(post_id : String, reddit : Reddit) : Comment = {
//     val response_body

//   }

  // def getStream(endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : PostStream = {

  //   val response_body = HttpService.get(endpoint,reddit.access_token,params)

  //   val jval = parse(response_body)

  //   val posts = for(post <- (jval \ "data" \ "children").children) yield {(post \ "data").extract[Post]}

  //   return PostStream((jval \ "data" \ "modhash"), (jval \ "data" \ "after"), (jval \ "data" \ "before"), posts)
  // }
// }
