package com.ubirch.login.core.actor

import com.ubirch.login.core.manager.ProviderInfoManager
import com.ubirch.util.model.JsonErrorResponse

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContextExecutor

/**
  * author: cvandrei
  * since: 2017-01-26
  */
class ProviderInfoActor extends Actor
  with ActorLogging {

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher

  override def receive: Receive = {

    case _: ProviderInfoList => context.sender() ! ProviderInfoManager.providerInfoList()

    case _ =>
      log.error("unknown message")
      sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")

  }

}

case class ProviderInfoList()
