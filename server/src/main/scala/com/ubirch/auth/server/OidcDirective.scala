package com.ubirch.auth.server

import akka.http.scaladsl.server.{Directives, Route}

/**
  * author: cvandrei
  * since: 2017-03-17
  */
trait OidcDirective extends Directives {

  def verifyToken(routes: => Route) = {
    val token: String = "" // TODO extract token from "Authorization" header
    // TODO extract context header
    // TODO extract provider header
    // TODO check if token exists in Redis
    // TODO update token's expiry date
  }

}
