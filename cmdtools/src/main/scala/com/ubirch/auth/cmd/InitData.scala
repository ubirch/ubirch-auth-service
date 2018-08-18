package com.ubirch.auth.cmd

import com.ubirch.auth.util.db.config.{OidcContextProviderUtil, OidcProviderUtil}
import com.ubirch.util.redis.test.RedisCleanup

import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-03-03
  */
object InitData extends App
  with CmdBase
  with RedisCleanup {

  // TODO migrate to encapsulate all executable logic within a method `run(): Unit`
  OidcProviderUtil.initProviders()
  OidcContextProviderUtil.initContexts()

  closeResources()

}
