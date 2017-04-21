package com.ubirch.auth.server.route

import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.mongo.connection.MongoUtil

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

/**
  * author: cvandrei
  * since: 2017-01-19
  */
class MainRoute(implicit mongo: MongoUtil) {

  val welcome = new WelcomeRoute {}
  val provider = new ProviderRoute {}
  val token = new TokenRoute {}
  val logout = new LogoutRoute {}
  val register = new RegisterRoute {}

  val myRoute: Route = {

    pathPrefix(RouteConstants.apiPrefix) {
      pathPrefix(RouteConstants.serviceName) {
        pathPrefix(RouteConstants.currentVersion) {

          provider.route ~
            token.route ~
            logout.route ~
            register.route ~
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
