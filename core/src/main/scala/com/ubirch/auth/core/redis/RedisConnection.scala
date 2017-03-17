package com.ubirch.auth.core.redis

import com.ubirch.auth.config.ConfigKeys
import com.ubirch.util.redis.RedisClientUtil

import akka.actor.ActorSystem
import redis.RedisClient

/**
  * author: cvandrei
  * since: 2017-03-17
  */
object RedisConnection {

  def client(system: ActorSystem): RedisClient = {
    RedisClientUtil.newInstance(ConfigKeys.CONFIG_PREFIX)(system)
  }

}
