package com.ubirch.auth.server.route

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{RegisterUser, RegistrationActor}
import com.ubirch.auth.model.UserInfo
import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.http.response.ResponseUtil
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.oidc.directive.OidcDirective
import com.ubirch.util.oidc.model.UserContext
import com.ubirch.util.redis.RedisClientUtil
import com.ubirch.util.rest.akka.directives.CORSDirective

import akka.actor.ActorSystem
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.server.Route
import akka.pattern.ask
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
  * since: 2017-04-20
  */
class RegisterRoute(implicit mongo: MongoUtil,
                    httpClient: HttpExt,
                    materializer: Materializer
                   )
  extends ResponseUtil
    with CORSDirective
    with StrictLogging {

  implicit val system = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  private val registrationActor = system.actorOf(RegistrationActor.props(), ActorNames.REGISTRATION)

  implicit protected val redis: RedisClient = RedisClientUtil.getRedisClient()
  private val oidcDirective = new OidcDirective()

  val route: Route = {

    path(RouteConstants.register) {
      respondWithCORS {
        oidcDirective.oidcToken2UserContext { userContext =>

          logger.debug(s"userContext=$userContext")
          post {
            registerUser(userContext)
          }

        }
      }
    }

  }

  private def registerUser(userContext: UserContext): Route = {

    onComplete(registrationActor ? RegisterUser(userContext)) {

      case Failure(t) =>
        logger.error("register user call responded with an unhandled message: ", t)
        complete(serverErrorResponse(errorType = "ServerError", errorMessage = t.getMessage))

      case Success(resp) =>

        resp match {

          case Some(userInfo: UserInfo) => complete(userInfo)

          case None =>
            logger.error("failed to register user (None)")
            complete(requestErrorResponse(errorType = "RegistrationError", errorMessage = "failed to register user"))

          case _ =>
            logger.error("failed to register user (server error)")
            complete(serverErrorResponse(errorType = "ServerError", errorMessage = "failed to register user"))

        }

    }

  }

}
