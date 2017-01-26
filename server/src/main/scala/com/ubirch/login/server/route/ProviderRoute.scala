package com.ubirch.login.server.route

import com.ubirch.login.config.Config
import com.ubirch.login.core.actor.util.ActorNames
import com.ubirch.login.core.actor.{ProviderInfoActor, ProviderInfoList}
import com.ubirch.login.model.provider.ProviderInfo
import com.ubirch.login.util.server.RouteConstants
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
  * since: 2017-01-26
  */
trait ProviderRoute extends MyJsonProtocol
  with CORSDirective
  with ResponseUtil {

  implicit val system = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  // TODO extract anything performance related to config
  private val providerInfoActor = system.actorOf(new RoundRobinPool(3).props(Props[ProviderInfoActor]), ActorNames.PROVIDER_INFO)

  val route: Route = {

    path(RouteConstants.providerInfo / RouteConstants.list) {
      respondWithCORS {

        get {
          onSuccess(providerInfoActor ? ProviderInfoList()) {
            case seq: Seq[ProviderInfo] => complete(seq)
            case _ => complete(serverErrorResponse(errorType = "QueryError", errorMessage = "failed to query provider info list"))
          }
        }

      }
    }

  }

}
