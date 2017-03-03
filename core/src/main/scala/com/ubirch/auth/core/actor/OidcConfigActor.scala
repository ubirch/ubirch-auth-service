package com.ubirch.auth.core.actor

import com.ubirch.auth.config.{OidcProviderConfig, RedisKeys}
import com.ubirch.util.futures.FutureUtil
import com.ubirch.util.json.JsonFormats

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

  override def receive: Receive = {

    case _: GetActiveProviderIds =>
      val sender = context.sender()
      activeProviderIds() map(sender ! _)

    case msg: GetProviderBaseConfig =>
      val sender = context.sender()
      providerBaseConfig(msg.providerId) map(sender ! _)

    case _: GetProviderBaseConfigs =>
      val sender = context.sender()
      providerBaseConfigs() map(sender ! _)

    case _ => log.error("unknown message")

  }

  private def activeProviderIds(): Future[Seq[String]] = {
    val redis = RedisClient()
    redis.lrange[String](RedisKeys.OIDC_PROVIDER_LIST, 0, -1)
  }


  private def providerBaseConfig(providerId: String): Future[OidcProviderConfig] = {

    val redis = RedisClient()

    val providerKey = RedisKeys.providerKey(providerId)
    log.debug(s"Redis key: providerKey=$providerKey")
    val redisResult = redis.get[String](providerKey)
    redisResult map {

      case None =>
        log.error(s"failed to load provider: $providerKey")
        throw new Exception(s"failed to load provider base config: providerKey=$providerKey")

      case Some(json) => read[OidcProviderConfig](json.toString)

    }

  }

  private def providerBaseConfigs(): Future[Seq[OidcProviderConfig]] = {

    activeProviderIds() flatMap { providerIdList =>

      val configList: Seq[Future[OidcProviderConfig]] = providerIdList map providerBaseConfig
      FutureUtil.unfoldInnerFutures(configList.toList)

    }

  }

}

case class GetActiveProviderIds()

case class GetProviderBaseConfig(providerId: String)

case class GetProviderBaseConfigs()
