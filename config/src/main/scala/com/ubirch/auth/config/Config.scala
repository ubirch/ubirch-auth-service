package com.ubirch.auth.config

import java.net.URI

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

  def oidcContextProvidersList(context: String): Seq[String] = config.getStringList(ConfigKeys.oidcContextProvidersList(context))

  def oidcContextProviderConfig(context: String, provider: String): ContextProviderConfig = {
    ContextProviderConfig(
      context = context,
      provider = provider,
      clientId = oidcClientId(context = context, provider = provider),
      clientSecret = oidcClientSecret(context = context, provider = provider),
      callbackUrl = new URI(oidcCallbackUrl(context = context, provider = provider))
    )
  }

  private def oidcClientId(context: String, provider: String): String = config.getString(ConfigKeys.oidcClientId(context, provider))

  private def oidcClientSecret(context: String, provider: String): String = config.getString(ConfigKeys.oidcClientSecret(context, provider))

  private def oidcCallbackUrl(context: String, provider: String): String = config.getString(ConfigKeys.oidcCallbackUrl(context, provider))

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

}

case class ContextProviderConfig(context: String,
                                 provider: String,
                                 clientId: String,
                                 clientSecret: String,
                                 callbackUrl: URI
                                )

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
