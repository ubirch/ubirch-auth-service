package com.ubirch.auth.config

/**
  * author: cvandrei
  * since: 2017-03-03
  */
object RedisKeys {

  private val oidc = "oidc"

  val OIDC_PROVIDER_PREFIX = s"$oidc.provider"

  final val OIDC_PROVIDER_LIST = s"$OIDC_PROVIDER_PREFIX.list"

  final def providerKey(provider: String) = s"$OIDC_PROVIDER_PREFIX.$provider"

  val OIDC_CONTEXT_PREFIX = s"$oidc.context"

  val OIDC_CONTEXT_LIST = s"$OIDC_CONTEXT_PREFIX.list"

}
