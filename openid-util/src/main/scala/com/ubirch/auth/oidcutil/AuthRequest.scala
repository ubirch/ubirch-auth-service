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

  def create(context: String): AuthenticationRequest = {

    val provider = Config.oidcContextProviderId(context)
    val providerConf = Config.oidcProviderConfig(provider)
    val clientID = new ClientID(Config.oidcClientId(context))
    val callback: URI = new URL(Config.oidcCallbackUrl(context)).toURI
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

  def redirectUrl(context: String): (String, State) = {

    val authReq = create(context)
    authReq.toHTTPRequest.send()
    val redirectHost = URLDecoder.decode(authReq.toHTTPRequest.getURL.toString, "UTF-8")
    val redirectParams = authReq.toHTTPRequest().getQuery

    (s"$redirectHost?$redirectParams", authReq.getState)

  }

}
