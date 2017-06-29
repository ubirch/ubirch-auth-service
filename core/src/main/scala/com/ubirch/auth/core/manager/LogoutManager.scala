package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{DeleteToken, StateAndCodeActor, VerifyTokenExists}

import akka.actor.ActorSystem
import akka.pattern.ask
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

  private val stateAndCodeActor = system.actorOf(StateAndCodeActor.props(), ActorNames.REDIS)

  def logout(token: String): Future[Boolean] = {

    (stateAndCodeActor ? VerifyTokenExists(token = token)).mapTo[Boolean].flatMap {

      case true =>
        (stateAndCodeActor ? DeleteToken(token = token)).mapTo[Boolean].map { tokenDeleted =>
          logger.debug(s"logout successful: token=$token")
          logger.info(s"logout successful")
          tokenDeleted
        }

      case false =>
        logger.debug(s"logout not needed since token already expired: token=$token")
        logger.info(s"logout not needed since token already expired")
        Future(true)

    }

  }

}
