package com.ubirch.auth.core.manager

import com.ubirch.auth.model.{UserInfo, UserUpdate}
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

    // TODO implement
    Future(None)

  }

  def update(userContext: UserContext, userUpdate: UserUpdate)
            (implicit mongo: MongoUtil): Future[Option[UserInfo]] = {

    // TODO implement
    Future(None)

  }

}
