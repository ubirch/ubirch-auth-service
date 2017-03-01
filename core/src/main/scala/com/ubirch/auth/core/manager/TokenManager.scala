package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.oidcutil.{TokenUserId, TokenUtil}

import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object TokenManager extends StrictLogging {

  def verifyCodeWith3rdParty(context: String, code: String): Option[TokenUserId] = {

    TokenUtil.requestToken(context = context, authCode = code) match {
      case None => None
      case Some(tokenResponse) => Some(TokenUserId(tokenResponse.token, tokenResponse.userId))
    }

  }

}
