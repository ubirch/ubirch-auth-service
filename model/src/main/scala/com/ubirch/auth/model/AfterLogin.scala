package com.ubirch.auth.model

/**
  * author: cvandrei
  * since: 2017-01-31
  */
case class AfterLogin(context: String,
                      appId: String = "legacy", // TODO remove default value after /provider/info/list/$LEGACY_CONTEXT has been removed
                      providerId: String,
                      code: String,
                      state: String
                     )
