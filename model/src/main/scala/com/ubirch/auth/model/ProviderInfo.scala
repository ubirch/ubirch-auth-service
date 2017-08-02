package com.ubirch.auth.model

/**
  * author: cvandrei
  * since: 2017-01-26
  */
case class ProviderInfo(context: String,
                        appId: Option[String] = None, // TODO change to mandatory once /provider/info/list/$CONTEXT has been removed
                        providerId: String,
                        name: String,
                        redirectUrl: String
                       )
