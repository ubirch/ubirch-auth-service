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

  def create: AuthenticationRequest = {

    // The client identifier provisioned by the server
    val clientID = new ClientID(Config.openIdConnectGenericClientId)

    // The client callback URL
    val callback: URI = new URL(Config.openIdConnectGenericCallbackUrl).toURI

    // Generate random state string for pairing the response to the request
    val state = new State()

    // Generate nonce
    val nonce = new Nonce()

    // Compose the request (in code flow)
    new AuthenticationRequest(
      new URL("https://example.com/login").toURI,
      new ResponseType(ResponseType.Value.CODE),
      Scope.parse(Config.openIdConnectGenericScope),
      clientID,
      callback,
      state,
      nonce
    )

  }

  def redirectUrl: String = {

    val authReq = create
    authReq.toHTTPRequest.send()
    val redirectHostUrl = authReq.toHTTPRequest.getURL.toString
    val redirectParams = authReq.toHTTPRequest().getQuery

    s"$redirectHostUrl?$redirectParams"

  }

}
