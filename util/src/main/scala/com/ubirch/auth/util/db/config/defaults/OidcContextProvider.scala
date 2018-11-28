package com.ubirch.auth.util.db.config.defaults

import java.net.URI

import com.ubirch.auth.model.db.ContextProviderConfig

/**
  * author: cvandrei
  * since: 2017-03-14
  */
object OidcContextProvider {

  final val contextProviderList: Seq[ContextProviderConfig] = Seq(

    // local
    ubirchAdminUILocalGoogle,
    ubirchAdminUILocalKeycloak,
    trackleUILocalGoogle,

    // ubirch-dev
    ubirchAdminUIDevGoogle,
    ubirchAdminUIDevKeycloak,
    trackleAdminUIDevGoogle,
    trackleUIDevGoogle,

    // demo
    ubirchAdminUIDemoGoogle,
    ubirchAdminUIDemoKeycloak,
    trackleAdminUIDemoGoogle,
    trackleUIDemoGoogle,

    // prod
    ubirchAdminUIProdGoogle,
    ubirchAdminUIProdKeycloak,
    trackleAdminUIProdGoogle

  )

  /*
   * *-local
   ***********************************************************************/

  private lazy val ubirchAdminUILocalGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchLocal,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-q7dccmh0leq20rqgs2550vp0u67pj42p.apps.googleusercontent.com",
    clientSecret = "BFmveucyPZ9Ijt31UvYocrj4",
    callbackUrl = new URI("http://localhost:9000/auth?providerId=google")
  )

  private lazy val ubirchAdminUILocalKeycloak: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchLocal,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdKeycloak,
    clientId = "ubirch-admin-ui-local",
    clientSecret = "e5c4baa6-2fa4-4c4f-8879-0eb83d4603c8",
    callbackUrl = new URI("http://localhost:9000/auth?providerId=keycloak")
  )

  private lazy val trackleUILocalGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleLocal,
    appId = AppIds.ui,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-dqhiaemv68bjvtnp84beg26plrpkmc8t.apps.googleusercontent.com",
    clientSecret = "n3fPuja818436VmggJZSht6-",
    callbackUrl = new URI("http://localhost:9100/auth?providerId=google")
  )

  /*
   * *-dev
   ***********************************************************************/

  private lazy val ubirchAdminUIDevGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchDev,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6rk357g8229khqp5g2ea3qu1hdesfq4p.apps.googleusercontent.com",
    clientSecret = "MV0WoHigItLlgRl1fS7ILP_g",
    callbackUrl = new URI("https://ubirch.dev.ubirch.com/auth?providerId=google")
  )

  private lazy val ubirchAdminUIDevKeycloak: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchDev,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdKeycloak,
    clientId = "ubirch-admin-ui-dev",
    clientSecret = "6b13fa01-f326-4715-82a5-077e361b1167",
    callbackUrl = new URI("https://ubirch.dev.ubirch.com/auth?providerId=keycloak")
  )

  private lazy val trackleAdminUIDevGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleDev,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-b639g31cl7u2ngtbqjibnsgqpsk8c55u.apps.googleusercontent.com",
    clientSecret = "qmbfxFXxgvBu6BIXieKut2m7",
    callbackUrl = new URI("http://ubirch.trackle.dev.ubirch.com/auth?providerId=google")
  )

  private lazy val trackleUIDevGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleDev,
    appId = AppIds.ui,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-rehhv8bdepdj3u8t4b89ta6udrglalj2.apps.googleusercontent.com",
    clientSecret = "RFBUQugBYmUcRYHnr9053dUJ",
    callbackUrl = new URI("http://trackle.dev.ubirch.com/auth?providerId=google")
  )

  /*
   * *-demo
   ***********************************************************************/

  private lazy val ubirchAdminUIDemoGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchDemo,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6iu7qbha7f5oo8d12tsteh5crsj53m9k.apps.googleusercontent.com",
    clientSecret = "utYeF1TtkeMDidNRBzOWivF0",
    callbackUrl = new URI("https://ubirch.demo.ubirch.com/auth?providerId=google")
  )

  private lazy val ubirchAdminUIDemoKeycloak: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchDemo,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdKeycloak,
    clientId = "ubirch-admin-ui-demo",
    clientSecret = "185b550c-220f-4acb-b024-8eaaef2a1c8f",
    callbackUrl = new URI("https://ubirch.demo.ubirch.com/auth?providerId=keycloak")
  )

  private lazy val trackleAdminUIDemoGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleDemo,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-vm8rp0ctbd7ufdt21qr60eu74saqab6a.apps.googleusercontent.com",
    clientSecret = "-PcHeTlPgOi4hss0ZLwzdAmX",
    callbackUrl = new URI("http://ubirch.trackle.demo.ubirch.com/auth?providerId=google")
  )

  private lazy val trackleUIDemoGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleDemo,
    appId = AppIds.ui,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-grferj4uljekn41j9tdbshgiqfd52q93.apps.googleusercontent.com",
    clientSecret = "kTGqW9-p3uhKfe7L9kMblGhv",
    callbackUrl = new URI("http://trackle.demo.ubirch.com/auth?providerId=google")
  )


  /*
   * *-prod
   ***********************************************************************/

  private lazy val ubirchAdminUIProdGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchProd,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-aetk3uvbgp9erra7ljf3i9n0du8rhjfp.apps.googleusercontent.com",
    clientSecret = "N93u2VuRWqW47SJu0fmaepHD",
    callbackUrl = new URI("https://ubirch.prod.ubirch.com/auth?providerId=google")
  )

  private lazy val ubirchAdminUIProdKeycloak: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchProd,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdKeycloak,
    clientId = "ubirch-admin-ui-prod",
    clientSecret = "dbc47b13-0d44-4328-bf34-1ae1299a4a78",
    callbackUrl = new URI("https://ubirch.prod.ubirch.com/auth?providerId=keycloak")
  )

  private lazy val trackleAdminUIProdGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleProd,
    appId = AppIds.adminUi,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-svjcpckodfa7q6f4uctjp3ttgfqdhetn.apps.googleusercontent.com",
    clientSecret = "hJCM_XzCBXOckNd66OHBBC4B",
    callbackUrl = new URI("http://ubirch.trackle.prod.ubirch.com/auth?providerId=google")
  )
}

object ContextDefinitions {

  /*
   * *-local
   *********************************************************************/

  final val ubirchLocal = "ubirch-local"

  final val trackleLocal = "trackle-local"

  /*
   * *-dev
   *********************************************************************/

  final val ubirchDev = "ubirch-dev"

  final val trackleDev = "trackle-dev"

  /*
   * *-demo
   *********************************************************************/

  final val ubirchDemo = "ubirch-demo"

  final val trackleDemo = "trackle-demo"

  /*
   * *-demo
   *********************************************************************/

  final val ubirchProd = "ubirch-prod"

  final val trackleProd = "trackle-prod"
}

object AppIds {

  final val adminUi = "admin-ui"

  final val ui = "ui"

}
