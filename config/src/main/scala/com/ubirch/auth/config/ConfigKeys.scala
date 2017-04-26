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
   * MongoDB
   *********************************************************************************************/

  final val MONGO_PREFIX = "ubirch.mongo"

  /*
   * Test User
   *********************************************************************************************/

  private val testUser = s"$CONFIG_PREFIX.testUser"

  final val TEST_USER_TOKEN = s"$testUser.token"

  final val TEST_PROVIDER_ID = s"$testUser.providerId"

  final val TEST_USER_ID = s"$testUser.userId"

  final val TEST_USER_CONTEXT = s"$testUser.context"

  final val TEST_USER_NAME = s"$testUser.name"

  final val TEST_USER_LOCALE = s"$testUser.locale"

}
