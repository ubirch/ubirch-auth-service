package com.ubirch.auth.server.route

import com.ubirch.auth.config.Config
import com.ubirch.util.json.MyJsonProtocol
import com.ubirch.util.model.JsonResponse

import akka.http.scaladsl.server.Directives.{complete, get}
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._

/**
  * author: cvandrei
  * since: 2017-01-26
  */
trait WelcomeRoute extends MyJsonProtocol {

  val route: Route = {

    get {
      complete {

        val goInfo = s"${Config.goPipelineName} / ${Config.goPipelineLabel} / ${Config.goPipelineRevision}"
        JsonResponse(message = s"Welcome to the ubirchAuthService ( $goInfo )")

      }
    }

  }

}
