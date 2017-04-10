package com.ubirch.auth.cmd

import com.ubirch.util.redis.RedisClientUtil

import akka.actor.{ActorSystem, Terminated}
import akka.util.Timeout
import redis.RedisClient

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-04-10
  */
trait CmdBase {

  implicit protected val system = ActorSystem()
  implicit protected val timeout = Timeout(15 seconds)

  implicit protected val redis: RedisClient = RedisClientUtil.getRedisClient()

  protected def closeResources(): Terminated = {

    redis.stop()
    Await.result(system.terminate(), 5 seconds)

  }

}
