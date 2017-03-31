package com.steams.scraw.messages

import com.steams.scraw.reddit.Reddit
import com.steams.scraw.utils.JsonHandler
import com.steams.scraw.http.HttpService

private[scraw] object MessageService extends JsonHandler {


  def getStream( endpoint : String, params : Map[String,Option[String]], reddit : Reddit) : MessageStream = {

    val response_body = HttpService.get(endpoint,reddit.access_token,params.empty)

    val messages_listing = parse(response_body) \ "data"

    val messages = (messages_listing \ "children").children.map( c => (c \ "data").extract[Message])

    val message_stream = MessageStream(
      (messages_listing \ "modhash"),
      (messages_listing \ "after"),
      (messages_listing \ "before"),
      messages)

    return message_stream
  }

}
