package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{DeleteState, RedisActor, RememberToken}
import com.ubirch.auth.core.util.OidcUtil
import com.ubirch.auth.model.AfterLogin

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinPool
import akka.util.Timeout
import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object TokenManager extends StrictLogging {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  // TODO extract anything performance related to config
  private val redisActor = system.actorOf(new RoundRobinPool(3).props(Props[RedisActor]), ActorNames.REDIS)

  def verifyCode(afterLogin: AfterLogin): Future[VerifyCodeResult] = {

    stateExists(afterLogin) map {

      case true =>

        // TODO verify "afterLogin.code" w/ OpenID Connect provider

        val provider = afterLogin.providerId
        // TODO replace w/ correct token
        val token = s"$provider-${afterLogin.code}-${afterLogin.state}"

        val userId = s"$provider-${Random.nextInt}" // TODO replace w/ correct userId
        redisActor ! RememberToken(afterLogin.providerId, token, userId)
        redisActor ! DeleteState(afterLogin.providerId, afterLogin.state)

        VerifyCodeResult(token = Some(token))

      case false =>

        logger.error(s"invalid state: $afterLogin")
        VerifyCodeResult(errorType = Some(VerifyCodeError.UnknownState))

    }

  }

  private def stateExists(afterLogin: AfterLogin): Future[Boolean] = {

    val redis = RedisClient()

    val provider = afterLogin.providerId
    val state = afterLogin.state
    val key = OidcUtil.stateToHashedKey(provider, state)

    redis.exists(key)

  }

}

case class VerifyCodeResult(token: Option[String] = None,
                            errorType: Option[VerifyCodeError.Value] = None
                           )

object VerifyCodeError extends Enumeration {

  val UnknownState, LoginFailed = Value

}
