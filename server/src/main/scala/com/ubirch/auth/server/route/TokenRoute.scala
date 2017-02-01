package com.ubirch.auth.server.route

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.TokenActor
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.model.token.AfterLogin
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
  * since: 2017-01-31
  */
trait TokenRoute extends MyJsonProtocol
  with CORSDirective
  with ResponseUtil {

  implicit val system = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  // TODO extract anything performance related to config
  private val tokenActor = system.actorOf(new RoundRobinPool(3).props(Props[TokenActor]), ActorNames.TOKEN)

  val route: Route = {

    path(RouteConstants.token / RouteConstants.verify) {
      respondWithCORS {

        post {
          entity(as[AfterLogin]) { afterLogin =>
            onSuccess(tokenActor ? afterLogin) {
              case res: AfterLogin => complete(afterLogin)
              case _ => complete(serverErrorResponse(errorType = "VerificationError", errorMessage = "failed to verify code"))
            }
          }
        }

      }
    }

  }

}
