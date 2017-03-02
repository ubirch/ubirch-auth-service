package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{RememberState, StateAndCodeActor}
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
object ProviderInfoManager extends StrictLogging {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  private val stateAndCodeActor = system.actorOf(new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props[StateAndCodeActor]), ActorNames.REDIS)

  def providerInfoList(context: String): Seq[ProviderInfo] = {

    if (Config.oidcActiveContextList contains context) {

      Config.oidcContextProvidersList(context) map { provider =>

        val (redirectUrl, state) = AuthRequest.redirectUrl(context, provider)
        stateAndCodeActor ! RememberState(provider, state.toString)

        val providerConf = Config.oidcProviderConfig(provider)
        ProviderInfo(
          context = context,
          providerId = providerConf.id,
          name = providerConf.name,
          redirectUrl = redirectUrl
        )

      }

    } else {
      logger.error(s"context not active: $context")
      Seq.empty
    }

  }

}
