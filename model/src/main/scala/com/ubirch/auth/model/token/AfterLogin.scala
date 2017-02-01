package com.ubirch.auth.model.token

/**
  * author: cvandrei
  * since: 2017-01-31
  */
case class AfterLogin(providerId: String,
                      code: String,
                      state: String
                     )
