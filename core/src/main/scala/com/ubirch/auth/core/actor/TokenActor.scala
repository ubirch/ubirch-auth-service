package com.ubirch.auth.core.actor

import com.ubirch.auth.core.manager.{TokenManager, VerifyCodeError, VerifyCodeResult}
import com.ubirch.auth.model.{AfterLogin, Token}
import com.ubirch.util.model.JsonErrorResponse

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

/**
  * author: cvandrei
  * since: 2017-01-31
  */
class TokenActor extends Actor
  with ActorLogging {

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher

  override def receive: Receive = {

    case afterLogin: AfterLogin =>

      val sender = context.sender()
      TokenManager.verifyCode(afterLogin).onComplete {

        case Success(verifyCodeResult: VerifyCodeResult) =>

          verifyCodeResult.token match {

            case Some(token) => sender ! Token(token)

            case None =>

              verifyCodeResult.errorType match {

                case VerifyCodeError.InvalidState =>
                  // TODO BadRequest (400)
                  log.error("unable to verify unknown state (it might be beyond it's TTL)")
                  sender ! JsonErrorResponse(errorType = "InvalidState", errorMessage = "invalid state")

                case VerifyCodeError.LoginFailed =>
                  // TODO Unauthorized (401)
                  log.error("code verification failed")
                  sender ! JsonErrorResponse(errorType = "LoginFailed", errorMessage = "invalid code")

                case None =>
                  // TODO InternalServerError (500)
                  log.error("request does not make sense: token and errorType were None (check TokenManager.verifyCode() for bugs!!!)")
                  sender ! JsonErrorResponse(errorType = "ServerError", errorMessage = "internal server error")

                case _ =>
                  // TODO InternalServerError (500)
                  log.error(s"unknown server error")
                  sender ! JsonErrorResponse(errorType = "ServerError", errorMessage = "internal server error")

              }


          }

        case Failure(t) =>
          log.error(t, s"code verification failed: afterLogin=$afterLogin")
          sender ! JsonErrorResponse(errorType = "ServerError", errorMessage = "code verification failed")

      }

    case _ =>
      log.error("unknown message")
      sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")

  }

}
