package com.ubirch.auth.core.manager

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{RedisActor, RememberState}
import com.ubirch.auth.model.ProviderInfo
import com.ubirch.auth.oidcutil.AuthRequest

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinPool
import akka.util.Timeout

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-01-26
  */
object ProviderInfoManager {

  implicit val system = ActorSystem()
//  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  // TODO extract anything performance related to config
  private val redisActor = system.actorOf(new RoundRobinPool(3).props(Props[RedisActor]), ActorNames.REDIS)

  def providerInfoList(): Seq[ProviderInfo] = {

    Config.oidcProviders map { provider =>

      val (redirectUrl, state) = AuthRequest.redirectUrl(provider)
      redisActor ! RememberState(provider, state.toString)

      ProviderInfo(
        id = Config.oidcProviderId(provider),
        name = Config.oidcProviderName(provider),
        redirectUrl = redirectUrl
      )

    }

  }

}
