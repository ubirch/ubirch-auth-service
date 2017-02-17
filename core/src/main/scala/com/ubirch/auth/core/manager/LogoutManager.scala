package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{DeleteToken, StateAndCodeActor, VerifyTokenExists}
import com.ubirch.auth.model.Logout

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
  * since: 2017-02-01
  */
object LogoutManager extends StrictLogging {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  private val stateAndCodeActor = system.actorOf(new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props[StateAndCodeActor]), ActorNames.REDIS)

  def logout(logout: Logout): Future[Boolean] = {

    val provider = logout.providerId
    val token = logout.token

    (stateAndCodeActor ? VerifyTokenExists(provider = provider, token = token)).mapTo[Boolean].flatMap {

      case true =>
        (stateAndCodeActor ? DeleteToken(provider = provider, token = token)).mapTo[Boolean].map { tokenDeleted =>
          logger.debug(s"logout successful: provider=$provider, token=$token")
          logger.info(s"logout successful: provider=$provider")
          tokenDeleted
        }

      case false =>
        logger.debug(s"logout not needed since token already expired: provider=$provider, token=$token")
        logger.info(s"logout not needed since token already expired: provider=$provider")
        Future(true)

    }

  }

}
