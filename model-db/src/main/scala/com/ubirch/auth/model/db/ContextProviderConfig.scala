package com.ubirch.auth.model.db

import java.net.URI

/**
  * author: cvandrei
  * since: 2017-03-13
  */
case class ContextProviderConfig(context: String,
                                 appId: String = "legacy", // TODO remove default once /provider/info/list/$CONTEXT has been removed
                                 provider: String,
                                 clientId: String,
                                 clientSecret: String,
                                 callbackUrl: URI
                                )
