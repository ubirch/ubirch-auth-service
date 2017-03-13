package com.ubirch.auth.core.manager

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.{ContextProviderConfig, OidcProviderConfig}
import com.ubirch.auth.oidcutil.{TokenUserId, TokenUtil}

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object TokenManager extends StrictLogging {

  def verifyCodeWith3rdParty(contextProvider: ContextProviderConfig,
                             providerConf: OidcProviderConfig,
                             code: String): Option[TokenUserId] = {
    TokenUtil.requestToken(contextProvider = contextProvider, providerConf = providerConf, authCode = code) match {
      case None => None
      case Some(tokenResponse) => Some(TokenUserId(tokenResponse.token, tokenResponse.userId))
    }

  }

}
