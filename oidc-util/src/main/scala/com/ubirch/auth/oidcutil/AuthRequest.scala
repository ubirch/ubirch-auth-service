package com.ubirch.auth.oidcutil

import java.net.{URI, URL, URLDecoder}

import com.nimbusds.oauth2.sdk.id.{ClientID, State}
import com.nimbusds.oauth2.sdk.{ResponseType, Scope}
import com.nimbusds.openid.connect.sdk.AuthenticationRequest

import com.ubirch.auth.model.db.{ContextProviderConfig, OidcProviderConfig}

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object AuthRequest {

  def redirectUrl(contextProviderConf: ContextProviderConfig, providerConf: OidcProviderConfig): (String, State) = {

    val authReq = create(contextProviderConf, providerConf)
    val authReqHttp = authReq.toHTTPRequest
    val redirectHost = URLDecoder.decode(authReqHttp.getURL.toString, "UTF-8")
    val redirectParams = authReqHttp.getQuery

    (s"$redirectHost?$redirectParams", authReq.getState)

  }

  def create(contextProviderConf: ContextProviderConfig, providerConf: OidcProviderConfig): AuthenticationRequest = {

    val clientID = new ClientID(contextProviderConf.clientId)
    val callback: URI = contextProviderConf.callbackUrl
    val state = new State()
    val nonce = null

    new AuthenticationRequest(
      new URL(providerConf.endpoints.authorization).toURI,
      new ResponseType(ResponseType.Value.CODE),
      Scope.parse(providerConf.scope),
      clientID,
      callback,
      state,
      nonce
    )

  }

}
