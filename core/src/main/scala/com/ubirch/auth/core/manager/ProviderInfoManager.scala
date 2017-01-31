package com.ubirch.auth.core.manager

import com.ubirch.auth.config.Config
import com.ubirch.auth.oidcutil.AuthRequest
import com.ubirch.auth.model.provider.ProviderInfo

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
