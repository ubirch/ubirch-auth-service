package com.ubirch.auth.core.actor

import com.ubirch.auth.core.manager.UserInfoManager
import com.ubirch.auth.model.UserUpdate
import com.ubirch.util.model.JsonErrorResponse
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.model.UserContext

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * author: cvandrei
  * since: 2017-04-25
  */
class UserInfoActor(implicit mongo: MongoUtil) extends Actor
  with ActorLogging {

  override def receive: Receive = {

    case get: GetInfo =>
      val sender = context.sender()
      UserInfoManager.getInfo(get.userContext) map (sender ! _)

    case update: UpdateInfo =>
      val sender = context.sender()
      UserInfoManager.update(update.userContext, update.update) map (sender ! _)

    case _ =>
      log.error("unknown message")
      sender ! JsonErrorResponse(errorType = "UnknownMessage", errorMessage = "unable to handle message")

  }

}

case class GetInfo(userContext: UserContext)

case class UpdateInfo(userContext: UserContext, update: UserUpdate)
