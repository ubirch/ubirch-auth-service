package com.ubirch.auth.config

/**
  * author: cvandrei
  * since: 2017-03-03
  */
object RedisKeys {

  private val oidc = "oidc"

  /*
   * provider
   ******************************************************************/

  val OIDC_PROVIDER_PREFIX = s"$oidc.provider"

  final val OIDC_PROVIDER_LIST = s"$OIDC_PROVIDER_PREFIX.list"

  final def providerKey(provider: String) = s"$OIDC_PROVIDER_PREFIX.$provider"

  /*
   * context
   ******************************************************************/

  val OIDC_CONTEXT_PREFIX = s"$oidc.context"

  val OIDC_CONTEXT_LIST = s"$OIDC_CONTEXT_PREFIX.list"

  def oidcContextPrefix(context: String): String = s"$OIDC_CONTEXT_PREFIX.$context"

  def oidcContextProviderKey(context: String, provider: String): String = s"$OIDC_CONTEXT_PREFIX.$context.$provider"

}
