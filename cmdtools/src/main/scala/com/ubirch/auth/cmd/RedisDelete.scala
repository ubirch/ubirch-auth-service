package com.ubirch.auth.cmd

import com.ubirch.auth.config.ConfigKeys
import com.ubirch.auth.model.db.redis.RedisKeys
import com.ubirch.util.redis.test.RedisCleanup

/**
  * author: cvandrei
  * since: 2017-03-15
  */
object RedisDelete extends App
  with RedisCleanup {

  deleteAll(redisPrefix = RedisKeys.OIDC, configPrefix = ConfigKeys.CONFIG_PREFIX)

}
