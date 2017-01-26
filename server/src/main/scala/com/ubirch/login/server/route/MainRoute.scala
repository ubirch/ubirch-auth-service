package com.ubirch.login.server.route

import com.ubirch.login.util.server.RouteConstants

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

/**
  * author: cvandrei
  * since: 2017-01-19
  */
class MainRoute {

  val login = new LoginRoute {}
  val callback = new CallbackRoute {}
  val welcome = new WelcomeRoute {}
  val provider = new ProviderRoute {}

  val myRoute: Route = {

    pathPrefix(RouteConstants.apiPrefix) {
      pathPrefix(RouteConstants.serviceName) {
        pathPrefix(RouteConstants.currentVersion) {

          login.route ~
            callback.route ~
            pathEndOrSingleSlash {
              welcome.route
            } ~ provider.route

        }
      }
    } ~
      pathSingleSlash {
        welcome.route
      }

  }

}
