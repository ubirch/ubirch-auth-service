package com.ubirch.auth.core.manager

import com.ubirch.util.model.DeepCheckResponse
import com.ubirch.util.redis.RedisClientUtil

import akka.actor.ActorSystem

import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2017-06-08
  */
object DeepCheckManager {

  /**
    * Check if we can run a simple query on the database.
    *
    * @param _system actor system required for Redis connection
    * @return deep check response with _status:OK_ if ok; otherwise with _status:NOK_
    */
  def connectivityCheck()(implicit _system: ActorSystem): Future[DeepCheckResponse] = {

    // TODO run deepCheck of user-service, too
    RedisClientUtil.connectivityCheck("auth-service")

  }

}
