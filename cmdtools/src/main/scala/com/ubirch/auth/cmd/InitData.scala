package com.ubirch.auth.cmd

import java.net.URI

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.{ContextProviderConfig, OidcProviderConfig, OidcProviderEndpoints, RedisKeys}
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
  initProviders()
  initContexts()
  akkaSystem.terminate()

  private def initProviders(): Unit = {
    logger.info("=== init (clean up and create): providers")
    cleanupProviders()
    createProviders()
  }

  private def cleanupProviders(): Unit = {

    val pattern = s"${RedisKeys.OIDC_PROVIDER_PREFIX}.*"
    redis.keys(pattern) map { keyList =>

      keyList foreach { key =>
        logger.info(s"cleanup(): key=$key")
        redis.del(key)
      }

    }

    Thread.sleep(defaultSleep)

  }

  private def createProviders(): Unit = {

    providerBaseConfigs foreach { providerConf =>

      storeProvider(providerConf) map { stored =>

        if (stored) {
          logger.info(s"created provider base conf: provider=${providerConf.id}")
        } else {
          logger.error(s"failed to create provider base conf: provider=${providerConf.id}")
        }

      }

    }

    Thread.sleep(defaultSleep)

  }

  private def storeProvider(conf: OidcProviderConfig): Future[Boolean] = {

    val id = conf.id
    val json = write(conf)
    for {

      conf <- redis.set(RedisKeys.providerKey(id), json)
      activeList <- redis.lpush[String](RedisKeys.OIDC_PROVIDER_LIST, id)

    } yield {
      conf && activeList > 0
    }

  }

  private def initContexts(): Unit = {
    logger.info("=== init (clean up and create): contexts")
    cleanupContexts()
    createContexts()
  }

  private def cleanupContexts(): Unit = {

    val pattern = s"${RedisKeys.OIDC_CONTEXT_PREFIX}.*"
    redis.keys(pattern) map { keyList =>

      keyList foreach { key =>
        logger.info(s"cleanup(): key=$key")
        redis.del(key)
      }

    }

    Thread.sleep(defaultSleep)

  }

  private def createContexts(): Unit = {

    contextProviderList foreach { ctxProviderConf =>

      storeContextProvider(ctxProviderConf) map { stored =>

        if (stored) {
          logger.info(s"created context provider conf: context=${ctxProviderConf.context}, provider=${ctxProviderConf.provider}")
        } else {
          logger.error(s"failed to create provider base conf: context=${ctxProviderConf.context}, provider=${ctxProviderConf.provider}")
        }

      }

    }

    Thread.sleep(defaultSleep)

  }

  private def storeContextProvider(ctxProviderConf: ContextProviderConfig): Future[Boolean] = {

    val context = ctxProviderConf.context
    val key = RedisKeys.oidcContextProviderKey(context = context, provider = ctxProviderConf.provider)
    val json = write(ctxProviderConf)
    for {

      conf <- redis.set(key, json)
      activeList <- redis.sadd[String](RedisKeys.OIDC_CONTEXT_LIST, context)

    } yield {
      conf && activeList > 0
    }

  }

  private lazy val providerBaseConfigs: Seq[OidcProviderConfig] = Seq(google, yahoo)

  private lazy val providerIdGoogle = "google"
  private lazy val providerIdYahoo = "yahoo"

  private lazy val google = OidcProviderConfig(
    id = providerIdGoogle,
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

  private lazy val yahoo = OidcProviderConfig(
    id = providerIdYahoo,
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

  private lazy val ctxTrackleDev = "trackle-dev"
  private lazy val ctxUbirchAdminUIDev = "ubirch-admin-ui-dev"
  private lazy val ctxTrackleAdminUIDev = "trackle-admin-ui-dev"

  private lazy val contextProviderList: Seq[ContextProviderConfig] = Seq(
    trackleDevGoogle, trackleDevYahoo,
    ubirchAdminUIDevYahoo,
    trackleAdminUIDevYahoo
  )

  private lazy val trackleDevGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ctxTrackleDev,
    provider = providerIdGoogle,
    clientId = "370115332091-kqf5hu698s4sodrvv03ka3bule530rp5.apps.googleusercontent.com",
    clientSecret = "M86oj4LxV-CcEDd3ougKSbsV",
    callbackUrl = new URI("https://localhost:10000/oidc-callback-google")
  )

  private lazy val trackleDevYahoo: ContextProviderConfig = ContextProviderConfig(
    context = ctxTrackleDev,
    provider = providerIdYahoo,
    clientId = "dj0yJmk9eWdKUGRJM01KclhqJmQ9WVdrOVlrMUVSRTF3TlRBbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1mMA--",
    clientSecret = "069dd28f2144ed043dcb70e27f99e424369c3040",
    callbackUrl = new URI("https://example.com/oidc-callback-yahoo")
  )

  private lazy val ubirchAdminUIDevYahoo: ContextProviderConfig = ContextProviderConfig(
    context = ctxUbirchAdminUIDev,
    provider = providerIdGoogle,
    clientId = "370115332091-q7dccmh0leq20rqgs2550vp0u67pj42p.apps.googleusercontent.com",
    clientSecret = "BFmveucyPZ9Ijt31UvYocrj4",
    callbackUrl = new URI("http://localhost:9000/auth?providerId=google")
  )

  private lazy val trackleAdminUIDevYahoo: ContextProviderConfig = ContextProviderConfig(
    context = ctxTrackleAdminUIDev,
    provider = providerIdGoogle,
    clientId = "370115332091-dqhiaemv68bjvtnp84beg26plrpkmc8t.apps.googleusercontent.com",
    clientSecret = "n3fPuja818436VmggJZSht6-",
    callbackUrl = new URI("http://localhost:9100/auth?providerId=google")
  )

}
