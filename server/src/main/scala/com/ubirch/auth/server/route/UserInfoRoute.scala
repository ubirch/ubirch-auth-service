package com.ubirch.auth.server.route

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{GetInfo, UpdateInfo, UserInfoActor}
import com.ubirch.auth.model.{UserUpdate, UserInfo}
import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.http.response.ResponseUtil
import com.ubirch.util.json.MyJsonProtocol
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.directive.OidcDirective
import com.ubirch.util.oidc.model.UserContext
import com.ubirch.util.redis.RedisClientUtil
import com.ubirch.util.rest.akka.directives.CORSDirective

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.routing.RoundRobinPool
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
class UserInfoRoute(implicit mongo: MongoUtil) extends MyJsonProtocol
  with CORSDirective
  with ResponseUtil
  with StrictLogging {

  implicit val system = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  private val userInfoActor = system.actorOf(new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props(new UserInfoActor)), ActorNames.USER_INFO)

  implicit protected val redis: RedisClient = RedisClientUtil.getRedisClient()
  private val oidcDirective = new OidcDirective()

  import oidcDirective._

  val route: Route = {

    path(RouteConstants.userInfo) {
      respondWithCORS {
        oidcToken2UserContext { userContext =>

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

    onComplete(userInfoActor ? GetInfo(userContext)) {

      case Failure(t) =>
        logger.error("get-user call responded with an unhandled message (check UserInfoRoute for bugs!!!)", t)
        complete(serverErrorResponse(errorType = "ServerError", errorMessage = "sorry, something went wrong on our end"))

      case Success(resp) =>

        resp match {

          case Some(userInfo: UserInfo) => complete(userInfo)

          case _ =>
            logger.error("failed to get user info")
            complete(serverErrorResponse(errorType = "QueryError", errorMessage = "failed to get user info"))

        }

    }

  }

  private def updateInfo(userContext: UserContext, update: UserUpdate): Route = {

    onComplete(userInfoActor ? UpdateInfo(userContext, update)) {

      case Failure(t) =>
        logger.error("update-user call responded with an unhandled message (check UserInfoRoute for bugs!!!)", t)
        complete(serverErrorResponse(errorType = "ServerError", errorMessage = "sorry, something went wrong on our end"))

      case Success(resp) =>

        resp match {

          case Some(userInfo: UserInfo) => complete(userInfo)

          case _ =>
            logger.error("failed to update user info")
            complete(serverErrorResponse(errorType = "UpdateError", errorMessage = "failed to update user info"))

        }

    }

  }

}
