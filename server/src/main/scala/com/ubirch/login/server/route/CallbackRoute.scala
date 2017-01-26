package com.ubirch.login.server.route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

/**
  * author: cvandrei
  * since: 2017-01-19
  */
trait CallbackRoute {

  val route: Route = {

    path("callback") {
      get {
        complete("OK")
      }
    }

  }

}
