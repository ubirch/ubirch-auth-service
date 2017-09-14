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
    ubirchAdminUILocalGoogle_Deprecated,
    trackleUILocalGoogle_Deprecated,

    // ubirch-dev
    ubirchAdminUIDevGoogle,
    trackleAdminUIDevGoogle,
    trackleUIDevGoogle,
    ubirchAdminUIDevGoogle_Deprecated,
    trackleAdminUIDevGoogle_Deprecated,
    trackleUIDevGoogle_Deprecated,

    // demo
    ubirchAdminUIDemoGoogle,
    trackleAdminUIDemoGoogle,
    trackleUIDemoGoogle,
    ubirchAdminUIDemoGoogle_Deprecated,
    trackleAdminUIDemoGoogle_Deprecated,
    trackleUIDemoGoogle_Deprecated

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

  private lazy val ubirchAdminUILocalGoogle_Deprecated: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchAdminUILocal,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-q7dccmh0leq20rqgs2550vp0u67pj42p.apps.googleusercontent.com",
    clientSecret = "BFmveucyPZ9Ijt31UvYocrj4",
    callbackUrl = new URI("http://localhost:9000/auth?providerId=google")
  )

  private lazy val trackleUILocalGoogle_Deprecated: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleUILocal,
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
    callbackUrl = new URI("http://ubirch.dev.ubirch.com/auth?providerId=google")
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

  private lazy val ubirchAdminUIDevGoogle_Deprecated: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchAdminUIDev,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6rk357g8229khqp5g2ea3qu1hdesfq4p.apps.googleusercontent.com",
    clientSecret = "MV0WoHigItLlgRl1fS7ILP_g",
    callbackUrl = new URI("http://ubirch.dev.ubirch.com/auth?providerId=google")
  )

  private lazy val trackleAdminUIDevGoogle_Deprecated: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleAdminUIDev,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-b639g31cl7u2ngtbqjibnsgqpsk8c55u.apps.googleusercontent.com",
    clientSecret = "qmbfxFXxgvBu6BIXieKut2m7",
    callbackUrl = new URI("http://ubirch.trackle.dev.ubirch.com/auth?providerId=google")
  )

  private lazy val trackleUIDevGoogle_Deprecated: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleUIDev,
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
    callbackUrl = new URI("http://ubirch.demo.ubirch.com/auth?providerId=google")
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

  private lazy val ubirchAdminUIDemoGoogle_Deprecated: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ubirchAdminUIDemo,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6iu7qbha7f5oo8d12tsteh5crsj53m9k.apps.googleusercontent.com",
    clientSecret = "utYeF1TtkeMDidNRBzOWivF0",
    callbackUrl = new URI("http://ubirch.demo.ubirch.com/auth?providerId=google")
  )

  private lazy val trackleAdminUIDemoGoogle_Deprecated: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleAdminUIDemo,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-vm8rp0ctbd7ufdt21qr60eu74saqab6a.apps.googleusercontent.com",
    clientSecret = "-PcHeTlPgOi4hss0ZLwzdAmX",
    callbackUrl = new URI("http://ubirch.trackle.demo.ubirch.com/auth?providerId=google")
  )

  private lazy val trackleUIDemoGoogle_Deprecated: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.trackleUIDemo,
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

  final val ubirchAdminUILocal = "ubirch-admin-ui-local"

  final val trackleAdminUILocal = "trackle-admin-ui-local"

  final val trackleUILocal = "trackle-ui-local"

  /*
   * *-dev
   *********************************************************************/

  final val ubirchDev = "ubirch-dev"

  final val trackleDev = "trackle-dev"

  final val ubirchAdminUIDev = "ubirch-admin-ui-dev"

  final val trackleAdminUIDev = "trackle-admin-ui-dev"

  final val trackleUIDev = "trackle-ui-dev"

  /*
   * *-demo
   *********************************************************************/

  final val ubirchDemo = "ubirch-demo"

  final val trackleDemo = "trackle-demo"

  final val ubirchAdminUIDemo = "ubirch-admin-ui-demo"

  final val trackleAdminUIDemo = "trackle-admin-ui-demo"

  final val trackleUIDemo = "trackle-ui-demo"

}

object AppIds {

  final val adminUi = "admin-ui"

  final val ui = "ui"

  final val legacy = "legacy"

}
