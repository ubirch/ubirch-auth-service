package com.ubirch.auth.core.actor

import com.ubirch.auth.core.manager.RegistrationManager
import com.ubirch.auth.model.NewUser
import com.ubirch.util.model.JsonErrorResponse
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.model.UserContext

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * author: cvandrei
  * since: 2017-04-20
  */
class RegistrationActor(implicit mongo: MongoUtil) extends Actor
  with ActorLogging {

  override def receive: Receive = {

    case register: RegisterUser =>
      val sender = context.sender()
      RegistrationManager.register(register.userContext, register.newUser) map (sender ! _)

    case _ =>
      log.error("unknown message")
      sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")

  }

}

case class RegisterUser(userContext: UserContext,
                        newUser: NewUser
                       )
