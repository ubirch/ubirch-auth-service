package com.ubirch.auth.config

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object ConfigKeys {

  private final val prefix = "ubirchAuthService"

  /*
   * general server configs
   *********************************************************************************************/

  final val INTERFACE = s"$prefix.interface"
  final val PORT = s"$prefix.port"
  final val TIMEOUT = s"$prefix.timeout"

  /*
   * Akka related configs
   *********************************************************************************************/

  private val akkaPrefix = s"$prefix.akka"

  final val ACTOR_TIMEOUT = s"$akkaPrefix.actorTimeout"
  final val AKKA_NUMBER_OF_WORKERS = s"$akkaPrefix.numberOfWorkers"

  /*
   * OpenID Connect (= oidc)
   *********************************************************************************************/

  private val oidc = s"$prefix.openIdConnect"

  private val oidcContextPrefix = s"$oidc.context"

  private def oidcContextPrefix(context: String): String = s"$oidcContextPrefix.$context"

  private def oidcContextProviderPrefix(context: String, provider: String) = s"${oidcContextPrefix(context)}.$provider"

  final def oidcClientId(context: String, provider: String) = s"${oidcContextProviderPrefix(context, provider)}.clientId"

  final def oidcClientSecret(context: String, provider: String) = s"${oidcContextProviderPrefix(context, provider)}.clientSecret"

  final def oidcCallbackUrl(context: String, provider: String) = s"${oidcContextProviderPrefix(context, provider)}.callbackUrl"

  final val OIDC_STATE_TTL = s"$oidc.state.ttl"

  final val OIDC_TOKEN_TTL = s"$oidc.token.ttl"

}
