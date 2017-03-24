package com.ubirch.auth.cmd

import com.ubirch.auth.testTools.db.config.{OidcContextProviderUtil, OidcProviderUtil}
import com.ubirch.util.redis.test.RedisCleanup

/**
  * author: cvandrei
  * since: 2017-03-03
  */
object InitData extends App
  with RedisCleanup {

  OidcProviderUtil.initProviders()
  OidcContextProviderUtil.initContexts()

}
