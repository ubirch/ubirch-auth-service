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
    trackleUIDemoGoogle

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
    clientSecret = "fc9bd005-1a4c-4c2f-99c3-dc3aa65c0ae1",
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
    clientSecret = "f64cf8e3-3721-4e5b-b91f-00d4b284265f",
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
    clientSecret = "b4d01ceb-49ee-4b52-a38a-0a764e713915",
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

}

object AppIds {

  final val adminUi = "admin-ui"

  final val ui = "ui"

}
