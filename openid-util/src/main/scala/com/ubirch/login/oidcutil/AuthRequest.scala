package com.ubirch.login.oidcutil

import java.net.{URI, URL}

import com.nimbusds.oauth2.sdk.id.{ClientID, State}
import com.nimbusds.oauth2.sdk.{ResponseType, Scope}
import com.nimbusds.openid.connect.sdk.{AuthenticationRequest, Nonce}

import com.ubirch.login.config.Config

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object AuthRequest {

  def create(provider: String): AuthenticationRequest = {

    val clientID = new ClientID(Config.oidcProviderClientId(provider))
    val callback: URI = new URL(Config.oidcProviderCallbackUrl(provider)).toURI
    val state = new State()
    val nonce = new Nonce()

    new AuthenticationRequest(
      new URL(Config.oidcProviderAuthorizationEndpoint(provider)).toURI,
      new ResponseType(ResponseType.Value.CODE),
      Scope.parse(Config.oidcProviderScope(provider)),
      clientID,
      callback,
      state,
      nonce
    )

  }

  def redirectUrl(provider: String): String = {

    val authReq = create(provider)
    authReq.toHTTPRequest.send()
    val redirectHostUrl = authReq.toHTTPRequest.getURL.toString
    val redirectParams = authReq.toHTTPRequest().getQuery

    s"$redirectHostUrl?$redirectParams"

  }

}
