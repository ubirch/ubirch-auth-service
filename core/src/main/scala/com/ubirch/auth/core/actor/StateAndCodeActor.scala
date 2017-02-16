package com.ubirch.auth.core.actor

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.manager.TokenManager
import com.ubirch.auth.util.oidc.OidcUtil

import akka.actor.{Actor, ActorLogging, ActorSystem}
import redis.RedisClient

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

/**
  * author: cvandrei
  * since: 2017-02-10
  */
class StateAndCodeActor extends Actor
  with ActorLogging {

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher
  implicit val akkaSystem: ActorSystem = context.system

  override def receive: Receive = {

    case vc: VerifyCode =>
      val sender = context.sender()
      verifyCodeGetToken(vc) map (sender ! _)

    case rs: RememberState => rememberState(rs)

    case ds: DeleteState => deleteState(ds)

    case vse: VerifyStateExists =>
      val sender = context.sender()
      stateExists(vse) map (sender ! _)

    case rt: RememberToken => rememberToken(rt)

    case dt: DeleteToken => deleteToken(dt)

    case vte: VerifyTokenExists =>
      val sender = context.sender()
      tokenExists(vte) map (sender ! _)

    case _ => log.error("unknown message")

  }

  private def verifyCodeGetToken(vc: VerifyCode): Future[VerifyCodeResult] = {

    // TODO refactor to be idempotent?
    val provider = vc.provider
    val code = vc.code
    val state = vc.state

    stateExists(VerifyStateExists(provider, state)) map {

      case true =>

        TokenManager.verifyCodeWith3rdParty(provider, code) match {

          case None => VerifyCodeResult(errorType = Some(VerifyCodeError.CodeVerification))

          case Some(tokenUserId) =>

            val token = tokenUserId.token
            val userId = tokenUserId.userId
            log.debug(s"token=$token, userId=$userId")
            self ! RememberToken(provider, token, userId)
            self ! DeleteState(provider, state)

            VerifyCodeResult(token = Some(token))

        }

      case false =>

        log.info(s"unknown state: $vc")
        VerifyCodeResult(errorType = Some(VerifyCodeError.UnknownState))

    }

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

  private def deleteState(ds: DeleteState): Future[Boolean] = {

    val redis = RedisClient()

    val provider = ds.provider
    val state = ds.state
    val key = OidcUtil.stateToHashedKey(provider, state)

    redis.del(key) map {

      case 1 =>
        log.debug(s"deleted state: $provider:$state ")
        true

      case 0 =>
        log.error(s"failed to delete state: $provider:$state")
        false

      case _ =>
        log.error(s"unexpected error while deleting state: $provider:$state")
        false

    }

  }

  private def stateExists(vse: VerifyStateExists): Future[Boolean] = {

    val redis = RedisClient()

    val provider = vse.provider
    val state = vse.state
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
          case true => log.info(s"remembered token for provider=$provider (ttl: ${ttl.get} seconds)")
          case false => log.error(s"failed to remember token for provider=$provider (ttl: ${ttl.get} seconds)")
        }

      case Failure(e) => log.error(s"failed to remember token for provider=$provider (ttl: ${ttl.get} seconds)", e)

    }

  }

  private def tokenExists(vte: VerifyTokenExists): Future[Boolean] = {

    val redis = RedisClient()

    val provider = vte.provider
    val token = vte.token
    val key = OidcUtil.tokenToHashedKey(provider, token)

    redis.exists(key)

  }

  private def deleteToken(dt: DeleteToken): Future[Boolean] = {

    val redis = RedisClient()

    val provider = dt.provider
    val token = dt.token
    val key = OidcUtil.tokenToHashedKey(provider, token)

    redis.del(key) map {

      case 1 =>
        log.debug(s"deleted token: $provider:$token")
        true

      case 0 =>
        log.error(s"failed to delete token: $provider:$token")
        false

      case _ =>
        log.error(s"unexpected error while deleting token: $provider:$token")
        false

    }

  }

}

case class VerifyCode(provider: String,
                      code: String,
                      state: String
                     )

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

case class VerifyTokenExists(provider: String,
                             token: String
                            )

case class DeleteToken(provider: String,
                       token: String
                      )

case class VerifyCodeResult(token: Option[String] = None,
                            errorType: Option[VerifyCodeError.Value] = None
                           )

object VerifyCodeError extends Enumeration {
  val UnknownState, LoginFailed, CodeVerification, Server = Value
}