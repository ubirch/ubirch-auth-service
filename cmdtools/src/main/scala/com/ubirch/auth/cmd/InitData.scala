package com.ubirch.auth.cmd

import com.ubirch.auth.model.db.redis.RedisKeys
import com.ubirch.auth.testTools.db.config.{OidcContextProviderUtil, OidcProviderUtil}
import com.ubirch.auth.testTools.db.redis.RedisCleanup

/**
  * author: cvandrei
  * since: 2017-03-03
  */
object InitData extends App
  with RedisCleanup {

  deleteAll(prefix = RedisKeys.OIDC)
  OidcProviderUtil.initProviders()
  OidcContextProviderUtil.initContexts()

}
