package com.ubirch.auth.core.manager

import com.ubirch.auth.model.Logout
import com.ubirch.auth.testTools.db.redis.RedisSpec
import com.ubirch.util.oidc.util.OidcUtil

/**
  * author: cvandrei
  * since: 2017-03-22
  */
class LogoutManagerSpec extends RedisSpec {

  feature("logout") {

    scenario("empty database") {

      // test
      LogoutManager.logout(Logout("provider-id", "token2")) flatMap { logoutResult =>

        // verify
        logoutResult should be(true)

        redis.keys("*") map { keys =>
          keys should be('isEmpty)
        }

      }

    }

    scenario("token does not exist") {

      // prepare
      val provider = "provider-id"
      val token1 = "token1" // to logout with
      val token2 = "token2" // exists in Redis
      val redisKey = OidcUtil.tokenToHashedKey(provider = provider, token = token2)
      redis.set(redisKey, "userId") flatMap { token2Created =>
        token2Created should be(true)

        // test
        LogoutManager.logout(Logout(provider, token1)) flatMap { logoutResult =>

          // verify
          Thread.sleep(200)
          logoutResult should be(true)

          redis.keys("*") map { keys =>
            keys.size shouldBe 1
          }

        }

      }

    }

    scenario("token exists") {

      // prepare
      val provider = "provider-id"
      val token = "token"
      val redisKey = OidcUtil.tokenToHashedKey(provider = provider, token = token)
      redis.set(redisKey, "userId") flatMap { tokenCreated =>
        tokenCreated should be(true)

        // test
        LogoutManager.logout(Logout(provider, token)) flatMap { logoutResult =>

          // verify
          Thread.sleep(500)
          logoutResult should be(true)

          redis.keys("*") map { keys =>
            keys should be('isEmpty)
          }

        }

      }

    }

  }

}
