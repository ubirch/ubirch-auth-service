package com.ubirch.auth.model.db

import java.net.URI

/**
  * author: cvandrei
  * since: 2017-03-13
  */
case class ContextProviderConfig(context: String,
                                 provider: String,
                                 clientId: String,
                                 clientSecret: String,
                                 callbackUrl: URI
                                )
