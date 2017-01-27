package com.ubirch.login.config

import com.ubirch.util.config.ConfigBase

import scala.collection.JavaConversions._

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

  def oidcProviders: Seq[String] = config.getStringList(ConfigKeys.OIDC_PROVIDERS_LIST).toList

  def oidcProviderName(provider: String): String = config.getString(ConfigKeys.oidcProviderName(provider))

  def oidcProviderScope(provider: String): String = config.getString(ConfigKeys.oidcProviderScope(provider))

  def oidcProviderLogoUrl(provider: String): String = config.getString(ConfigKeys.oidcProviderLogoUrl(provider))

  def oidcProviderClientId(provider: String): String = config.getString(ConfigKeys.oidcProviderClientId(provider))

  def oidcProviderClientSecret(provider: String): String = config.getString(ConfigKeys.oidcProviderClientSecret(provider))

  def oidcProviderEndpointConfig(provider: String): String = config.getString(ConfigKeys.oidcProviderEndpointConfig(provider))

  def oidcProviderAuthorizationEndpoint(provider: String): String = config.getString(ConfigKeys.oidcProviderAuthorizationEndpoint(provider))

  def oidcProviderTokenEndpoint(provider: String): String = config.getString(ConfigKeys.oidcProviderTokenEndpoint(provider))

  def oidcProviderUserInfoEndpoint(provider: String): String = config.getString(ConfigKeys.oidcProviderUserInfoEndpoint(provider))

  def oidcProviderCallbackUrl(provider: String): String = config.getString(ConfigKeys.oidcProviderCallbackUrl(provider))

}
