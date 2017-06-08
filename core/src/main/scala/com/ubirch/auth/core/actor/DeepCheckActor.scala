package com.ubirch.auth.core.actor

import com.ubirch.auth.core.manager.DeepCheckManager
import com.ubirch.util.model.DeepCheckResponse

import akka.actor.{Actor, ActorLogging, ActorSystem}

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  * author: cvandrei
  * since: 2017-06-08
  */
class DeepCheckActor extends Actor
  with ActorLogging {

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher
  implicit val akkaSystem: ActorSystem = context.system

  override def receive: Receive = {

    case _: DeepCheckRequest =>
      val sender = context.sender()
      deepCheck() map (sender ! _)

    case _ => log.error("unknown message")

  }

  private def deepCheck(): Future[DeepCheckResponse] = DeepCheckManager.connectivityCheck()

}

case class DeepCheckRequest()
