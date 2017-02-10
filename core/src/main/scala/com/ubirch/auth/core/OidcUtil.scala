package com.ubirch.auth.core

/**
  * author: cvandrei
  * since: 2017-02-09
  */
object OidcUtil {

  def stateToHashedKey(provider: String, state: String): String = s"state:$provider:$state"

  def tokenToHashedKey(provider: String, token: String): String = s"token:$provider:$token"

}
