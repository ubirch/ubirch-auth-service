package com.ubirch.auth.core.actor

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.util.OidcUtil
import com.ubirch.auth.model.AfterLogin

import akka.actor.{Actor, ActorLogging, ActorSystem}
import redis.RedisClient

import scala.concurrent.{ExecutionContextExecutor, Future}
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

    case rs: RememberState => rememberState(rs)

    case ds: DeleteState => deleteState(ds)

    case vse: VerifyStateExists => // TODO

    case rt: RememberToken => rememberToken(rt)

    case _ => log.error("unknown message")

  }

  private def rememberState(rs: RememberState): Unit = {

    val redis = RedisClient()

    val provider = rs.provider
    val state = rs.state
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

  private def deleteState(ds: DeleteState): Unit = {

    val redis = RedisClient()

    val provider = ds.provider
    val state = ds.state
    val key = OidcUtil.stateToHashedKey(provider, state)

    redis.del(key) onComplete {

      case Success(result) =>
        result match {
          case 1 => log.debug(s"deleted state: $provider:$state ")
          case 0 => log.error(s"failed to delete state: $provider:$state")
          case _ => log.error(s"unexpected error while deleting state: $provider:$state")
        }

      case Failure(e) => log.error(s"failed to delete state: $provider:$state", e)

    }

  }

  private def stateExists(afterLogin: AfterLogin): Future[Boolean] = {

    val redis = RedisClient()

    val provider = afterLogin.providerId
    val state = afterLogin.state
    val key = OidcUtil.stateToHashedKey(provider, state)

    redis.exists(key)

  }

  private def rememberToken(rt: RememberToken): Unit = {

    val redis = RedisClient()

    val provider = rt.provider
    val token = rt.token
    val userId = rt.userId
    val key = OidcUtil.tokenToHashedKey(provider, token)
    val ttl = Some(Config.oidcTokenTtl())

    redis.set(key, userId, ttl) onComplete {

      case Success(result) =>
        result match {
          case true => log.debug(s"remembered token for provider=$provider (ttl: ${ttl.get} seconds)")
          case false => log.error(s"failed to remember token for provider=$provider (ttl: ${ttl.get} seconds)")
        }

      case Failure(e) => log.error(s"failed to remember token for provider=$provider (ttl: ${ttl.get} seconds)", e)

    }

  }

}

case class RememberState(provider: String,
                         state: String
                        )

case class DeleteState(provider: String,
                       state: String
                      )

case class VerifyStateExists(provider: String,
                             state: String
                            )

case class RememberToken(provider: String,
                         token: String,
                         userId: String
                        )
