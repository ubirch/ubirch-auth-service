package com.ubirch.auth.testToolsExt.user

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{RememberToken, StateAndCodeActor}

import akka.actor.ActorSystem
import akka.util.Timeout

import scala.concurrent.duration._
import scala.language.postfixOps
/**
  * author: cvandrei
  * since: 2017-03-28
  */
object TestUserUtil extends StrictLogging {

  implicit val system: ActorSystem = ActorSystem()
  implicit val timeout: Timeout = Timeout(15 seconds)

  private val stateAndCodeActor = system.actorOf(StateAndCodeActor.props(), ActorNames.OIDC_CONFIG)

  def persistTestUserToken(token: String = Config.testUserToken(),
                           providerId: String = Config.testProviderId(),
                           userId: String = Config.testUserId(),
                           context: String = Config.testUserContext(),
                           userName: String = Config.testUserName(),
                           userLocale: String = Config.testUserLocale(),
                           sleepAfter: Long = 500
                          ): String = {

    logger.info("====== create: test user token")

    stateAndCodeActor ! RememberToken(
      context = context,
      token = token,
      providerId = providerId,
      userId = userId,
      userName = userName,
      locale = userLocale
    )

    Thread.sleep(sleepAfter)
    system.terminate()

    token

  }

}
