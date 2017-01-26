package com.ubirch.login.config

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object ConfigKeys {

  private final val prefix = "ubirchLoginService"

  /*
   * general server configs
   *********************************************************************************************/

  final val INTERFACE = s"$prefix.interface"
  final val PORT = s"$prefix.port"
  final val TIMEOUT = s"$prefix.timeout"
  final val ACTOR_TIMEOUT = s"$prefix.actorTimeout"

  /*
   * OpenID Connect (= oidc)
   *********************************************************************************************/

  private val oidcProvidersPrefix = s"$prefix.openIdConnectProviders"
  final val OIDC_PROVIDERS_LIST = s"$oidcProvidersPrefix.providerList"

  final def oidcProviderName(provider: String) = s"$oidcProvidersPrefix.$provider.name"

  final def oidcProviderScope(provider: String) = s"$oidcProvidersPrefix.$provider.scope"

  final def oidcProviderLogoUrl(provider: String) = s"$oidcProvidersPrefix.$provider.logoUrl"

  final def oidcProviderClientId(provider: String) = s"$oidcProvidersPrefix.$provider.clientId"

  final def oidcProviderLoginUrl(provider: String) = s"$oidcProvidersPrefix.$provider.loginUrl"

  final def oidcProviderCallbackUrl(provider: String) = s"$oidcProvidersPrefix.$provider.callbackUrl"

}
