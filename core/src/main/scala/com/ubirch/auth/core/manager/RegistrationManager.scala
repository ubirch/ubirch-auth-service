package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.model.{NewUser, UserInfo}
import com.ubirch.user.core.manager.UserManager
import com.ubirch.user.model.db.User
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.model.UserContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2017-04-21
  */
object RegistrationManager extends StrictLogging {

  def register(userContext: UserContext,
               newUser: NewUser
              )
              (implicit mongo: MongoUtil): Future[Option[UserInfo]] = {

    UserManager.findByProviderIdAndExternalId(userContext.providerId, userContext.userId) map {

      case None =>
        logger.debug(s"user does not exist --> register: context=$userContext, newUser=$newUser")
        // TODO implement
        Some(
          UserInfo(
            displayName = newUser.displayName,
            myGroups = Set(newUser.myGroup
            )
          )
        )

      case Some(_: User) =>
        logger.debug(s"unable to register existing user: context=$userContext, newUser=$newUser")
        None

    }

  }

}
