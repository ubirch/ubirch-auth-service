package com.ubirch.auth.core.manager

import com.ubirch.auth.testTools.db.redis.RedisSpec
import com.ubirch.util.json.JsonFormats
import com.ubirch.util.oidc.model.UserContext
import com.ubirch.util.oidc.util.OidcUtil

import org.json4s.native.Serialization.write

/**
  * author: cvandrei
  * since: 2017-03-22
  */
class LogoutManagerSpec extends RedisSpec {

  implicit private val formatter = JsonFormats.default

  feature("logout") {

    scenario("empty database") {

      // test
      LogoutManager.logout("some-token") flatMap { logoutResult =>

        // verify
        logoutResult should be(true)

        redis.keys("*") map { keys =>
          keys should be('isEmpty)
        }

      }

    }

    scenario("token does not exist") {

      // prepare
      val token1 = "some-token-1" // to logout with
      val token2 = "some-token-2" // exists in Redis
      val redisKey = OidcUtil.tokenToHashedKey(token2)
      val redisValue = write(
        UserContext(
          context = "some-context",
          providerId = "some-provider-id",
          userId = "some-user-id",
          userName = "some-user-name",
          locale = "en"
        )
      )
      redis.set(redisKey, redisValue) flatMap { token2Created =>
        token2Created should be(true)

        // test
        LogoutManager.logout(token1) flatMap { logoutResult =>

          // verify
          Thread.sleep(500)
          logoutResult should be(true)

          redis.keys("*") map { keys =>
            keys.size shouldBe 1
          }

        }

      }

    }

    scenario("token exists") {

      // prepare
      val token = "some-token"
      val redisKey = OidcUtil.tokenToHashedKey(token)
      val redisValue = write(
        UserContext(
          context = "some-context",
          providerId = "some-provider-id",
          userId = "some-user-id",
          userName = "some-user-name",
          locale = "en"
        )
      )
      redis.set(redisKey, redisValue) flatMap { tokenCreated =>
        tokenCreated should be(true)

        // test
        LogoutManager.logout(token) flatMap { logoutResult =>

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
