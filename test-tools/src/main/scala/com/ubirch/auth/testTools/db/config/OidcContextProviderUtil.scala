package com.ubirch.auth.testTools.db.config

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.ConfigKeys
import com.ubirch.auth.model.db.ContextProviderConfig
import com.ubirch.auth.model.db.redis.RedisKeys
import com.ubirch.auth.testTools.db.config.defaults.OidcContextProvider
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
object OidcContextProviderUtil extends StrictLogging
  with MyJsonProtocol {

  // TODO scaladoc
  def initContexts(
                    activateContexts: Boolean = true,
                    sleepAfter: Long = 500
                  ): Future[Seq[ContextProviderConfig]] = {

    implicit val system = ActorSystem()
    implicit val timeout = Timeout(15 seconds)
    val redis = RedisClientUtil.newInstance(ConfigKeys.CONFIG_PREFIX)(system)

    logger.info("====== create: contexts")
    val contextsStored = OidcContextProvider.contextProviderList map { ctxProviderConf =>

      storeContextProvider(ctxProviderConf, activateContexts, redis) map { stored =>

        if (stored) {
          logger.info(s"created context provider conf: context=${ctxProviderConf.context}, provider=${ctxProviderConf.provider}")
        } else {
          logger.error(s"failed to create provider base conf: context=${ctxProviderConf.context}, provider=${ctxProviderConf.provider}")
        }
        ctxProviderConf

      }

    }

    Thread.sleep(sleepAfter)
    system.terminate()

    FutureUtil.unfoldInnerFutures(contextsStored)

  }

  // TODO scaladoc
  def storeContextProvider(
                            ctxProviderConf: ContextProviderConfig,
                            activateContext: Boolean = true,
                            redis: RedisClient
                          ): Future[Boolean] = {

    val context = ctxProviderConf.context
    val key = RedisKeys.oidcContextProviderKey(context = context, provider = ctxProviderConf.provider)
    val json = write(ctxProviderConf)

    redis.set(key, json) flatMap { stored =>

      if (activateContext) {

        redis.sadd[String](RedisKeys.OIDC_CONTEXT_LIST, context) map { activated =>
          stored && activated > 0
        }

      } else {
        Future(stored)
      }

    }

  }

}
