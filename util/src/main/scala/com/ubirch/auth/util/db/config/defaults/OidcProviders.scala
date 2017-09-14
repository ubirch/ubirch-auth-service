package com.ubirch.auth.util.db.config.defaults

import com.ubirch.auth.model.db.{OidcProviderConfig, OidcProviderEndpoints}

/**
  * author: cvandrei
  * since: 2017-03-14
  */
object OidcProviders {

  final val providerIdGoogle: String = "google"
  final val providerIdYahoo: String = "yahoo"
  final val providerIdKeycloak: String = "keycloak"

  final val providers: Seq[OidcProviderConfig] = Seq(google, yahoo, keycloak)

  private lazy val google = OidcProviderConfig(
    id = providerIdGoogle,
    name = "Google",
    scope = "openid profile",
    endpointConfig = "https://accounts.google.com/.well-known/openid-configuration",
    tokenSigningAlgorithms = Seq("RS256"),
    endpoints = OidcProviderEndpoints(
      authorization = "https://accounts.google.com/o/oauth2/v2/auth",
      token = "https://www.googleapis.com/oauth2/v4/token",
      jwks = "https://www.googleapis.com/oauth2/v3/certs"
    )
  )

  private lazy val yahoo = OidcProviderConfig(
    id = providerIdYahoo,
    name = "Yahoo",
    scope = "openid profile",
    endpointConfig = "https://login.yahoo.com/.well-known/openid-configuration",
    tokenSigningAlgorithms = Seq("RS256", "ES256"),
    endpoints = OidcProviderEndpoints(
      authorization = "https://api.login.yahoo.com/oauth2/request_auth",
      token = "https://api.login.yahoo.com/oauth2/get_token",
      jwks = "https://login.yahoo.com/openid/v1/certs"
    )
  )

  private lazy val keycloak = OidcProviderConfig(
    id = providerIdKeycloak,
    name = "Keycloak",
    scope = "openid profile",
    endpointConfig = "https://id.dev.ubirch.com/auth/realms/ubirch/.well-known/openid-configuration",
    tokenSigningAlgorithms = Seq("RS256"),
    endpoints = OidcProviderEndpoints(
      authorization = "https://id.dev.ubirch.com/auth/realms/ubirch/protocol/openid-connect/auth",
      token = "https://id.dev.ubirch.com/auth/realms/ubirch/protocol/openid-connect/token",
      jwks = "https://id.dev.ubirch.com/auth/realms/ubirch/protocol/openid-connect/certs"
    )
  )

}
