package com.ubirch.auth.model

/**
  * author: cvandrei
  * since: 2017-01-26
  */
case class ProviderInfo(context: String,
                        appId: String,
                        providerId: String,
                        name: String,
                        redirectUrl: String
                       )
