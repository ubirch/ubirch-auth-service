package com.ubirch.auth.testTools.db.redis

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
    deleteAll()
    Thread.sleep(100)
  }

}
