package com.ubirch.auth.model

/**
  * author: cvandrei
  * since: 2017-01-31
  */
case class AfterLogin(providerId: String,
                      code: String,
                      state: String
                     )
