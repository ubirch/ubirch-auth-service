package com.ubirch.auth.core.actor

import com.ubirch.auth.core.manager.LogoutManager
import com.ubirch.auth.model.Logout
import com.ubirch.util.model.JsonErrorResponse

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

/**
  * author: cvandrei
  * since: 2017-02-01
  */
class LogoutActor extends Actor
  with ActorLogging {

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher

  override def receive: Receive = {

    case logout: Logout =>

      val sender = context.sender()
      LogoutManager.logout(logout).onComplete {

        case Success(status: Boolean) => sender ! status

        case Failure(t) =>
          log.error(t, s"failed to logout token: $logout")
          sender ! JsonErrorResponse(errorType = "ServerError", errorMessage = "logout failed")

      }

    case _ =>
      log.error("unknown message")
      sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")

  }

}
