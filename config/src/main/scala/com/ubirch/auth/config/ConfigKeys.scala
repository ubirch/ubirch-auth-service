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

  private val oidcPrefix = s"$prefix.openIdConnectProviders"

  final val OIDC_PROVIDERS_LIST = s"$oidcPrefix.providerList"

  private def oidcProviderPrefix(provider: String) = s"$oidcPrefix.$provider"

  final def oidcId(provider: String) = s"${oidcProviderPrefix(provider)}.id"

  final def oidcName(provider: String) = s"${oidcProviderPrefix(provider)}.name"

  final def oidcScope(provider: String) = s"${oidcProviderPrefix(provider)}.scope"

  final def oidcClientId(provider: String) = s"${oidcProviderPrefix(provider)}.clientId"

  final def oidcClientSecret(provider: String) = s"${oidcProviderPrefix(provider)}.clientSecret"

  final def oidcEndpointConfig(provider: String): String = s"${oidcProviderPrefix(provider)}.endpointConfig"

  final def oidcTokenSigningAlgorithms(provider: String) = s"${oidcProviderPrefix(provider)}.tokenSigningAlgorithms"

  private def oidcEndpointsPrefix(provider: String): String = s"${oidcProviderPrefix(provider)}.endpoints"

  final def oidcAuthorizationEndpoint(provider: String) = s"${oidcEndpointsPrefix(provider)}.authorization"

  final def oidcTokenEndpoint(provider: String) = s"${oidcEndpointsPrefix(provider)}.token"

  final def oidcJwksUri(provider: String) = s"${oidcEndpointsPrefix(provider)}.jwks"

  final def oidcCallbackUrl(provider: String) = s"${oidcProviderPrefix(provider)}.callbackUrl"

  final val OIDC_STATE_TTL = s"$oidcPrefix.state.ttl"

  final val OIDC_TOKEN_TTL = s"$oidcPrefix.token.ttl"

  /*
   * OpenID Connect (= oidc) Providers
   *********************************************************************************************/

  // TODO shorten all method and variable names once we got rid of most of those in the above section

  private val oidcProviderDetailsPrefix = s"$oidcPrefix.details"

  final val OIDC_PROVIDERS_ACTIVE = s"$oidcProviderDetailsPrefix.active"

  private def oidcProviderDetailsProviderPrefix(provider: String) = s"$oidcProviderDetailsPrefix.$provider"

  final def oidcActiveProviderId(provider: String) = s"${oidcProviderDetailsProviderPrefix(provider)}.id"

  final def oidcActiveProviderName(provider: String) = s"${oidcProviderDetailsProviderPrefix(provider)}.name"

  final def oidcActiveProviderScope(provider: String) = s"${oidcProviderDetailsProviderPrefix(provider)}.scope"

  final def oidcActiveProviderEndpointConfig(provider: String): String = s"${oidcProviderDetailsProviderPrefix(provider)}.endpointConfig"

  final def oidcActiveProviderTokenSigningAlgorithms(provider: String) = s"${oidcProviderDetailsProviderPrefix(provider)}.tokenSigningAlgorithms"

  private def oidcActiveProviderEndpointsPrefix(provider: String): String = s"${oidcProviderDetailsProviderPrefix(provider)}.endpoints"

  final def oidcActiveProviderAuthorizationEndpoint(provider: String) = s"${oidcActiveProviderEndpointsPrefix(provider)}.authorization"

  final def oidcActiveProviderTokenEndpoint(provider: String) = s"${oidcActiveProviderEndpointsPrefix(provider)}.token"

  final def oidcActiveProviderJwksUri(provider: String) = s"${oidcActiveProviderEndpointsPrefix(provider)}.jwks"

}
