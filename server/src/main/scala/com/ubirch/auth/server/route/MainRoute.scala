package com.ubirch.auth.server.route

import com.ubirch.auth.util.server.RouteConstants

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

/**
  * author: cvandrei
  * since: 2017-01-19
  */
class MainRoute {

  val welcome = new WelcomeRoute {}
  val provider = new ProviderRoute {}
  val token = new TokenRoute {}
  val logout = new LogoutRoute {}

  val myRoute: Route = {

    pathPrefix(RouteConstants.apiPrefix) {
      pathPrefix(RouteConstants.serviceName) {
        pathPrefix(RouteConstants.currentVersion) {

          provider.route ~
            token.route ~
            logout.route ~
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
