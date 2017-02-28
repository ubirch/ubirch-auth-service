package com.ubirch.auth.config

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

  /*
   * Akka Related
   ************************************************************************************************/

  /**
    * Default actor timeout.
    *
    * @return timeout in seconds
    */
  def actorTimeout: Int = config.getInt(ConfigKeys.ACTOR_TIMEOUT)

  def akkaNumberOfWorkers: Int = config.getInt(ConfigKeys.AKKA_NUMBER_OF_WORKERS)

  /*
   * OpenID Connect Related
   ************************************************************************************************/

  def oidcProviders: Seq[String] = config.getStringList(ConfigKeys.OIDC_PROVIDERS_LIST).toList

  def oidcProviderId(provider: String): String = config.getString(ConfigKeys.oidcId(provider))

  def oidcName(provider: String): String = config.getString(ConfigKeys.oidcName(provider))

  def oidcScope(provider: String): String = config.getString(ConfigKeys.oidcScope(provider))

  def oidcClientId(provider: String): String = config.getString(ConfigKeys.oidcClientId(provider))

  def oidcClientSecret(provider: String): String = config.getString(ConfigKeys.oidcClientSecret(provider))

  def oidcEndpointConfig(provider: String): String = config.getString(ConfigKeys.oidcEndpointConfig(provider))

  def oidcTokenSigningAlgorithms(provider: String): Seq[String] = config.getStringList(ConfigKeys.oidcTokenSigningAlgorithms(provider))

  def oidcAuthorizationEndpoint(provider: String): String = config.getString(ConfigKeys.oidcAuthorizationEndpoint(provider))

  def oidcTokenEndpoint(provider: String): String = config.getString(ConfigKeys.oidcTokenEndpoint(provider))

  def oidcJwksUri(provider: String): String = config.getString(ConfigKeys.oidcJwksUri(provider))

  def oidcCallbackUrl(provider: String): String = config.getString(ConfigKeys.oidcCallbackUrl(provider))

  /**
    * States are an additional OpenID Connect security feature. We create them when provider infos are queried and not
    * all of them will be used. Hence we want them the have a time-to-live (TTL).
    *
    * @return ttl in seconds
    */
  def oidcStateTtl(): Long = config.getLong(ConfigKeys.OIDC_STATE_TTL)

  /**
    * Tokens exist when a user successfully logged in but not without a time-to-live (TTL).
    *
    * @return ttl in seconds
    */
  def oidcTokenTtl(): Long = config.getLong(ConfigKeys.OIDC_TOKEN_TTL)

  /*
   * General OpenID Connect Provider Configs
   ************************************************************************************************/

  def oidcProvidersActive: Seq[String] = config.getStringList(ConfigKeys.OIDC_PROVIDERS_ACTIVE).toList

  def oidcProviderConfig(provider: String): OidcProviderConfig = {

    OidcProviderConfig(
      id = oidcActiveProviderId(provider),
      name = oidcActiveProviderName(provider),
      scope = oidcActiveProviderScope(provider),
      endpointConfig = oidcActiveProviderEndpointConfig(provider),
      tokenSigningAlgorithms = oidcActiveProviderTokenSigningAlgorithms(provider),
      endpoints = OidcProviderEndpoints(
        authorization = oidcActiveProviderAuthorizationEndpoint(provider),
        token = oidcActiveProviderTokenEndpoint(provider),
        jwks = oidcActiveProviderJwksUri(provider)
      )
    )

  }

  private def oidcActiveProviderId(provider: String): String = config.getString(ConfigKeys.oidcId(provider))

  private def oidcActiveProviderName(provider: String): String = config.getString(ConfigKeys.oidcName(provider))

  private def oidcActiveProviderScope(provider: String): String = config.getString(ConfigKeys.oidcScope(provider))

  private def oidcActiveProviderEndpointConfig(provider: String): String = config.getString(ConfigKeys.oidcEndpointConfig(provider))

  private def oidcActiveProviderTokenSigningAlgorithms(provider: String): Seq[String] = config.getStringList(ConfigKeys.oidcTokenSigningAlgorithms(provider))

  private def oidcActiveProviderAuthorizationEndpoint(provider: String): String = config.getString(ConfigKeys.oidcAuthorizationEndpoint(provider))

  private def oidcActiveProviderTokenEndpoint(provider: String): String = config.getString(ConfigKeys.oidcTokenEndpoint(provider))

  private def oidcActiveProviderJwksUri(provider: String): String = config.getString(ConfigKeys.oidcJwksUri(provider))

}

case class OidcProviderConfig(id: String,
                              name: String,
                              scope: String,
                              endpointConfig: String,
                              tokenSigningAlgorithms: Seq[String],
                              endpoints: OidcProviderEndpoints
                             )

case class OidcProviderEndpoints(authorization: String,
                                 token: String,
                                 jwks: String
                                )
