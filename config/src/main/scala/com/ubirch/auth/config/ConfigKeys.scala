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

  final val OIDC_CONTEXT_ACTIVE_LIST = s"$oidcContextPrefix.activeList"

  private def oidcContextPrefix(context: String): String = s"$oidcContextPrefix.$context"

  def oidcContextProvidersList(context: String): String = s"${oidcContextPrefix(context)}.providers"

  private def oidcContextProviderPrefix(context: String, provider: String) = s"${oidcContextPrefix(context)}.$provider"

  final def oidcClientId(context: String, provider: String) = s"${oidcContextProviderPrefix(context, provider)}.clientId"

  final def oidcClientSecret(context: String, provider: String) = s"${oidcContextProviderPrefix(context, provider)}.clientSecret"

  final def oidcCallbackUrl(context: String, provider: String) = s"${oidcContextProviderPrefix(context, provider)}.callbackUrl"

  final val OIDC_STATE_TTL = s"$oidc.state.ttl"

  final val OIDC_TOKEN_TTL = s"$oidc.token.ttl"

  /*
   * OpenID Connect (= oidc) Providers
   *********************************************************************************************/

  private val oidcProviderPrefix = s"$oidc.provider"

  private def oidcProviderPrefix(provider: String): String = s"$oidcProviderPrefix.$provider"

  final def oidcProviderName(provider: String) = s"${oidcProviderPrefix(provider)}.name"

  final def oidcScope(provider: String) = s"${oidcProviderPrefix(provider)}.scope"

  final def oidcEndpointConfig(provider: String) = s"${oidcProviderPrefix(provider)}.endpointConfig"

  final def oidcTokenSigningAlgorithms(provider: String) = s"${oidcProviderPrefix(provider)}.tokenSigningAlgorithms"

  private def oidcEndpointsPrefix(provider: String) = s"${oidcProviderPrefix(provider)}.endpoints"

  final def oidcEndpointAuthorization(provider: String) = s"${oidcEndpointsPrefix(provider)}.authorization"

  final def oidcEndpointToken(provider: String) = s"${oidcEndpointsPrefix(provider)}.token"

  final def oidcEndpointJwks(provider: String) = s"${oidcEndpointsPrefix(provider)}.jwks"

}
