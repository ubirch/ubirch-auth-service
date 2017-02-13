package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.Random

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object TokenManager extends StrictLogging {

  def verifyCodeWith3rdParty(provider: String, code: String): Future[TokenUserId] = {

    // TODO verify "afterLogin.code" w/ OpenID Connect provider

    // TODO replace w/ correct token
    val token = s"$provider-$code-${Random.nextInt}"
    // TODO replace w/ correct userId
    val userId = s"$provider-${Random.nextInt}"

    Future(TokenUserId(token, userId))

  }

}

case class TokenUserId(token: String,
                       userId: String
                      )
