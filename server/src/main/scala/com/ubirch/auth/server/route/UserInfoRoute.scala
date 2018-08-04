package com.ubirch.auth.server.route

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.model.{UserInfo, UserUpdate}
import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.user.client.rest.UserServiceClientRest
import com.ubirch.user.model.rest.SimpleUserContext
import com.ubirch.util.http.response.ResponseUtil
import com.ubirch.util.json.Json4sUtil
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.directive.OidcDirective
import com.ubirch.util.oidc.model.UserContext
import com.ubirch.util.redis.RedisClientUtil
import com.ubirch.util.rest.akka.directives.CORSDirective

import akka.actor.ActorSystem
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.util.Timeout
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
import redis.RedisClient

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
  * author: cvandrei
  * since: 2017-04-25
  */
class UserInfoRoute(implicit mongo: MongoUtil,
                    system: ActorSystem,
                    httpClient: HttpExt,
                    materializer: Materializer
                   )
  extends ResponseUtil
    with CORSDirective
    with StrictLogging {

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(Config.actorTimeout seconds)

  implicit protected val redis: RedisClient = RedisClientUtil.getRedisClient()
  private val oidcDirective = new OidcDirective()

  val route: Route = {

    path(RouteConstants.userInfo) {
      respondWithCORS {
        oidcDirective.oidcToken2UserContext { userContext =>

          logger.debug(s"userContext=$userContext")
          get {
            getInfo(userContext)
          } ~ put {
            entity(as[UserUpdate]) { update =>
              updateInfo(userContext, update)
            }
          }

        }
      }
    }

  }

  private def getInfo(userContext: UserContext): Route = {

    val restCall = UserServiceClientRest.userInfoGET(
      context = userContext.context,
      providerId = userContext.providerId,
      externalUserId = userContext.userId
    )
    onComplete(restCall) {

      case Failure(t) =>

        logger.error("get-user call responded with an unhandled message (check UserInfoRoute for bugs!!!)", t)
        complete(serverErrorResponse(errorType = "ServerError", errorMessage = "sorry, something went wrong on our end"))

      case Success(resp) =>

        resp match {

          case Some(userInfo: com.ubirch.user.model.rest.UserInfo) =>

            complete(Json4sUtil.any2any[UserInfo](userInfo))

          case None =>

            logger.error("failed to get user info (None)")
            complete(requestErrorResponse(errorType = "NoUserInfoFound", errorMessage = "failed to get user info"))

          case _ =>

            logger.error("failed to get user info (server error)")
            complete(serverErrorResponse(errorType = "ServerError", errorMessage = "failed to get user info"))

        }

    }

  }

  private def updateInfo(userContext: UserContext, update: UserUpdate): Route = {

    val updateInfo = com.ubirch.user.model.rest.UpdateInfo(
      SimpleUserContext(
        context = userContext.context,
        providerId = userContext.providerId,
        userId = userContext.userId
      ),
      Json4sUtil.any2any[com.ubirch.user.model.rest.UserUpdate](update)
    )
    val restCall = UserServiceClientRest.userInfoPUT(updateInfo)
    onComplete(restCall) {

      case Failure(t) =>
        logger.error("update-user call responded with an unhandled message (check UserInfoRoute for bugs!!!)", t)
        complete(serverErrorResponse(errorType = "ServerError", errorMessage = "sorry, something went wrong on our end"))

      case Success(resp) =>

        resp match {

          case Some(userInfo: com.ubirch.user.model.rest.UserInfo) =>

            complete(Json4sUtil.any2any[UserInfo](userInfo))

          case None =>

            logger.error("failed to update user info (None)")
            complete(requestErrorResponse(errorType = "UpdateError", errorMessage = "failed to update user info"))

          case _ =>

            logger.error("failed to update user info (server error)")
            complete(serverErrorResponse(errorType = "ServerError", errorMessage = "failed to update user info"))

        }

    }

  }

}
