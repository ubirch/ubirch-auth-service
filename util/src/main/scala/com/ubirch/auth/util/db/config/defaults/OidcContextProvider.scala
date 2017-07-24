package com.ubirch.auth.util.db.config.defaults

import java.net.URI

import com.ubirch.auth.model.db.ContextProviderConfig

/**
  * author: cvandrei
  * since: 2017-03-14
  */
object OidcContextProvider {

  final val activeContexts: Seq[String] = Seq(
    // local
    ContextDefinitions.ctxTrackleLocal,
    ContextDefinitions.ctxUbirchAdminUILocal,
    ContextDefinitions.ctxTrackleAdminUILocal,
    // dev
    ContextDefinitions.ctxUbirchAdminUIDev,
    ContextDefinitions.ctxTrackleAdminUIDev,
    // demo
    ContextDefinitions.ctxUbirchAdminUIDemo,
    ContextDefinitions.ctxTrackleAdminUIDemo

  )

  final val contextProviderList: Seq[ContextProviderConfig] = Seq(
    // local
    trackleLocalGoogle, trackleLocalYahoo,
    ubirchAdminUILocalGoogle,
    trackleAdminUILocalGoogle,
    // dev
    ubirchAdminUIDevGoogle,
    trackleAdminUIDevGoogle,
    // demo
    ubirchAdminUIDemoGoogle,
    trackleAdminUIDemoGoogle,
  )

  /*
   * *-local
   ***********************************************************************/

  private lazy val trackleLocalGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxTrackleLocal,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-kqf5hu698s4sodrvv03ka3bule530rp5.apps.googleusercontent.com",
    clientSecret = "M86oj4LxV-CcEDd3ougKSbsV",
    callbackUrl = new URI("https://localhost:10000/oidc-callback-google")
  )

  private lazy val trackleLocalYahoo: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxTrackleLocal,
    provider = OidcProviders.providerIdYahoo,
    clientId = "dj0yJmk9eWdKUGRJM01KclhqJmQ9WVdrOVlrMUVSRTF3TlRBbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1mMA--",
    clientSecret = "069dd28f2144ed043dcb70e27f99e424369c3040",
    callbackUrl = new URI("https://example.com/oidc-callback-yahoo")
  )

  private lazy val ubirchAdminUILocalGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxUbirchAdminUILocal,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-q7dccmh0leq20rqgs2550vp0u67pj42p.apps.googleusercontent.com",
    clientSecret = "BFmveucyPZ9Ijt31UvYocrj4",
    callbackUrl = new URI("http://localhost:9000/auth?providerId=google")
  )

  private lazy val trackleAdminUILocalGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxTrackleAdminUILocal,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-dqhiaemv68bjvtnp84beg26plrpkmc8t.apps.googleusercontent.com",
    clientSecret = "n3fPuja818436VmggJZSht6-",
    callbackUrl = new URI("http://localhost:9100/auth?providerId=google")
  )

  /*
   * *-dev
   ***********************************************************************/

  private lazy val ubirchAdminUIDevGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxUbirchAdminUIDev,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6rk357g8229khqp5g2ea3qu1hdesfq4p.apps.googleusercontent.com",
    clientSecret = "MV0WoHigItLlgRl1fS7ILP_g",
    callbackUrl = new URI("http://ubirch.dev.ubirch.com/auth?providerId=google")
  )

  private lazy val trackleAdminUIDevGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxTrackleAdminUIDev,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6rk357g8229khqp5g2ea3qu1hdesfq4p.apps.googleusercontent.com",
    clientSecret = "", // TODO set client secret
    callbackUrl = new URI("http://ubirch.trackle.dev.ubirch.com/auth?providerId=google")
  )

  /*
   * *-demo
   ***********************************************************************/

  private lazy val ubirchAdminUIDemoGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxUbirchAdminUIDemo,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6iu7qbha7f5oo8d12tsteh5crsj53m9k.apps.googleusercontent.com",
    clientSecret = "utYeF1TtkeMDidNRBzOWivF0",
    callbackUrl = new URI("http://ubirch.demo.ubirch.com/auth?providerId=google")
  )

  private lazy val trackleAdminUIDemoGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxTrackleAdminUIDemo,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6iu7qbha7f5oo8d12tsteh5crsj53m9k.apps.googleusercontent.com",
    clientSecret = "", // TODO set client secret
    callbackUrl = new URI("http://ubirch.trackle.demo.ubirch.com/auth?providerId=google")
  )

}

object ContextDefinitions {

  /*
   * *-local
   *********************************************************************/

  final val ctxTrackleAdminUILocal = "trackle-admin-ui-local"

  final val ctxTrackleLocal = "trackle-local"

  final val ctxUbirchAdminUILocal = "ubirch-admin-ui-local"

  /*
   * *-dev
   *********************************************************************/

  final val ctxUbirchAdminUIDev = "ubirch-admin-ui-dev"

  final val ctxTrackleAdminUIDev = "trackle-admin-ui-dev"

  /*
   * *-demo
   *********************************************************************/

  final val ctxUbirchAdminUIDemo = "ubirch-admin-ui-demo"

  final val ctxTrackleAdminUIDemo = "trackle-admin-ui-demo"

}