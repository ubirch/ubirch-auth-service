package com.ubirch.util.redis.config

import com.ubirch.util.config.ConfigBase

/**
  * author: cvandrei
  * since: 2017-03-15
  */
object RedisConfig extends ConfigBase {

  def redisHostPort(configPrefix: String): (String, Int) = {

    val finalPrefix = getFinalPrefix(configPrefix)

    val host = config.getString(s"$finalPrefix${RedisConfigKeys.REDIS_HOST}")
    val port = config.getInt(s"$finalPrefix${RedisConfigKeys.REDIS_PORT}")

    (host, port)

  }

  private def getFinalPrefix(prefix: String): String = {

    if (prefix == "") {
      ""
    } else {
      s"$prefix."
    }

  }

}
