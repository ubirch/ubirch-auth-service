package com.ubirch.auth.core.manager

import java.util.UUID

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.model.{UserInfo, UserInfoGroup}
import com.ubirch.user.core.manager.{ContextManager, GroupManager, GroupsManager, UserManager}
import com.ubirch.user.model.db.{Context, Group, User}
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

  def register(userContext: UserContext)(implicit mongo: MongoUtil): Future[Option[UserInfo]] = {

    val context = userContext.context
    val providerId = userContext.providerId
    val externalUserId = userContext.userId

    for {

      userOpt <- UserManager.findByProviderIdAndExternalId(
        providerId = providerId,
        externalUserId = externalUserId
      )

      contextOpt <- ContextManager.findByName(context)

      groups <- GroupsManager.findByContextAndUser(
        contextName = context,
        providerId = providerId,
        externalUserId = externalUserId
      )

      userInfo <- createUserAndOrGroup(
        userContext = userContext,
        userOpt = userOpt,
        contextOpt = contextOpt,
        groups = groups
      )

    } yield userInfo

  }

  private def userInfo(userDisplayName: String,
                       locale: String,
                       groupId: UUID,
                       groupDisplayName: String
                      ): Option[UserInfo] = {

    Some(
      UserInfo(
        displayName = userDisplayName,
        locale = locale,
        myGroups = Set(
          UserInfoGroup(groupId, groupDisplayName)
        )
      )
    )

  }


  private def createUserAndOrGroup(userContext: UserContext,
                                   userOpt: Option[User],
                                   contextOpt: Option[Context],
                                   groups: Set[Group]
                                  )
                                  (implicit mongo: MongoUtil): Future[Option[UserInfo]] = {

    val contextName = userContext.context

    if (contextOpt.isEmpty) {

      logger.error(s"unable to register user if context does not exist in database: context=$contextName")
      Future(None)

    } else if (groups.nonEmpty) {

      logger.debug(s"user has been registered before since they created groups: context=$contextName, user=$userOpt, groups=$groups")
      logger.error("user has been registered before since they created groups")
      Future(None)

    } else {

      val context = contextOpt.get
      val groupDisplayName = s"${userContext.userName}"

      if (userOpt.isDefined) {

        logger.debug(s"register user: create group (context=$context, name=${userContext.userName})")
        createGroup(groupDisplayName, userOpt.get, context)

      } else {

        logger.debug(s"register user: create user and group (context=$context, name=${userContext.userName})")
        createUser(
          displayName = userContext.userName,
          providerId = userContext.providerId,
          externalId = userContext.userId,
          locale = userContext.locale
        ) flatMap {

          case None =>
            logger.error(s"failed to create user: contextName=$contextName")
            Future(None)

          case Some(userCreated: User) => createGroup(groupDisplayName, userCreated, context)

        }

      }

    }

  }

  private def createUser(displayName: String,
                         providerId: String,
                         externalId: String,
                         locale: String
                        )
                        (implicit mongo: MongoUtil): Future[Option[User]] = {

    val user = User(
      displayName = displayName,
      providerId = providerId,
      externalId = externalId,
      locale = locale
    )
    UserManager.create(user)

  }

  private def createGroup(displayName: String,
                          owner: User,
                          context: Context
                         )
                         (implicit mongo: MongoUtil): Future[Option[UserInfo]] = {

    val group = Group(
      displayName = displayName,
      ownerId = owner.id,
      contextId = context.id,
      allowedUsers = Set.empty
    )
    GroupManager.create(group) map {

      case None =>
        logger.error(s"group creation failed: user=${owner.displayName}/${owner.id}, group=$group")
        None

      case Some(groupCreated: Group) =>
        logger.debug(s"created group: group=$group")
        userInfo(
          userDisplayName = owner.displayName,
          locale = owner.locale,
          groupId = UUIDUtil.fromString(groupCreated.id),
          groupDisplayName = groupCreated.displayName
        )

    }

  }

}
