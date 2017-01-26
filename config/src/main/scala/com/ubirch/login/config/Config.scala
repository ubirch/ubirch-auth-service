package com.ubirch.login.config

import com.ubirch.util.config.ConfigBase

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object Config extends ConfigBase {

  /**
    * The interface the server runs on.
    *
    * @return interface
    */
  def interface: String = config.getString(ConfigKeys.INTERFACE)

  /**
    * Port the server listens on.
    *
    * @return port number
    */
  def port: Int = config.getInt(ConfigKeys.PORT)

  /**
    * Default server timeout.
    *
    * @return timeout in seconds
    */
  def timeout: Int = config.getInt(ConfigKeys.TIMEOUT)

  /**
    * Default actor timeout.
    *
    * @return timeout in seconds
    */
  def actorTimeout: Int = config.getInt(ConfigKeys.ACTOR_TIMEOUT)

  /*
   * OpenID Connect Related
   ************************************************************************************************/

  def openIdConnectGenericClientId: String = config.getString(ConfigKeys.OPENID_CONNECT_GENERIC_CLIENT_ID)
  def openIdConnectGenericCallbackUrl: String = config.getString(ConfigKeys.OPENID_CONNECT_GENERIC_CALLBACK_URL)
  def openIdConnectGenericScope: String = config.getString(ConfigKeys.OPENID_CONNECT_GENERIC_SCOPE)

}
