package com.ubirch.auth.config

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object ConfigKeys {

  final val CONFIG_PREFIX = "ubirchAuthService"

  /*
   * general server configs
   *********************************************************************************************/

  final val INTERFACE = s"$CONFIG_PREFIX.interface"
  final val PORT = s"$CONFIG_PREFIX.port"
  final val TIMEOUT = s"$CONFIG_PREFIX.timeout"

  /*
   * Akka related configs
   *********************************************************************************************/

  private val akkaPrefix = s"$CONFIG_PREFIX.akka"

  final val ACTOR_TIMEOUT = s"$akkaPrefix.actorTimeout"
  final val AKKA_NUMBER_OF_WORKERS = s"$akkaPrefix.numberOfWorkers"

  /*
   * OpenID Connect (= oidc)
   *********************************************************************************************/

  private val oidc = s"$CONFIG_PREFIX.openIdConnect"

  final val OIDC_STATE_TTL = s"$oidc.state.ttl"

  final val OIDC_TOKEN_TTL = s"$oidc.token.ttl"

  /*
   * Redis
   *********************************************************************************************/

  private val redis = s"$CONFIG_PREFIX.redis"

  final val REDIS_HOST = s"$redis.host"

  final val REDIS_PORT = s"$redis.port"

}
