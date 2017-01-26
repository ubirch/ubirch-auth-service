package com.ubirch.login.core.manager

import com.ubirch.login.model.provider.ProviderInfo

/**
  * author: cvandrei
  * since: 2017-01-26
  */
object ProviderInfoManager {

  def providerInfoList(): Seq[ProviderInfo] = {

    // TODO read providers from config
    val genericProvider = ProviderInfo(name = "Generic",
      logoUrl = "https://example.com/logo.jpg",
      redirectUrl = "https://example.com?foo=bar"
    )

    Seq(genericProvider)

  }

}
