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

  val myRoute: Route = {
    pathPrefix(RouteConstants.api / RouteConstants.serviceName / RouteConstants.currentVersion) {

      login.route ~
        callback.route ~
        pathEndOrSingleSlash {
          welcome.route
        }

    } ~
      pathSingleSlash {
        welcome.route
      }
  }

}
