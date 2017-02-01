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
  final val ACTOR_TIMEOUT = s"$prefix.actorTimeout"

  /*
   * Redis
   *********************************************************************************************/

  private val redisPrefix = s"$prefix.redis"

  final val REDIS_HOST = s"$redisPrefix.host"
  final val REDIS_PORT = s"$redisPrefix.port"
  final val REDIS_DATABASE = s"$redisPrefix.database"
  final val REDIS_PASSWORD = s"$redisPrefix.password"

  /*
   * OpenID Connect (= oidc)
   *********************************************************************************************/

  private val oidcProvidersPrefix = s"$prefix.openIdConnectProviders"

  final val OIDC_PROVIDERS_LIST = s"$oidcProvidersPrefix.providerList"

  final def oidcProviderId(provider: String) = s"$oidcProvidersPrefix.$provider.id"

  final def oidcProviderName(provider: String) = s"$oidcProvidersPrefix.$provider.name"

  final def oidcProviderScope(provider: String) = s"$oidcProvidersPrefix.$provider.scope"

  final def oidcProviderClientId(provider: String) = s"$oidcProvidersPrefix.$provider.clientId"

  final def oidcProviderClientSecret(provider: String) = s"$oidcProvidersPrefix.$provider.clientSecret"

  def oidcProviderEndpointConfig(provider: String): String = s"$oidcProvidersPrefix.$provider.endpointConfig"

  private def oidcEndpointsPrefix(provider: String): String = s"$oidcProvidersPrefix.$provider.endpoints"

  final def oidcProviderAuthorizationEndpoint(provider: String) = s"${oidcEndpointsPrefix(provider)}.authorization"

  final def oidcProviderTokenEndpoint(provider: String) = s"${oidcEndpointsPrefix(provider)}.token"

  final def oidcProviderUserInfoEndpoint(provider: String) = s"${oidcEndpointsPrefix(provider)}.userInfo"

  final def oidcProviderCallbackUrl(provider: String) = s"$oidcProvidersPrefix.$provider.callbackUrl"

}
