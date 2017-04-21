package com.ubirch.auth.core.manager

import java.util.UUID

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.model.{NewUser, UserInfo, UserInfoGroup}
import com.ubirch.user.core.manager.{ContextManager, GroupManager, GroupsManager, UserManager}
import com.ubirch.user.model.db.Group
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.model.UserContext
import com.ubirch.util.uuid.UUIDUtil

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

    val context = userContext.context
    val providerId = userContext.providerId
    val externalUserId = userContext.userId

    for {

      userOpt <- UserManager.findByProviderIdAndExternalId(providerId = providerId, externalUserId = externalUserId)
      contextOpt <- ContextManager.findByName(context)
      groups <- GroupsManager.findByContextAndUser(contextName = context, providerId = providerId, externalUserId = externalUserId)

    } yield {

      if (contextOpt.isEmpty) {
        logger.error("unable to register user if context does not exist in database")
        None
      } else if (groups.nonEmpty) {
        logger.debug(s"user already has groups in this context and must have been registered before: context=$context, user=$userOpt, groups=$groups")
        logger.error("user already has groups in this context and must have been registered before")
        None
      } else {

        if (userOpt.isDefined) {

          logger.debug(s"user already exists: context=$userContext, newUser=$newUser")

          // TODO check if newUser.displayName differs from current one --> update if it does
          val userDisplayName = userOpt.get.displayName
          val contextId = contextOpt.get.id
          val ownerId = userOpt.get.id
          val group = Group(
            displayName = newUser.myGroup,
            ownerId = ownerId,
            contextId = contextId,
            allowedUsers = Set.empty
          )
          // TODO solve compile error
          GroupManager.create(group) map {

            case None =>
              logger.error(s"register: user already existed but group creation failed: user=$userDisplayName, group=$group")
              None

            case Some(groupCreated: Group) =>
              logger.debug(s"register: user already existed and only created a group: user=$userDisplayName, group=$groupCreated")
              userInfo(userDisplayName, groupCreated.id, groupCreated.displayName)

          }

        } else {

          logger.debug("register user: create user and group")
          // TODO create user and group
          userInfo(newUser.displayName, UUIDUtil.uuid, newUser.myGroup)

        }

      }

    }

  }

  private def userInfo(userDisplayName: String, groupId: UUID, groupDisplayName: String): Option[UserInfo] = {

    Some(
      UserInfo(
        displayName = userDisplayName,
        myGroups = Set(
          UserInfoGroup(groupId, groupDisplayName)
        )
      )
    )

  }

}
