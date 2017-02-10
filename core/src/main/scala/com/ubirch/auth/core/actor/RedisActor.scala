package com.ubirch.auth.core.actor

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.util.OidcUtil

import akka.actor.{Actor, ActorLogging, ActorSystem}
import redis.RedisClient

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

/**
  * author: cvandrei
  * since: 2017-02-10
  */
class RedisActor extends Actor
  with ActorLogging {

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher
  implicit val akkaSystem: ActorSystem = context.system

  override def receive: Receive = {

    case rs: RememberState => rememberState(rs.provider, rs.state)

    case vs: VerifyStateExists => // TODO

    case rt: RememberToken => // TODO

    case _ => log.error("unknown message")

  }

  private def rememberState(provider: String, state: String): Unit = {

    val redis = RedisClient()

    val key = OidcUtil.stateToHashedKey(provider, state)
    val ttl = Some(Config.oidcStateTtl())

    redis.set(key, "1", ttl) onComplete {

      case Success(result) =>
        result match {
          case true => log.debug(s"remembered state: $state (ttl: ${ttl.get} seconds)")
          case false => log.error(s"failed to remember state: $state (ttl: ${ttl.get} seconds)")
        }

      case Failure(e) => log.error(s"failed to remember state: $state (ttl: ${ttl.get} seconds)", e)

    }

  }

}

case class RememberState(provider: String,
                         state: String
                        )

case class VerifyStateExists(provider: String,
                             state: String
                            )

case class RememberToken(provider: String,
                         token: String
                        )
