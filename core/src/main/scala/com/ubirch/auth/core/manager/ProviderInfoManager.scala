package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{ContextProviderIds, GetContextProvider, GetProviderBaseConfig, IsContextActive, OidcConfigActor, RememberState, StateAndCodeActor}
import com.ubirch.auth.model.ProviderInfo
import com.ubirch.auth.model.db.{ContextProviderConfig, OidcProviderConfig}
import com.ubirch.auth.oidcutil.AuthRequest
import com.ubirch.util.futures.FutureUtil

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
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
  private val oidcConfigActor = system.actorOf(new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props[OidcConfigActor]), ActorNames.OIDC_CONFIG)

  def providerInfoList(context: String): Future[Seq[ProviderInfo]] = {

    (oidcConfigActor ? IsContextActive(context)).mapTo[Boolean].flatMap {

      case true =>

        (oidcConfigActor ? ContextProviderIds(context)).mapTo[Seq[String]].flatMap { providerIdList =>

          val futureProviders: Seq[Future[ProviderInfo]] = providerIdList map { provider =>

            for {
              providerConf <- (oidcConfigActor ? GetProviderBaseConfig(provider)).mapTo[OidcProviderConfig]
              contextProviderConf <- (oidcConfigActor ? GetContextProvider(context, provider)).mapTo[ContextProviderConfig]
            } yield {

              val (redirectUrl, state) = AuthRequest.redirectUrl(contextProviderConf, providerConf)
              stateAndCodeActor ! RememberState(provider, state.toString)

              ProviderInfo(
                context = context,
                providerId = providerConf.id,
                name = providerConf.name,
                redirectUrl = redirectUrl
              )

            }

          }
          FutureUtil.unfoldInnerFutures(futureProviders)

        }

      case false =>

        logger.error(s"context not active: $context")
        Future(Seq.empty)

    }

  }

}
