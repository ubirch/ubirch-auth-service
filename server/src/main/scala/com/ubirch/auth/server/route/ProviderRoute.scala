package com.ubirch.auth.server.route

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.core.actor.util.ActorNames
import com.ubirch.auth.core.actor.{GetProviderInfoList, ProviderInfoActor, ProviderInfoList}
import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.http.response.ResponseUtil
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
import scala.util.{Failure, Success}

/**
  * author: cvandrei
  * since: 2017-01-26
  */
trait ProviderRoute extends ResponseUtil
  with CORSDirective
  with StrictLogging {

  implicit val system = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout = Timeout(Config.actorTimeout seconds)

  private val providerInfoActor = system.actorOf(new RoundRobinPool(Config.akkaNumberOfWorkers).props(Props[ProviderInfoActor]), ActorNames.PROVIDER_INFO)

  val route: Route = {

    path(RouteConstants.providerInfo / RouteConstants.list / Segment) { context =>
      respondWithCORS {

        get {
          onComplete(providerInfoActor ? GetProviderInfoList(context)) {

            case Failure(t) =>
              logger.error("verify code call responded with an unhandled message (check TokenRoute for bugs!!!)", t)
              complete(serverErrorResponse(errorType = "ServerError", errorMessage = "sorry, something went wrong on our end"))

            case Success(resp) =>

              resp match {
                case providerInfos: ProviderInfoList => complete(providerInfos.seq)
                case _ => complete(serverErrorResponse(errorType = "QueryError", errorMessage = "failed to query provider info list"))
              }

          }
        }

      }
    }

  }

}
