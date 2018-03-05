package com.ubirch.auth.core.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.RoundRobinPool
import com.ubirch.auth.config.Config
import com.ubirch.auth.core.manager.RegistrationManager
import com.ubirch.util.model.JsonErrorResponse
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.model.UserContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * author: cvandrei
  * since: 2017-04-20
  */
class RegistrationActor(implicit mongo: MongoUtil) extends Actor
  with ActorLogging {

  override def receive: Receive = {

    case register: RegisterUser =>
      val sender = context.sender()
      RegistrationManager.register(register.userContext).onComplete {
        case Success(ui) if ui.isDefined =>
          sender ! ui.get
        case Success(ui) =>
          sender ! JsonErrorResponse(
            errorType = "ValidationError",
            errorMessage = s"Registration failed for ${register.userContext.userId}"
          )
        case Failure(t) =>
          sender ! JsonErrorResponse(
            errorType = "ValidationError",
            errorMessage = t.getMessage
          )
      }

    case _ =>
      log.error("unknown message")
      sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")

  }

}

object RegistrationActor {

  def props()(implicit mongo: MongoUtil): Props = {
    new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props(new RegistrationActor))
  }

}

case class RegisterUser(userContext: UserContext)
