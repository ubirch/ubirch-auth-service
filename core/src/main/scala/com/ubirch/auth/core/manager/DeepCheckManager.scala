package com.ubirch.auth.core.manager

import com.ubirch.user.client.rest.UserServiceClientRest
import com.ubirch.util.deepCheck.model.DeepCheckResponse
import com.ubirch.util.deepCheck.util.DeepCheckResponseUtil
import com.ubirch.util.redis.RedisClientUtil

import akka.actor.ActorSystem
import akka.http.scaladsl.HttpExt
import akka.stream.Materializer

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
  def connectivityCheck()
                       (implicit _system: ActorSystem, httpClient: HttpExt, materializer: Materializer): Future[DeepCheckResponse] = {

    implicit val ec = _system.dispatcher
    for {

      redis <- RedisClientUtil.connectivityCheck("auth-service")
      userDeepCheck <- UserServiceClientRest.deepCheck() // TODO http timeouts are not yet handled

    } yield {

      DeepCheckResponseUtil.merge(
        Seq(
          redis,
          userDeepCheck
        )
      )

    }

  }

}
