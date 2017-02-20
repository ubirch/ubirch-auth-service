package com.ubirch.auth.server.route

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.LogoutActor
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.model.Logout
import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.http.response.ResponseUtil
import com.ubirch.util.json.MyJsonProtocol
import com.ubirch.util.rest.akka.directives.CORSDirective

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-02-01
  */
trait LogoutRoute extends MyJsonProtocol
  with CORSDirective
  with ResponseUtil {

  implicit val system = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  private val logoutActor = system.actorOf(new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props[LogoutActor]), ActorNames.LOGOUT)

  val route: Route = {

    path(RouteConstants.logout) {
      respondWithCORS {

        post {
          entity(as[Logout]) { logout =>
            onSuccess(logoutActor ? logout) {
              case status: Boolean if status => complete("OK")
              case status: Boolean if !status => complete(requestErrorResponse(errorType = "LogoutError", errorMessage = "logout failed"))
              case _ => complete(serverErrorResponse(errorType = "LogoutError", errorMessage = "logout failed"))
            }
          }
        }

      }
    }

  }

}
