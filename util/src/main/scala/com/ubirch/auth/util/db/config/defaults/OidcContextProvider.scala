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
    ContextDefinitions.ctxTrackleUILocal,
    ContextDefinitions.ctxUbirchAdminUILocal,
    // dev
    ContextDefinitions.ctxUbirchAdminUIDev,
    ContextDefinitions.ctxTrackleAdminUIDev,
    ContextDefinitions.ctxTrackleUIDev,
    // demo
    ContextDefinitions.ctxUbirchAdminUIDemo,
    ContextDefinitions.ctxTrackleAdminUIDemo,
    ContextDefinitions.ctxTrackleUIDemo
  )

  final val contextProviderList: Seq[ContextProviderConfig] = Seq(
    // local
    ubirchAdminUILocalGoogle,
    trackleUILocalGoogle,
    // ubirch-dev
    ubirchAdminUIDevGoogle,
    trackleAdminUIDevGoogle,
    trackleUIDevGoogle,
    // demo
    ubirchAdminUIDemoGoogle,
    trackleAdminUIDemoGoogle,
    trackleAdminUIDemoGoogle
  )

  /*
   * *-local
   ***********************************************************************/

  private lazy val ubirchAdminUILocalGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxUbirchAdminUILocal,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-q7dccmh0leq20rqgs2550vp0u67pj42p.apps.googleusercontent.com",
    clientSecret = "BFmveucyPZ9Ijt31UvYocrj4",
    callbackUrl = new URI("http://localhost:9000/auth?providerId=google")
  )

  private lazy val trackleUILocalGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxTrackleUILocal,
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
    context = ContextDefinitions.ctxTrackleAdminUIDemo,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6iu7qbha7f5oo8d12tsteh5crsj53m9k.apps.googleusercontent.com",
    clientSecret = "", // TODO set client secret
    callbackUrl = new URI("http://ubirch.trackle.dev.ubirch.com/auth?providerId=google")
  )

  private lazy val trackleUIDevGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxTrackleUIDev,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6iu7qbha7f5oo8d12tsteh5crsj53m9k.apps.googleusercontent.com",
    clientSecret = "", // TODO set client secret
    callbackUrl = new URI("http://trackle.dev.ubirch.com/auth?providerId=google")
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

  private lazy val trackleUIDemoGoogle: ContextProviderConfig = ContextProviderConfig(
    context = ContextDefinitions.ctxTrackleUIDemo,
    provider = OidcProviders.providerIdGoogle,
    clientId = "370115332091-6iu7qbha7f5oo8d12tsteh5crsj53m9k.apps.googleusercontent.com",
    clientSecret = "", // TODO set client secret
    callbackUrl = new URI("http://trackle.demo.ubirch.com/auth?providerId=google")
  )

}

object ContextDefinitions {

  /*
   * *-local
   *********************************************************************/

  final val ctxUbirchAdminUILocal = "ubirch-admin-ui-local"

  final val ctxTrackleAdminUILocal = "trackle-admin-ui-local"

  final val ctxTrackleUILocal = "trackle-ui-local"

  /*
   * *-dev
   *********************************************************************/

  final val ctxUbirchAdminUIDev = "ubirch-admin-ui-dev"

  final val ctxTrackleAdminUIDev = "trackle-admin-ui-dev"

  final val ctxTrackleUIDev = "trackle-ui-dev"

  /*
   * *-demo
   *********************************************************************/

  final val ctxUbirchAdminUIDemo = "ubirch-admin-ui-demo"

  final val ctxTrackleAdminUIDemo = "trackle-admin-ui-demo"

  final val ctxTrackleUIDemo = "trackle-ui-demo"

}