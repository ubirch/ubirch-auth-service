package com.ubirch.auth.core.actor

import com.ubirch.auth.model.db.redis.RedisKeys
import com.ubirch.auth.model.db.{ContextProviderConfig, OidcProviderConfig}
import com.ubirch.util.futures.FutureUtil
import com.ubirch.util.json.JsonFormats
import com.ubirch.util.redis.RedisClientUtil

import org.json4s.native.Serialization.read

import akka.actor.{Actor, ActorLogging, ActorSystem}
import redis.RedisClient

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  * author: cvandrei
  * since: 2017-03-03
  */
class OidcConfigActor extends Actor
  with ActorLogging {

  implicit private val formatter = JsonFormats.default

  implicit private val executionContext: ExecutionContextExecutor = context.dispatcher
  implicit private val akkaSystem: ActorSystem = context.system

  private val redis: RedisClient = RedisClientUtil.getRedisClient()

  override def receive: Receive = {

    case _: GetActiveProviderIds =>
      val sender = context.sender()
      activeProviderIds() map (sender ! _)

    case msg: GetProviderBaseConfig =>
      val sender = context.sender()
      providerBaseConfig(msg.providerId) map (sender ! _)

    case _: GetProviderBaseConfigs =>
      val sender = context.sender()
      providerBaseConfigs() map (sender ! _)

    case _: GetActiveContexts =>
      val sender = context.sender()
      activeContexts() map (sender ! _)

    case msg: IsContextActive =>
      val sender = context.sender()
      isContextActive(msg.context) map (sender ! _)

    case msg: ContextProviderIds =>
      val sender = context.sender()
      contextProviderIds(msg.context) map (sender ! _)

    case msg: GetContextProviders =>
      val sender = context.sender()
      contextProviders(msg.context) map (sender ! _)

    case msg: GetContextProvider =>
      val sender = context.sender()
      contextProvider(msg.context, msg.provider) map (sender ! _)

    case _ => log.error("unknown message")

  }

  private def activeProviderIds(): Future[Seq[String]] = {
    redis.lrange[String](RedisKeys.OIDC_PROVIDER_LIST, 0, -1)
  }

  private def providerBaseConfig(providerId: String): Future[OidcProviderConfig] = {

    val providerKey = RedisKeys.providerKey(providerId)
    log.debug(s"Redis key: providerKey=$providerKey")
    val redisResult = redis.get[String](providerKey)
    redisResult map {

      case None =>
        log.error(s"failed to load provider: $providerKey")
        throw new Exception(s"failed to load provider base config: providerKey=$providerKey")

      case Some(json) => read[OidcProviderConfig](json)

    }

  }

  private def providerBaseConfigs(): Future[Seq[OidcProviderConfig]] = {

    activeProviderIds() flatMap { providerIdList =>

      val configList: Seq[Future[OidcProviderConfig]] = providerIdList map providerBaseConfig
      FutureUtil.unfoldInnerFutures(configList)

    }

  }

  private def activeContexts(): Future[Seq[String]] = {
    redis.lrange[String](RedisKeys.OIDC_CONTEXT_LIST, 0, -1)
  }

  private def isContextActive(context: String): Future[Boolean] = {
    redis.sismember(RedisKeys.OIDC_CONTEXT_LIST, context)
  }

  private def contextProviderIds(context: String): Future[Seq[String]] = {

    val prefix = RedisKeys.oidcContextPrefix(context)
    val pattern = s"$prefix.*"

    redis.keys(pattern) flatMap { providerKeys =>

      val allProviderKeys = providerKeys map (_.replaceAll(prefix + ".", ""))

      activeProviderIds() map { activeProviders =>
        allProviderKeys filter(activeProviders.contains(_))
      }

    }

  }

  private def contextProviders(context: String): Future[Seq[ContextProviderConfig]] = {

    val pattern = s"${RedisKeys.oidcContextPrefix(context)}.*"
    redis.keys(pattern) flatMap { providerList =>
      FutureUtil.unfoldInnerFutures(
        providerList.map(contextProvider(context, _))
      )
    }

  }

  private def contextProvider(context: String, provider: String): Future[ContextProviderConfig] = {

    val key = RedisKeys.oidcContextProviderKey(context, provider)
    val redisResult = redis.get[String](key)
    redisResult map {

      case None =>
        log.error(s"failed to load context provider: context=$context, provider=$provider")
        throw new Exception(s"failed to load context provider: context=$context, provider=$provider")

      case Some(json) => read[ContextProviderConfig](json)

    }

  }

}

case class GetActiveProviderIds()

case class GetProviderBaseConfig(providerId: String)

case class GetProviderBaseConfigs()

case class GetActiveContexts()

case class IsContextActive(context: String)

case class ContextProviderIds(context: String)

case class GetContextProviders(context: String)

case class GetContextProvider(context: String, provider: String)
