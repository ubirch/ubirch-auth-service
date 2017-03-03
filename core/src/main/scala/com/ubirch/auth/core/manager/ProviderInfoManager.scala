package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.{Config, OidcProviderConfig}
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{GetProviderBaseConfig, OidcConfigActor, RememberState, StateAndCodeActor}
import com.ubirch.auth.model.ProviderInfo
import com.ubirch.auth.oidcutil.AuthRequest
import com.ubirch.util.futures.FutureUtil

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinPool
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * author: cvandrei
  * since: 2017-01-26
  */
object ProviderInfoManager extends StrictLogging {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  private val stateAndCodeActor = system.actorOf(new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props[StateAndCodeActor]), ActorNames.REDIS)
  private val oidcConfigActor = system.actorOf(new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props[OidcConfigActor]), ActorNames.OIDC_CONFIG)

  def providerInfoList(context: String): Future[Seq[ProviderInfo]] = {

    if (Config.oidcActiveContextList contains context) {

      val futureProviders: Seq[Future[ProviderInfo]] = Config.oidcContextProvidersList(context) map { provider =>

        val (redirectUrl, state) = AuthRequest.redirectUrl(context, provider)
        stateAndCodeActor ! RememberState(provider, state.toString)

        (oidcConfigActor ? GetProviderBaseConfig(provider)).mapTo[OidcProviderConfig].map { providerConf =>
          ProviderInfo(
            context = context,
            providerId = providerConf.id,
            name = providerConf.name,
            redirectUrl = redirectUrl
          )
        }

      }
      FutureUtil.unfoldInnerFutures(futureProviders.toList)

    } else {
      logger.error(s"context not active: $context")
      Future(Seq.empty)
    }

  }

}
