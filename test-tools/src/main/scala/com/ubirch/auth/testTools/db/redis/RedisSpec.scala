package com.ubirch.auth.testTools.db.redis

import com.ubirch.auth.config.ConfigKeys
import com.ubirch.util.redis.test.RedisCleanup

import org.scalatest.{AsyncFeatureSpec, BeforeAndAfterEach, Matchers}

/**
  * author: cvandrei
  * since: 2017-03-14
  */
trait RedisSpec extends AsyncFeatureSpec
  with Matchers
  with BeforeAndAfterEach
  with RedisCleanup {

  override protected def beforeEach(): Unit = {
    deleteAll(configPrefix = ConfigKeys.CONFIG_PREFIX)
    Thread.sleep(100)
  }

}
