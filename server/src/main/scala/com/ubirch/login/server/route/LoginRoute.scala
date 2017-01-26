package com.ubirch.login.server.route

import com.ubirch.login.oidcutil.AuthRequest

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

/**
  * author: cvandrei
  * since: 2017-01-19
  */
trait LoginRoute {

  val route: Route = {

    path("login") {
      get {

        // TODO remember state for later since we should check the state
        val redirectUrl = AuthRequest.redirectUrl
        redirect(redirectUrl, StatusCodes.PermanentRedirect)

      }
    }

  }

}
