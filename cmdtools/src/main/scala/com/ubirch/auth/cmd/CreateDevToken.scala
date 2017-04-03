package com.ubirch.auth.cmd

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.testToolsExt.user.TestUserUtil

/**
  * author: cvandrei
  * since: 2017-03-28
  */
object CreateDevToken extends App
  with StrictLogging {

  val token = TestUserUtil.persistTestUserToken()
  logger.info("====== CREATE DEV TOKEN")
  logger.info("=== to remember another tuple of [token, userId, context] please modify the config (ubirchAuthService.testUser.*) and run this tool again")
  logger.info(s"=== token: $token")

}
