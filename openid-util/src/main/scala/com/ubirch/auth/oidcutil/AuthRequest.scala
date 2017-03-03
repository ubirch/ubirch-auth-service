package com.ubirch.auth.oidcutil

import java.net.{URI, URL, URLDecoder}

import com.nimbusds.oauth2.sdk.id.{ClientID, State}
import com.nimbusds.oauth2.sdk.{ResponseType, Scope}
import com.nimbusds.openid.connect.sdk.AuthenticationRequest

import com.ubirch.auth.config.{Config, OidcProviderConfig}

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object AuthRequest {

  def create(context: String, providerConf: OidcProviderConfig): AuthenticationRequest = {

    val contextProviderConfig = Config.oidcContextProviderConfig(context = context, provider = providerConf.id)
    val clientID = new ClientID(contextProviderConfig.clientId)
    val callback: URI = contextProviderConfig.callbackUrl
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

  def redirectUrl(context: String, providerConf: OidcProviderConfig): (String, State) = {

    val authReq = create(context, providerConf)
    authReq.toHTTPRequest.send()
    val redirectHost = URLDecoder.decode(authReq.toHTTPRequest.getURL.toString, "UTF-8")
    val redirectParams = authReq.toHTTPRequest().getQuery

    (s"$redirectHost?$redirectParams", authReq.getState)

  }

}
