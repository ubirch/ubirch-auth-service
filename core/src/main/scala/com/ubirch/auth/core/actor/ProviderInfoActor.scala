package com.ubirch.auth.core.actor

import com.ubirch.auth.core.manager.ProviderInfoManager
import com.ubirch.auth.model.ProviderInfo
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

    case _: GetProviderInfoList => sender ! ProviderInfoList(ProviderInfoManager.providerInfoList())

    case _ =>
      log.error("unknown message")
      sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")

  }

}

case class GetProviderInfoList()

case class ProviderInfoList(seq: Seq[ProviderInfo])
