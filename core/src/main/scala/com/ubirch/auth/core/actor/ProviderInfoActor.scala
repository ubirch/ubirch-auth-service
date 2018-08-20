package com.ubirch.auth.core.actor

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.manager.ProviderInfoManager
import com.ubirch.auth.model.ProviderInfo
import com.ubirch.util.model.JsonErrorResponse

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.RoundRobinPool

import scala.concurrent.ExecutionContextExecutor

/**
  * author: cvandrei
  * since: 2017-01-26
  */
class ProviderInfoActor extends Actor
  with ActorLogging {

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher

  override def receive: Receive = {

    case getInfos: GetProviderInfoList =>

      val sender = context.sender
      ProviderInfoManager.providerInfoList(context = getInfos.context, appId = getInfos.appId) map(sender ! ProviderInfoList(_))


  }

  override def unhandled(message: Any): Unit = {
    log.error(s"received from ${context.sender().path} unknown message: ${message.toString} (${message.getClass})")
    context.sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")
  }

}

object ProviderInfoActor {
  def props(): Props = new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props[ProviderInfoActor])
}

case class GetProviderInfoList(context: String, appId: String)

case class ProviderInfoList(seq: Seq[ProviderInfo])
