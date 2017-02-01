package com.ubirch.auth.core.manager

import com.ubirch.auth.model.token.{AfterLogin, Token}

import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object TokenManager {

  def verifyCode(afterLogin: AfterLogin): Future[Token] = {
    // TODO verify that "afterLogin.state" matches the one from when we created the redirectUrl
    // TODO verify "afterLogin.code" w/ OpenID Connect provider
    Future(Token(s"${afterLogin.code}-${afterLogin.state}"))
  }

}
