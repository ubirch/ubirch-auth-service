package com.ubirch.auth.core.manager

import com.ubirch.auth.model.{UserInfo, UserInfoGroup, UserUpdate}
import com.ubirch.user.core.manager.{GroupsManager, UserManager}
import com.ubirch.user.model.db.{Group, User}
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.model.UserContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2017-04-25
  */
object UserInfoManager {

  def getInfo(userContext: UserContext)
             (implicit mongo: MongoUtil): Future[Option[UserInfo]] = {

    // TODO automated tests
    UserManager.findByProviderIdAndExternalId(
      providerId = userContext.providerId,
      externalUserId = userContext.userId
    ) flatMap {

      case None => Future(None)

      case Some(user: User) =>

        GroupsManager.findByContextAndUser(
          contextName = userContext.context,
          providerId = userContext.providerId,
          externalUserId = userContext.userId
        ) map { groups =>

          val myGroups = groups filter(_.ownerId == user.id)
          val allowedGroups = groups diff myGroups

          val info = UserInfo(
            displayName = user.displayName,
            myGroups = toUserInfoGroups(myGroups),
            allowedGroups = toUserInfoGroups(allowedGroups)
          )
          Some(info)

        }


    }

  }

  def update(userContext: UserContext, userUpdate: UserUpdate)
            (implicit mongo: MongoUtil): Future[Option[UserInfo]] = {

    // TODO implement
    Future(None)

  }

  private def toUserInfoGroups(groups: Set[Group]): Set[UserInfoGroup] = {
    groups map { group =>
      UserInfoGroup(group.id, group.displayName)
    }
  }

}
