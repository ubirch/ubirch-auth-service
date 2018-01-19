package com.ubirch.auth.server.route

import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.mongo.connection.MongoUtil

import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer

/**
  * author: cvandrei
  * since: 2017-01-19
  */
class MainRoute(implicit mongo: MongoUtil,
                httpClient: HttpExt,
                materializer: Materializer
               ) {

  val welcome = new WelcomeRoute {}
  val deepCheck = new DeepCheckRoute {}
  val provider = new ProviderRoute {}
  val token = new TokenRoute {}
  val logout = new LogoutRoute {}
  val register = new RegisterRoute {}
  val userInfo = new UserInfoRoute() {}

  val myRoute: Route = {

    pathPrefix(RouteConstants.apiPrefix) {
      pathPrefix(RouteConstants.serviceName) {
        pathPrefix(RouteConstants.currentVersion) {

          provider.route ~
            token.route ~
            logout.route ~
            register.route ~
            userInfo.route ~
            deepCheck.route ~
            path(RouteConstants.check) {
              welcome.route
            } ~ pathEndOrSingleSlash {
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
