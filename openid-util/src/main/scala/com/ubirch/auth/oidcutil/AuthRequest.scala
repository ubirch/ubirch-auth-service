package com.ubirch.auth.oidcutil

import java.net.{URI, URL, URLDecoder}

import com.nimbusds.oauth2.sdk.id.{ClientID, State}
import com.nimbusds.oauth2.sdk.{ResponseType, Scope}
import com.nimbusds.openid.connect.sdk.AuthenticationRequest

import com.ubirch.auth.config.{ContextProviderConfig, OidcProviderConfig}

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object AuthRequest {

  def redirectUrl(contextProviderConf: ContextProviderConfig, providerConf: OidcProviderConfig): (String, State) = {

    val authReq = create(contextProviderConf, providerConf)
    authReq.toHTTPRequest.send()
    val redirectHost = URLDecoder.decode(authReq.toHTTPRequest.getURL.toString, "UTF-8")
    val redirectParams = authReq.toHTTPRequest().getQuery

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
