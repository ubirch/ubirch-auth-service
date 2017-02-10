package com.ubirch.auth.server.route

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{RedisActor, VerifyCode, VerifyCodeError, VerifyCodeResult}
import com.ubirch.auth.model.{AfterLogin, Token}
import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.http.response.ResponseUtil
import com.ubirch.util.json.MyJsonProtocol
import com.ubirch.util.model.JsonErrorResponse
import com.ubirch.util.rest.akka.directives.CORSDirective

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-01-31
  */
trait TokenRoute extends MyJsonProtocol
  with CORSDirective
  with ResponseUtil
  with StrictLogging {

  implicit val system = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  // TODO extract anything performance related to config
  private val redisActor = system.actorOf(new RoundRobinPool(3).props(Props[RedisActor]), ActorNames.REDIS)

  val route: Route = {

    path(RouteConstants.verify / RouteConstants.code) {
      respondWithCORS {

        post {
          entity(as[AfterLogin]) { afterLogin =>
            onSuccess(redisActor ? VerifyCode(afterLogin.providerId, afterLogin.code, afterLogin.state)) {

              case verifyCodeResult: VerifyCodeResult =>

                verifyCodeResult.token match {

                  case Some(token) => complete(Token(token))

                  case None =>

                    verifyCodeResult.errorType match {

                      case Some(VerifyCodeError.UnknownState) =>
                        val jsonError = JsonErrorResponse(errorType = "UnknownState", errorMessage = "invalid state")
                        complete(requestErrorResponse(jsonError, BadRequest))

                      case Some(VerifyCodeError.LoginFailed) =>
                        logger.error("code verification failed")
                        val jsonError = JsonErrorResponse(errorType = "LoginFailed", errorMessage = "invalid code")
                        complete(requestErrorResponse(jsonError, Unauthorized))

                      case None =>
                        logger.error("request does not make sense: the resulting token and errorType were None (check RedisActor.verifyCode() for bugs!!!)")
                        val jsonError = JsonErrorResponse(errorType = "ServerError", errorMessage = "internal server error")
                        complete(serverErrorResponse(jsonError))

                      case _ =>
                        logger.error(s"unknown server error")
                        val jsonError = JsonErrorResponse(errorType = "ServerError", errorMessage = "internal server error")
                        complete(serverErrorResponse(jsonError))

                    }

                }

              case _ => complete(serverErrorResponse(errorType = "VerificationError", errorMessage = "failed to verify code"))

            }
          }
        }

      }
    }

  }

}
