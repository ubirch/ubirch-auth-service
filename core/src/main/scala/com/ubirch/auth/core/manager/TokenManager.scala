package com.ubirch.auth.core.manager

import com.ubirch.auth.model.token.{AfterLogin, Token}

import scala.concurrent.Future

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object TokenManager {

  def verifyCode(afterLogin: AfterLogin): Future[Token] = {
    Future(Token(s"${afterLogin.code}-${afterLogin.state}"))
  }

}
