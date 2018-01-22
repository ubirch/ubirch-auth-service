package com.ubirch.auth.client.rest.config

import com.ubirch.auth.util.server.RouteConstants
import com.ubirch.util.config.ConfigBase

/**
  * author: cvandrei
  * since: 2018-01-22
  */
object AuthClientRestConfig extends ConfigBase {

  /**
    * The host the REST API runs on.
    *
    * @return host
    */
  private def host = config.getString(AuthClientRestConfigKeys.HOST)

  val urlCheck = s"$host${RouteConstants.pathCheck}"

  val urlDeepCheck = s"$host${RouteConstants.pathDeepCheck}"

  val urlRegister = s"$host${RouteConstants.pathRegister}"

}
