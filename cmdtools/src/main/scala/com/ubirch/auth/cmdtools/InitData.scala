package com.ubirch.auth.cmdtools

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.{OidcProviderConfig, OidcProviderEndpoints, RedisKeys}
import com.ubirch.util.json.MyJsonProtocol

import org.json4s.native.Serialization.write

import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2017-03-03
  */
object InitData extends App
  with MyJsonProtocol
  with StrictLogging {

  implicit val akkaSystem = akka.actor.ActorSystem()
  val defaultSleep = 1000

  val redis = RedisClient()
  cleanup()
  create()
  akkaSystem.terminate()

  private def cleanup(): Unit = {

    val pattern = s"${RedisKeys.OIDC_PROVIDER_PREFIX}.*"
    redis.keys(pattern) map { keyList =>

      keyList foreach { key =>
        logger.info(s"cleanup(): key=$key")
        redis.del(key)
      }

    }

    Thread.sleep(defaultSleep)

  }

  private def create(): Unit = {

    providerBaseConfigs() foreach { providerConf =>

      store(providerConf) map { stored =>

        if (stored) {
          logger.info(s"created provider base conf: provider=${providerConf.id}")
        } else {
          logger.error(s"failed to create provider base conf: provider=${providerConf.id}")
        }

      }

    }

    Thread.sleep(defaultSleep)

  }

  private def store(conf: OidcProviderConfig): Future[Boolean] = {

    val id = conf.id
    for {

      conf <- redis.set(RedisKeys.providerKey(id), write(conf))
      activeList <- redis.lpush[String](RedisKeys.OIDC_PROVIDER_LIST, id)

    } yield {
      conf && activeList > 0
    }

  }

  private val google = OidcProviderConfig(
    id = "google",
    name = "Google",
    scope = "openid",
    endpointConfig = "https://accounts.google.com/.well-known/openid-configuration",
    tokenSigningAlgorithms = Seq("RS256"),
    endpoints = OidcProviderEndpoints(
      authorization = "https://accounts.google.com/o/oauth2/v2/auth",
      token = "https://www.googleapis.com/oauth2/v4/token",
      jwks = "https://www.googleapis.com/oauth2/v3/certs"
    )
  )

  private val yahoo = OidcProviderConfig(
    id = "yahoo",
    name = "Yahoo",
    scope = "openid",
    endpointConfig = "https://login.yahoo.com/.well-known/openid-configuration",
    tokenSigningAlgorithms = Seq("RS256", "ES256"),
    endpoints = OidcProviderEndpoints(
      authorization = "https://api.login.yahoo.com/oauth2/request_auth",
      token = "https://api.login.yahoo.com/oauth2/get_token",
      jwks = "https://login.yahoo.com/openid/v1/certs"
    )
  )

  private def providerBaseConfigs(): Seq[OidcProviderConfig] = Seq(google, yahoo)

}
