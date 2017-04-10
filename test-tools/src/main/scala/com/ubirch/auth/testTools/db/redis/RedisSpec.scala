package com.ubirch.auth.testTools.db.redis

import com.ubirch.auth.config.ConfigKeys
import com.ubirch.util.redis.RedisClientUtil
import com.ubirch.util.redis.test.RedisCleanup

import org.scalatest.{AsyncFeatureSpec, BeforeAndAfterAll, BeforeAndAfterEach, Matchers}

import akka.actor.ActorSystem
import akka.util.Timeout
import redis.RedisClient

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-03-14
  */
trait RedisSpec extends AsyncFeatureSpec
  with Matchers
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with RedisCleanup {

  implicit protected val system = ActorSystem()
  implicit protected val timeout = Timeout(15 seconds)

  implicit protected val redis: RedisClient = RedisClientUtil.getRedisClient()

  override protected def beforeEach(): Unit = {
    deleteAll(configPrefix = ConfigKeys.CONFIG_PREFIX)
    Thread.sleep(100)
  }

  override protected def afterAll(): Unit = system.terminate()

}
