package com.ubirch.auth.oidcutil

import java.net.{URI, URL, URLDecoder}

import com.nimbusds.oauth2.sdk.id.{ClientID, State}
import com.nimbusds.oauth2.sdk.{ResponseType, Scope}
import com.nimbusds.openid.connect.sdk.AuthenticationRequest

import com.ubirch.auth.config.Config

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object AuthRequest {

  def create(provider: String): AuthenticationRequest = {

    val clientID = new ClientID(Config.oidcProviderClientId(provider))
    val callback: URI = new URL(Config.oidcProviderCallbackUrl(provider)).toURI
    val state = new State()
    val nonce = null

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

  def redirectUrl(provider: String): (String, State) = {

    val authReq = create(provider)
    authReq.toHTTPRequest.send()
    val redirectHost = URLDecoder.decode(authReq.toHTTPRequest.getURL.toString, "UTF-8")
    val redirectParams = authReq.toHTTPRequest().getQuery

    (s"$redirectHost?$redirectParams", authReq.getState)

  }

}
