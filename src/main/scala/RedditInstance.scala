package scraw

import scraw.users.{UsersService,User}
import scraw.utils.InstanceConfig
import scraw.http.OauthService

case class RedditInstance private (val username : String, val password : String, val access_token : String) {

  def owner : User = UsersService.getUser("me",this)

}

object RedditInstance{

  def apply(config: InstanceConfig) : RedditInstance = {

    val access_token =  OauthService.authenticate(
      config.client_id,
      config.client_secret,
      config.user_agent,
      config.username,
      config.password
    ).get

    return RedditInstance(config.username,config.password,access_token)
  }
}
