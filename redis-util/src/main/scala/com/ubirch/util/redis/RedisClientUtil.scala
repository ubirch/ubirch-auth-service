package com.ubirch.util.redis

import com.ubirch.util.redis.config.RedisConfig

import akka.actor.ActorSystem
import redis.RedisClient

/**
  * author: cvandrei
  * since: 2017-03-15
  */
object RedisClientUtil {

  /**
    * Gives us an open Redis connection based on the configured host, port and password.
    *
    * @param configPrefix prefix under which redis config keys will be looked for
    * @param system       Akka's Actor System since it's required by the Redis client
    * @return an open Redis connection
    */
  def newInstance(configPrefix: String)(implicit system: ActorSystem): RedisClient = {

    val hostPort = RedisConfig.hostAndPort(configPrefix)
    val password = RedisConfig.password(configPrefix)

    RedisClient(
      host = hostPort._1,
      port = hostPort._2,
      password = password
    )

  }

}
