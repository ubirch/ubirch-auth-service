package com.ubirch.util.redis

import com.ubirch.util.redis.config.RedisConfig

import akka.actor.ActorSystem
import redis.RedisClient

/**
  * author: cvandrei
  * since: 2017-03-15
  */
object RedisClientUtil {

  // TODO scaladoc
  def newInstance(configPrefix: String)(implicit system: ActorSystem): RedisClient = {

    val hostPort = RedisConfig.redisHostPort(configPrefix)
    RedisClient(host = hostPort._1, port = hostPort._2)

  }

}
