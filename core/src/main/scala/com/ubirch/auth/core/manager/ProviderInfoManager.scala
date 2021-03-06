package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{ContextProviderIds, GetContextProvider, GetProviderBaseConfig, IsContextActive, OidcConfigActor, RememberState, StateAndCodeActor}
import com.ubirch.auth.model.ProviderInfo
import com.ubirch.auth.model.db.{ContextProviderConfig, OidcProviderConfig}
import com.ubirch.auth.oidcutil.AuthRequest

import akka.actor.ActorSystem
import akka.pattern.ask
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

  private val stateAndCodeActor = system.actorOf(StateAndCodeActor.props(), ActorNames.REDIS)
  private val oidcConfigActor = system.actorOf(OidcConfigActor.props(), ActorNames.OIDC_CONFIG)

  def providerInfoList(context: String, appId: String): Future[Seq[ProviderInfo]] = {

    logger.debug(s"providerInfoList(): context=$context, appId=$appId")
    (oidcConfigActor ? IsContextActive(context)).mapTo[Boolean].flatMap {

      case true =>

        (oidcConfigActor ? ContextProviderIds(context, appId)).mapTo[Seq[String]].flatMap { providerIdList =>

          logger.debug(s"providerInfoList(): providerIdList=$providerIdList")
          val futureProviders: Seq[Future[ProviderInfo]] = providerIdList map { provider =>

            for {
              providerConf <- (oidcConfigActor ? GetProviderBaseConfig(provider)).mapTo[OidcProviderConfig]
              contextProviderConf <- (oidcConfigActor ? GetContextProvider(context, appId, provider)).mapTo[ContextProviderConfig]
            } yield {

              val (redirectUrl, state) = AuthRequest.redirectUrl(contextProviderConf, providerConf)
              stateAndCodeActor ! RememberState(provider, state.toString)

              ProviderInfo(
                context = context,
                appId = appId,
                providerId = providerConf.id,
                name = providerConf.name,
                redirectUrl = redirectUrl
              )

            }

          }
          Future.sequence(futureProviders)

        }

      case false =>

        logger.error(s"context not active: $context")
        Future(Seq.empty)

    }

  }

}
