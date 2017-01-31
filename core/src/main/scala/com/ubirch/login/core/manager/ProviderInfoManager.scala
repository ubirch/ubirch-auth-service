package com.ubirch.login.core.manager

import com.ubirch.login.config.Config
import com.ubirch.login.model.provider.ProviderInfo
import com.ubirch.login.oidcutil.AuthRequest

/**
  * author: cvandrei
  * since: 2017-01-26
  */
object ProviderInfoManager {

  def providerInfoList(): Seq[ProviderInfo] = {

    // TODO remember states (as we need them to verify the token later)
    Config.oidcProviders map { provider =>
      val redirectUrl = AuthRequest.redirectUrl(provider)
      ProviderInfo(
        name = Config.oidcProviderName(provider),
        redirectUrl = redirectUrl
      )
    }

  }

}
