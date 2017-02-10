package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.util.OidcUtil
import com.ubirch.auth.model.AfterLogin

import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object TokenManager extends App
  with StrictLogging {

  def verifyCode(afterLogin: AfterLogin): Future[VerifyCodeResult] = {

    stateExists(afterLogin) map {

      case true =>

        // TODO verify "afterLogin.code" w/ OpenID Connect provider

        val provider = afterLogin.providerId
        // TODO replace w/ correct token
        val token = s"$provider-${afterLogin.code}-${afterLogin.state}"

        val userId = s"$provider-${Random.nextInt}" // TODO replace w/ correct userId
        rememberToken(afterLogin.providerId, token, userId)

        VerifyCodeResult(token = Some(token))

      case false =>

        logger.error(s"invalid state: $afterLogin")
        VerifyCodeResult(errorType = Some(VerifyCodeError.UnknownState))

    }

  }

  private def stateExists(afterLogin: AfterLogin): Future[Boolean] = {

    implicit val akkaSystem = akka.actor.ActorSystem()
    val redis = RedisClient()

    val provider = afterLogin.providerId
    val state = afterLogin.state
    val key = OidcUtil.stateToHashedKey(provider, state)

    redis.exists(key)

  }

  private def rememberToken(provider: String, token: String, userId: String): Unit = {

    implicit val akkaSystem = akka.actor.ActorSystem()
    val redis = RedisClient()

    val key = OidcUtil.tokenToHashedKey(provider, token)
    val ttl = Some(Config.oidcTokenTtl())

    redis.set(key, userId, ttl) onComplete {

      case Success(result) =>
        result match {
          case true => logger.debug(s"remembered token for provider=$provider (ttl: ${ttl.get} seconds)")
          case false => logger.error(s"failed to remember token for provider=$provider (ttl: ${ttl.get} seconds)")
        }

      case Failure(e) => logger.error(s"failed to remember token for provider=$provider (ttl: ${ttl.get} seconds)", e)

    }

  }

}

case class VerifyCodeResult(token: Option[String] = None,
                            errorType: Option[VerifyCodeError.Value] = None
                           )

object VerifyCodeError extends Enumeration {

  val UnknownState, LoginFailed = Value

}
