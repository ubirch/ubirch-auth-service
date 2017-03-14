package com.ubirch.auth.testTools.db.redis

import org.scalatest.{BeforeAndAfterEach, FeatureSpec, Matchers}

/**
  * author: cvandrei
  * since: 2017-03-14
  */
trait RedisSpec extends FeatureSpec
  with Matchers
  with BeforeAndAfterEach
  with RedisCleanup {

  override protected def beforeEach(): Unit = {
    deleteAll()
    Thread.sleep(100)
  }

}
