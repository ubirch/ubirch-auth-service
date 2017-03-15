package com.ubirch.auth.cmd

import com.ubirch.auth.model.db.redis.RedisKeys
import com.ubirch.auth.testTools.db.redis.RedisCleanup

/**
  * author: cvandrei
  * since: 2017-03-15
  */
object RedisDelete extends App
  with RedisCleanup {

  deleteAll(prefix = RedisKeys.OIDC)

}
