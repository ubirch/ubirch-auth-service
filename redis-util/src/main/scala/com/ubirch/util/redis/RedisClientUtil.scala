package com.ubirch.util.redis

import akka.actor.ActorSystem
import redis.RedisClient

/**
  * author: cvandrei
  * since: 2017-03-15
  */
object RedisClientUtil {

  // TODO scaladoc
  def newInstance(configPrefix: String)(implicit system: ActorSystem): RedisClient = {

    // TODO read localhost:port and set
    RedisClient()

  }

}
