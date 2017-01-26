package com.ubirch.login.config

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object ConfigKeys {

  private final val prefix = "ubirchLoginService"

  final val INTERFACE = s"$prefix.interface"
  final val PORT = s"$prefix.port"
  final val TIMEOUT = s"$prefix.timeout"

  private val openIdConnectPrefix = s"$prefix.openid-connect-generic"
  final val OPENID_CONNECT_GENERIC_CLIENT_ID = s"$openIdConnectPrefix.client-id"
  final val OPENID_CONNECT_GENERIC_CALLBACK_URL = s"$openIdConnectPrefix.callback-url"
  final val OPENID_CONNECT_GENERIC_SCOPE = s"$openIdConnectPrefix.scope"

}
