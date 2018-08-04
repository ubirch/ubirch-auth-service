package com.ubirch.auth.server.route

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{Logout, LogoutActor}
import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.http.response.ResponseUtil
import com.ubirch.util.rest.akka.directives.CORSDirective

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-02-01
  */
class LogoutRoute(implicit system: ActorSystem) extends ResponseUtil
  with CORSDirective {

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(Config.actorTimeout seconds)

  private val logoutActor = system.actorOf(LogoutActor.props(), ActorNames.LOGOUT)

  val route: Route = {

    path(RouteConstants.logout / Segment) { token =>
      respondWithCORS {

        get {
            onSuccess(logoutActor ? Logout(token)) {
              case status: Boolean if status => complete("OK")
              case status: Boolean if !status => complete(requestErrorResponse(errorType = "LogoutError", errorMessage = "logout failed"))
              case _ => complete(serverErrorResponse(errorType = "LogoutError", errorMessage = "logout failed"))
            }
        }

      }
    }

  }

}
