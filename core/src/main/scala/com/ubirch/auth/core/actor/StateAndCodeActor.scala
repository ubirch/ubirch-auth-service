package com.ubirch.auth.core.actor

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.manager.TokenManager
import com.ubirch.auth.core.redis.RedisConnection
import com.ubirch.auth.model.db.{ContextProviderConfig, OidcProviderConfig}
import com.ubirch.util.json.JsonFormats
import com.ubirch.util.oidc.model.UserContext
import com.ubirch.util.oidc.util.OidcUtil

import org.json4s.native.Serialization.write

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import redis.RedisClient

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
  * author: cvandrei
  * since: 2017-02-10
  */
class StateAndCodeActor extends Actor
  with ActorLogging {

  implicit private val formatter = JsonFormats.default

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher
  implicit val akkaSystem: ActorSystem = context.system
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  private val oidcConfigActor = context.actorOf(new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props[OidcConfigActor]), ActorNames.OIDC_CONFIG)

  override def receive: Receive = {

    case vc: VerifyCode =>
      val sender = context.sender()
      verifyCodeGetToken(vc) map (sender ! _)

    case rs: RememberState => rememberState(rs) // TODO integration tests

    case ds: DeleteState => // TODO integration tests
      val sender = context.sender()
      deleteState(ds) map (sender ! _)

    case vse: VerifyStateExists => // TODO integration tests
      val sender = context.sender()
      stateExists(vse) map (sender ! _)

    case rt: RememberToken => rememberToken(rt) // TODO integration tests

    case dt: DeleteToken => // TODO integration tests
      val sender = context.sender()
      deleteToken(dt) map (sender ! _)

    case vte: VerifyTokenExists => // TODO integration tests
      val sender = context.sender()
      tokenExists(vte) map (sender ! _)

    case _ => log.error("unknown message")

  }

  private def verifyCodeGetToken(vc: VerifyCode): Future[VerifyCodeResult] = {

    // TODO refactor to be idempotent?
    val context = vc.context
    val provider = vc.provider
    val code = vc.code
    val state = vc.state

    stateExists(VerifyStateExists(provider, state)) flatMap {

      case true =>

        for {
          providerConf <- (oidcConfigActor ? GetProviderBaseConfig(provider)).mapTo[OidcProviderConfig]
          contextProviderConf <- (oidcConfigActor ? GetContextProvider(context, provider)).mapTo[ContextProviderConfig]
        } yield {

          TokenManager.verifyCodeWith3rdParty(contextProvider = contextProviderConf, providerConf = providerConf, code = code) match {

            case None => VerifyCodeResult(errorType = Some(VerifyCodeError.CodeVerification))

            case Some(tokenUserId) =>

              val token = tokenUserId.token
              val userId = tokenUserId.userId
              log.debug(s"token=$token, userId=$userId")
              self ! RememberToken(context = context, token = token, userId = userId)
              self ! DeleteState(provider, state)

              VerifyCodeResult(token = Some(token))

          }

        }

      case false =>

        log.info(s"unknown state: $vc")
        Future(VerifyCodeResult(errorType = Some(VerifyCodeError.UnknownState)))

    }

  }

  private def rememberState(rs: RememberState): Unit = {

    val redis = redisClient()

    val provider = rs.provider
    val state = rs.state
    val key = OidcUtil.stateToHashedKey(provider, state)
    val ttl = Config.oidcStateTtl()

    redis.set(key, "1", exSeconds = Some(ttl)) onComplete {

      case Success(result) =>

        result match {

          case true =>
            log.debug(s"remembered state: $state (ttl: $ttl seconds), provider=$provider, key=$key")
            log.info(s"remembered state (ttl: $ttl seconds), provider=$provider")

          case false => log.error(s"failed to remember state: $state (ttl: $ttl seconds)")

        }

      case Failure(e) => log.error(s"failed to remember state: $state (ttl: $ttl seconds)", e)

    }

  }

  private def deleteState(ds: DeleteState): Future[Boolean] = {

    val redis = redisClient()

    val provider = ds.provider
    val state = ds.state
    val key = OidcUtil.stateToHashedKey(provider, state)

    redis.del(key) map {

      case 1 =>
        log.debug(s"deleted state: $provider::$state (key=$key)")
        log.info(s"deleted state: provider=$provider")
        true

      case 0 =>
        log.error(s"failed to delete state: provider=$provider (key=$key)")
        false

      case _ =>
        log.error(s"unexpected error while deleting state: provider=$provider (key=$key)")
        false

    }

  }

  private def stateExists(vse: VerifyStateExists): Future[Boolean] = {

    val redis = redisClient()

    val provider = vse.provider
    val state = vse.state
    val key = OidcUtil.stateToHashedKey(provider, state)

    redis.exists(key)

  }

  private def rememberToken(rt: RememberToken): Unit = {

    val redis = redisClient()

    val token = rt.token
    val context = rt.context
    val userId = rt.userId
    val userContext = UserContext(context = context, userId = userId)
    val userContextJson = write(userContext)
    val key = OidcUtil.tokenToHashedKey(token)
    val ttl = Config.oidcTokenTtl()

    redis.set(key, userContextJson, exSeconds = Some(ttl)) onComplete {

      case Success(result) =>

        result match {

          case true =>
            log.debug(s"remembered token (ttl: $ttl seconds), key=$key, userId=$userId, context=$context, token=$token")
            log.info(s"remembered token (ttl: $ttl seconds)")

          case false => log.error(s"failed to remember token (ttl: $ttl seconds)")

        }

      case Failure(e) => log.error(s"failed to remember token (ttl: $ttl seconds)", e)

    }

  }

  private def tokenExists(vte: VerifyTokenExists): Future[Boolean] = {

    val redis = redisClient()

    val token = vte.token
    val key = OidcUtil.tokenToHashedKey(token)

    redis.exists(key)

  }

  private def deleteToken(dt: DeleteToken): Future[Boolean] = {

    val redis = redisClient()

    val token = dt.token
    val key = OidcUtil.tokenToHashedKey(token)

    redis.del(key) map {

      case 1 => true

      case 0 =>
        log.error(s"failed to delete token: key=$key")
        false

      case _ =>
        log.error(s"unexpected error while deleting token: key=$key")
        false

    }

  }

  private def redisClient(): RedisClient = RedisConnection.client(akkaSystem)

}

case class VerifyCode(context: String,
                      provider: String,
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

case class RememberToken(context: String,
                         token: String,
                         userId: String
                        )

case class VerifyTokenExists(token: String)

case class DeleteToken(token: String)

case class VerifyCodeResult(token: Option[String] = None,
                            errorType: Option[VerifyCodeError.Value] = None
                           )

object VerifyCodeError extends Enumeration {
  val UnknownState, LoginFailed, CodeVerification, Server = Value
}