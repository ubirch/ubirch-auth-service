package com.ubirch.login.server.route

import com.ubirch.login.util.server.RouteConstants

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

/**
  * author: cvandrei
  * since: 2017-01-19
  */
class MainRoute {

  val welcome = new WelcomeRoute {}
  val provider = new ProviderRoute {}

  val myRoute: Route = {

    pathPrefix(RouteConstants.apiPrefix) {
      pathPrefix(RouteConstants.serviceName) {
        pathPrefix(RouteConstants.currentVersion) {

          provider.route ~
            pathEndOrSingleSlash {
              welcome.route
            }

        }
      }
    } ~
      pathSingleSlash {
        welcome.route
      }

  }

}
