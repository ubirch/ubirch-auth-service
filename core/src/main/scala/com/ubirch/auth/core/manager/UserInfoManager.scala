package com.ubirch.auth.core.manager

import com.ubirch.auth.core.manager.util.UserInfoUtil
import com.ubirch.auth.model.{UserInfo, UserUpdate}
import com.ubirch.user.core.manager.{GroupsManager, UserManager}
import com.ubirch.user.model.db.User
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.model.UserContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2017-04-25
  */
object UserInfoManager {

  /**
    * @param userContext user is determined based on this context
    * @param mongo       mongo connection
    * @return None if no user is found; Some otherwise
    */
  def getInfo(userContext: UserContext)
             (implicit mongo: MongoUtil): Future[Option[UserInfo]] = {

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

          val myGroups = groups filter (_.ownerId == user.id)
          val allowedGroups = groups diff myGroups

          val info = UserInfo(
            displayName = user.displayName,
            locale = user.locale,
            activeUser = user.activeUser.getOrElse(false),
            myGroups = UserInfoUtil.toUserInfoGroups(myGroups),
            allowedGroups = UserInfoUtil.toUserInfoGroups(allowedGroups)
          )
          Some(info)

        }


    }

  }

  def update(userContext: UserContext, userUpdate: UserUpdate)
            (implicit mongo: MongoUtil): Future[Option[UserInfo]] = {

    UserManager.findByProviderIdAndExternalId(
      providerId = userContext.providerId,
      externalUserId = userContext.userId
    ) flatMap {

      case None => Future(None)

      case Some(user: User) =>

        val forUpdate = user.copy(displayName = userUpdate.displayName)
        if (user == forUpdate) {

          getInfo(userContext)

        } else {

          UserManager.update(forUpdate) flatMap {
            case None => Future(None)
            case Some(_: User) => getInfo(userContext)
          }

        }

    }

  }

}
