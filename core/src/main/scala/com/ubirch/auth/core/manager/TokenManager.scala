package com.ubirch.auth.core.manager

import com.ubirch.auth.model.token.{AfterLogin, Token}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object TokenManager {

  def verifyCode(afterLogin: AfterLogin): Future[Option[Token]] = {

    // TODO verify that "afterLogin.state" matches the one from when we created the redirectUrl
    // TODO verify "afterLogin.code" w/ OpenID Connect provider
    // TODO remember token that we get from OpenID Connect provider

    Random.nextBoolean() match {
      case true => Future(Some(Token(s"${afterLogin.providerId}-${afterLogin.code}-${afterLogin.state}")))
      case false => Future(None)
    }

  }

}
