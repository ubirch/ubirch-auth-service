package com.ubirch.auth.config

/**
  * author: cvandrei
  * since: 2017-03-03
  */
object RedisKeys {

  val OIDC_PROVIDER_PREFIX = "oidc.provider"

  final val OIDC_PROVIDER_LIST = s"$OIDC_PROVIDER_PREFIX.list"

  final def providerKey(provider: String) = s"$OIDC_PROVIDER_PREFIX.$provider"

}
