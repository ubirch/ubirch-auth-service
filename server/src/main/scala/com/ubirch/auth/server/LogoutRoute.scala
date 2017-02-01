package com.ubirch.auth.server

import com.ubirch.auth.config.Config
import com.ubirch.auth.model.Logout
import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.http.response.ResponseUtil
import com.ubirch.util.json.MyJsonProtocol
import com.ubirch.util.rest.akka.directives.CORSDirective

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.util.Timeout

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

  val route: Route = {

    path(RouteConstants.logout) {
      respondWithCORS {

        post {
          entity(as[Logout]) { logout =>
            complete("OK") // TODO notify actor
          }
        }

      }
    }

  }

}
