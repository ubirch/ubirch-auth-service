package com.ubirch.login.core.manager

import com.ubirch.login.model.provider.ProviderInfo

/**
  * author: cvandrei
  * since: 2017-01-26
  */
object ProviderInfoManager {

  def providerInfoList(): Seq[ProviderInfo] = {

    // TODO read providers from config
    val genericProvider1 = ProviderInfo(name = "Generic1",
      logoUrl = "https://example.com/logo-generic1.jpg",
      redirectUrl = "https://example.com?foo1=bar1"
    )
    val genericProvider2 = ProviderInfo(name = "Generic2",
      logoUrl = "https://example.com/logo-generic2.jpg",
      redirectUrl = "https://example.com?foo2=bar2"
    )

    Seq(genericProvider1, genericProvider2)

  }

}
