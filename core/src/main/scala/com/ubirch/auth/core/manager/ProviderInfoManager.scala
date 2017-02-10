package com.ubirch.auth.core.manager

import com.nimbusds.oauth2.sdk.id.State
import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.util.OidcUtil
import com.ubirch.auth.model.ProviderInfo
import com.ubirch.auth.oidcutil.AuthRequest

import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * author: cvandrei
  * since: 2017-01-26
  */
object ProviderInfoManager extends App
  with StrictLogging {

  def providerInfoList(): Seq[ProviderInfo] = {

    Config.oidcProviders map { provider =>

      val (redirectUrl, state) = AuthRequest.redirectUrl(provider)
      rememberState(provider, state)

      ProviderInfo(
        id = Config.oidcProviderId(provider),
        name = Config.oidcProviderName(provider),
        redirectUrl = redirectUrl
      )

    }

  }

  private def rememberState(provider: String, state: State): Unit = {

    implicit val akkaSystem = akka.actor.ActorSystem()
    val redis = RedisClient()

    val key = OidcUtil.stateToHashedKey(provider, state.toString)
    val ttl = Some(Config.oidcStateTtl())

    redis.set(key, "1", ttl) onComplete {

      case Success(result) =>
        result match {
          case true => logger.debug(s"remembered state: $state (ttl: ${ttl.get} seconds)")
          case false => logger.error(s"failed to remember state: $state (ttl: ${ttl.get} seconds)")
        }

      case Failure(e) => logger.error(s"failed to remember state: $state (ttl: ${ttl.get} seconds)", e)

    }

  }

}
