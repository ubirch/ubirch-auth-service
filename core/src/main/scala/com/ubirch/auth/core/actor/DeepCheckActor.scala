package com.ubirch.auth.core.actor

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.manager.DeepCheckManager
import com.ubirch.util.deepCheck.model.{DeepCheckRequest, DeepCheckResponse}

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.HttpExt
import akka.routing.RoundRobinPool
import akka.stream.Materializer

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  * author: cvandrei
  * since: 2017-06-08
  */
class DeepCheckActor(implicit httpClient: HttpExt, materializer: Materializer) extends Actor
  with ActorLogging {

  implicit val executionContext: ExecutionContextExecutor = context.dispatcher
  implicit val akkaSystem: ActorSystem = context.system

  override def receive: Receive = {

    case _: DeepCheckRequest =>

      val sender = context.sender()
      deepCheck() map (sender ! _)

  }

  override def unhandled(message: Any): Unit = {
    log.error(s"received from ${context.sender().path} unknown message: ${message.toString} (${message.getClass})")
  }

  private def deepCheck(): Future[DeepCheckResponse] = DeepCheckManager.connectivityCheck()

}

object DeepCheckActor {
  def props()(implicit httpClient: HttpExt, materializer: Materializer): Props = new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props(new DeepCheckActor))
}
