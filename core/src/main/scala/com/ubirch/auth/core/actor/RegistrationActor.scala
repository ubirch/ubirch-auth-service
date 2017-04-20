package com.ubirch.auth.core.actor

import com.ubirch.auth.model.{NewUser, UserInfo}
import com.ubirch.util.model.JsonErrorResponse
import com.ubirch.util.oidc.model.UserContext

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2017-04-20
  */
class RegistrationActor extends Actor
  with ActorLogging {

  override def receive: Receive = {

    case register: RegisterUser =>
      val sender = context.sender
      val result = Future(UserInfo()) // TODO implement
      result map (sender ! _)

    case _ =>
      log.error("unknown message")
      sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")

  }

}

case class RegisterUser(userContext: UserContext,
                        newUser: NewUser
                       )
