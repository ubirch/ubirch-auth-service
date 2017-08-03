package com.ubirch.auth.util.db.config

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.model.db.ContextProviderConfig
import com.ubirch.auth.model.db.redis.RedisKeys
import com.ubirch.auth.util.db.config.defaults.OidcContextProvider
import com.ubirch.util.futures.FutureUtil
import com.ubirch.util.json.MyJsonProtocol

import org.json4s.native.Serialization.write

import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2017-03-14
  */
object OidcContextProviderUtil extends StrictLogging
  with MyJsonProtocol {

  /**
    * Inits OpenID Connect provider contexts.
    *
    * @param activateContexts true if contexts are created as active
    * @param sleepAfter       how long to sleep afterwards in milliseconds to account for db delays
    * @param redis            redis connection
    * @return created context configs
    */
  def initContexts(activateContexts: Boolean = true,
                   sleepAfter: Long = 500
                  )
                  (implicit redis: RedisClient): Future[Map[String, Seq[ContextProviderConfig]]] = {

    logger.info("====== create: contexts")
    val contextsStored = OidcContextProvider.contextProviderList map { ctxProviderConf =>

      storeContextProvider(ctxProviderConf, activateContexts) map { stored =>

        if (stored) {
          logger.info(s"created context related conf: context=${ctxProviderConf.context}, appId=${ctxProviderConf.appId}, provider=${ctxProviderConf.provider}")
        } else {
          logger.info(s"did not create context related conf (most likely it already exists): context=${ctxProviderConf.context}, appId=${ctxProviderConf.appId}, provider=${ctxProviderConf.provider}")
        }
        ctxProviderConf

      }

    }

    Thread.sleep(sleepAfter)

    FutureUtil.unfoldInnerFutures(contextsStored).map { seq =>
      seq.groupBy(_.context)
    }

  }

  /**
    * Create a singe provider context.
    *
    * @param ctxProviderConf provider context config to create
    * @param activateContext true if provider context is created as active
    * @param redis           redis connection
    * @return true if created successfully
    */
  def storeContextProvider(ctxProviderConf: ContextProviderConfig,
                           activateContext: Boolean = true
                          )
                          (implicit redis: RedisClient): Future[Boolean] = {

    val context = ctxProviderConf.context
    val key = RedisKeys.oidcContextProviderKey(
      context = context,
      appId = ctxProviderConf.appId,
      provider = ctxProviderConf.provider
    )

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

  /**
    * Disable a provider context.
    *
    * @param context    context to disable
    * @param sleepAfter how long to sleep afterwards in milliseconds to account for db delays
    * @param redis      redis connection
    * @return true if created successfully
    */
  def disableContext(context: String, sleepAfter: Long = 100)
                    (implicit redis: RedisClient): Future[Boolean] = {

    val result = redis.srem(RedisKeys.OIDC_CONTEXT_LIST, context) map (_ > 0)

    Thread.sleep(sleepAfter)
    result

  }

}
