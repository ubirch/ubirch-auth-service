package com.ubirch.auth.testTools.db.config

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.ConfigKeys
import com.ubirch.auth.model.db.OidcProviderConfig
import com.ubirch.auth.model.db.redis.RedisKeys
import com.ubirch.auth.testTools.db.config.defaults.OidcProviders
import com.ubirch.util.futures.FutureUtil
import com.ubirch.util.json.MyJsonProtocol
import com.ubirch.util.redis.RedisClientUtil

import org.json4s.native.Serialization.write

import akka.actor.ActorSystem
import akka.util.Timeout
import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-03-14
  */
object OidcProviderUtil extends StrictLogging
  with MyJsonProtocol {

  // TODO scaladoc
  final def initProviders(
                           activateProviders: Boolean = true,
                           sleepAfter: Long = 500
                         ): Future[Seq[OidcProviderConfig]] = {

    implicit val system = ActorSystem()
    implicit val timeout = Timeout(15 seconds)
    val redis = RedisClientUtil.newInstance(ConfigKeys.CONFIG_PREFIX)(system)

    logger.info("====== create: providers")
    val providersStored = OidcProviders.providers map { providerConf =>

      storeProvider(providerConf, activateProviders, redis) map { stored =>

        if (stored) {
          logger.info(s"created provider base conf: provider=${providerConf.id}")
        } else {
          logger.error(s"failed to create provider base conf: provider=${providerConf.id}")
        }
        providerConf

      }

    }

    Thread.sleep(sleepAfter)
    system.terminate()

    FutureUtil.unfoldInnerFutures(providersStored)

  }

  // TODO scaladoc
  def storeProvider(
                     provider: OidcProviderConfig,
                     activateProvider: Boolean = true,
                     redis: RedisClient
                   ): Future[Boolean] = {

    val id = provider.id
    val json = write(provider)

    redis.set(RedisKeys.providerKey(id), json) flatMap { stored =>

      if (activateProvider) {

        redis.lpush[String](RedisKeys.OIDC_PROVIDER_LIST, id) map { activated =>
          stored && activated > 0
        }

      } else {
        Future(stored)
      }

    }

  }

}
