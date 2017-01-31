package com.ubirch.auth.core.actor

import com.ubirch.auth.model.token.AfterLogin
import com.ubirch.util.model.JsonErrorResponse

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContextExecutor

/**
  * author: cvandrei
  * since: 2017-01-31
  */
class TokenActor extends Actor
  with ActorLogging {

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher

  override def receive: Receive = {

    case afterLogin: AfterLogin =>
      context.sender() ! afterLogin // TODO implement logic verify "code" and "status" from "afterLogin"

    case _ =>
      log.error("unknown message")
      sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")

  }

}
