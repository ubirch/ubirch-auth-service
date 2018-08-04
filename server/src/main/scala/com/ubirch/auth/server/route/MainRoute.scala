package com.ubirch.auth.server.route

import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.mongo.connection.MongoUtil

import akka.actor.ActorSystem
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer

/**
  * author: cvandrei
  * since: 2017-01-19
  */
class MainRoute(implicit mongo: MongoUtil,
                system: ActorSystem,
                httpClient: HttpExt,
                materializer: Materializer
               ) {

  private val welcome = new WelcomeRoute {}
  private val deepCheck = new DeepCheckRoute {}
  private val provider = new ProviderRoute {}
  private val token = new TokenRoute {}
  private val logout = new LogoutRoute {}
  private val register = new RegisterRoute {}
  private val userInfo = new UserInfoRoute() {}

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
