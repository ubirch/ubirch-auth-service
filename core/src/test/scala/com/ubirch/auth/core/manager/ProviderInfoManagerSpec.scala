package com.ubirch.auth.core.manager

import java.net.{URI, URL}

import com.ubirch.auth.model.ProviderInfo
import com.ubirch.auth.model.db.{ContextProviderConfig, OidcProviderConfig}
import com.ubirch.auth.testTools.db.redis.RedisSpec
import com.ubirch.auth.util.db.config.defaults.AppIds
import com.ubirch.auth.util.db.config.{OidcContextProviderUtil, OidcProviderUtil}

/**
  * author: cvandrei
  * since: 2017-03-14
  */
class ProviderInfoManagerSpec extends RedisSpec {

  feature("providerInfoList()") {

    scenario("context does not exist; no config exists --> return no providers") {
      ProviderInfoManager.providerInfoList("contextName", "appId") map (_ should be('isEmpty))
    }

    scenario("context does not exist") {

      OidcProviderUtil.initProviders() flatMap { _ =>
        OidcContextProviderUtil.initContexts() flatMap { expectedContextList =>

          val context = expectedContextList.head._1 + "foo"
          expectedContextList.contains(context) should be(false)

          // test
          ProviderInfoManager.providerInfoList(context, AppIds.legacy) map { contextProviderList =>
            contextProviderList should be('isEmpty)
          }

        }
      }

    }

    scenario("context exists and has providers --> return providers") {

      // prepare
      OidcProviderUtil.initProviders() flatMap { expectedProviderList =>
        OidcContextProviderUtil.initContexts() flatMap { expectedContextList =>

          val context = expectedContextList.head._1

          // test
          ProviderInfoManager.providerInfoList(context, AppIds.legacy) map { contextProviderList =>

            // verify
            val expectedContextProviders = expectedContextList(context)
            contextProviderList.size should be(expectedContextProviders.size)
            verifyContextProviders(contextProviderList, expectedProviderList, expectedContextList(context)) should be(true)

          }

        }
      }

    }

    // TODO fix test (UBD-430)! it fails with: Ask timed out
    /*
    scenario("context exists but without providers (none configured, but activated) --> return no providers") {

      // prepare
      OidcProviderUtil.initProviders() flatMap { expectedProviderList =>
        OidcContextProviderUtil.initContexts() flatMap { expectedContextList =>

          expectedProviderList.values map(_.id) foreach(OidcProviderUtil.deleteProvider(_))

          val context = expectedContextList.head._1

          // test && verify
          ProviderInfoManager.providerInfoList(context, AppIds.legacy) map (_ should be('isEmpty))

        }
      }

    }
    */

    scenario("context exists but without providers (none activated) --> return no providers") {

      // prepare
      OidcProviderUtil.initProviders() flatMap { expectedProviderList =>
        OidcContextProviderUtil.initContexts() flatMap { expectedContextList =>

          expectedProviderList.values map (_.id) foreach (OidcProviderUtil.disableProvider(_))

          val context = expectedContextList.head._1

          // test && verify
          ProviderInfoManager.providerInfoList(context, AppIds.legacy) map (_ should be('isEmpty))

        }
      }

    }

    scenario("context exists and has providers but context is not enabled --> return no providers") {

      // prepare
      OidcProviderUtil.initProviders() flatMap { expectedProviderList =>
        OidcContextProviderUtil.initContexts() flatMap { expectedContextList =>

          val context = expectedContextList.head._1
          OidcContextProviderUtil.disableContext(context) flatMap { contextDisabled =>

            contextDisabled should be(true)

            // test && verify
            ProviderInfoManager.providerInfoList(context, AppIds.legacy) map (_ should be('isEmpty))

          }

        }
      }

    }

  }

  private def verifyContextProviders(actualProviderInfoList: Seq[ProviderInfo],
                                     expectedProviderList: Map[String, OidcProviderConfig],
                                     expectedContextList: Seq[ContextProviderConfig]
                                    ): Boolean = {

    // verify that all elements in actualProviderInfoList have the same context
    actualProviderInfoList.filter(_.context == actualProviderInfoList.head.context) should be(actualProviderInfoList)

    // verify that all fields except "redirectUrl" match
    val expectedProviderInfos = expectedContextList.map { ctx =>
      ProviderInfo(
        context = ctx.context,
        appId = ctx.appId,
        providerId = ctx.provider,
        name = expectedProviderList(ctx.provider).name,
        redirectUrl = ""
      )
    }
    actualProviderInfoList map (_.copy(redirectUrl = "")) should be(expectedProviderInfos)

    // verify redirect urls
    actualProviderInfoList foreach { providerInfo =>

      verifyRedirectUriHostPath(providerInfo, expectedProviderList)
      verifyRedirectParams(providerInfo, expectedProviderList, expectedContextList)

    }

    true

  }

  private def verifyRedirectUriHostPath(providerInfo: ProviderInfo,
                                        expectedProviderList: Map[String, OidcProviderConfig]
                                       ): Unit = {

    val redirectUrl = new URL(providerInfo.redirectUrl)
    val authorizationUri = expectedProviderList(providerInfo.providerId).endpoints.authorization

    val protocol = redirectUrl.getProtocol
    val host = redirectUrl.getHost
    val path = redirectUrl.getPath
    s"$protocol://$host$path" should be(authorizationUri)

  }

  private def verifyRedirectParams(providerInfo: ProviderInfo,
                                   expectedProviderList: Map[String, OidcProviderConfig],
                                   expectedContextList: Seq[ContextProviderConfig]
                                  ): Unit = {

    val redirectUrl = new URL(providerInfo.redirectUrl)
    val params = redirectUrl.getQuery.split("&").map { section =>
      val split = section.split("=")
      split(0) -> split(1)
    }.toMap

    val context = expectedContextList.find(_.provider == providerInfo.providerId).get

    params("response_type") should be("code")
    params("client_id") should be(context.clientId)
    new URI(params("redirect_uri")).getPath should be(context.callbackUrl.toString)
    params("scope") should be(expectedProviderList(providerInfo.providerId).scope.replaceAll(" ", "+"))
    params("state").length should be > 0

  }

}
