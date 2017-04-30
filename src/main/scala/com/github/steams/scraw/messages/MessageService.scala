package com.github.steams.scraw.messages

import com.github.steams.scraw.reddit.Reddit
import com.github.steams.scraw.utils.JsonHandler
import com.github.steams.scraw.http.HttpService
import com.github.steams.scraw.http.Endpoint

private[scraw] object MessageService extends JsonHandler {

  def block_author(fullname : String, instance : Reddit) = HttpService.post(Endpoint.block, Seq("id" -> fullname), instance.access_token)

  def send_pm( subject : String, message : String, name : String, instance : Reddit) : Unit = {
    val response_body = HttpService.post(
      Endpoint.private_message,
      Seq("api_type" -> "json",
          "from_sr" -> "",
          "subject" -> subject,
          "text" -> message,
          "to" -> name
      ),
      instance.access_token
    )
  }

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
