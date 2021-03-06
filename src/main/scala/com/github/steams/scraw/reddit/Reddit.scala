package com.github.steams.scraw.reddit

import com.github.steams.scraw.http.OauthService
import com.github.steams.scraw.users.{User, UsersService}
import com.github.steams.scraw.utils.InstanceConfig

case class Reddit (val username : String, val password : String, val access_token : String) {

  def owner : User = UsersService.getUser("me",this)

}

object Reddit{

  def apply(config: InstanceConfig) : Reddit = {

    lazy val access_token =  OauthService.authenticate(
      config.client_id,
      config.client_secret,
      config.user_agent,
      config.username,
      config.password
    ).get

    println(access_token)

    return Reddit(config.username,config.password,access_token)
  }
}
