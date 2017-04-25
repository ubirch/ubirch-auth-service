package com.ubirch.auth.core.manager

import com.ubirch.auth.core.manager.util.UserInfoUtil
import com.ubirch.auth.model.UserInfo
import com.ubirch.auth.testTools.db.MongoSpec
import com.ubirch.user.testTools.external.DataHelpers
import com.ubirch.util.oidc.model.UserContext

/**
  * author: cvandrei
  * since: 2017-04-25
  */
class UserInfoManagerSpec extends MongoSpec {

  private val dataHelpers = new DataHelpers

  feature("getInfo()") {

    scenario("user does not exist") {

      // prepare
      val userContext = UserContext(context = "context-test", "provider-test", "user-test")

      // test
      UserInfoManager.getInfo(userContext) map { result =>

        // verify
        result shouldBe None

      }

    }

    scenario("user exists - without groups") {

      // prepare
      for {

        contextOpt <- dataHelpers.createContext()
        userOpt <- dataHelpers.createUser()

        user = userOpt.get
        userContext = UserContext(
          context = contextOpt.get.displayName,
          providerId = user.providerId,
          userId = user.externalId
        )

        // test
        result <- UserInfoManager.getInfo(userContext)

      } yield {

        // verify
        val expected = UserInfo(
          displayName = user.displayName
        )
        result shouldBe Some(expected)

      }

    }

    scenario("user exists - with myGroups") {

      // prepare
      for {

        contextOpt <- dataHelpers.createContext()
        userOpt <- dataHelpers.createUser()
        myGroup1Opt <- dataHelpers.createGroup(contextOpt = contextOpt, ownerOpt = userOpt)
        myGroup2Opt <- dataHelpers.createGroup(contextOpt = contextOpt, ownerOpt = userOpt)

        user = userOpt.get
        userContext = UserContext(
          context = contextOpt.get.displayName,
          providerId = user.providerId,
          userId = user.externalId
        )

        // test
        result <- UserInfoManager.getInfo(userContext)

      } yield {

        // verify
        val myGroups = UserInfoUtil.toUserInfoGroups(Set(myGroup1Opt.get, myGroup2Opt.get))
        val expected = UserInfo(
          displayName = user.displayName,
          myGroups = myGroups
        )
        result shouldBe Some(expected)

      }

    }

    scenario("user exists - with allowedGroups") {

      // prepare
      for {

        contextOpt <- dataHelpers.createContext()
        user1Opt <- dataHelpers.createUser()
        user2Opt <- dataHelpers.createUser()
        allowedGroup1Opt <- dataHelpers.createGroup(contextOpt = contextOpt, ownerOpt = user2Opt, allowedUsersOpt = user1Opt)
        allowedGroup2Opt <- dataHelpers.createGroup(contextOpt = contextOpt, ownerOpt = user2Opt, allowedUsersOpt = user1Opt)

        user = user1Opt.get
        userContext = UserContext(
          context = contextOpt.get.displayName,
          providerId = user.providerId,
          userId = user.externalId
        )

        // test
        result <- UserInfoManager.getInfo(userContext)

      } yield {

        // verify
        val allowedGroups = UserInfoUtil.toUserInfoGroups(Set(allowedGroup1Opt.get, allowedGroup2Opt.get))
        val expected = UserInfo(
          displayName = user.displayName,
          allowedGroups = allowedGroups
        )
        result shouldBe Some(expected)

      }

    }

    scenario("user exists - with myGroups && allowedGroups") {

      // prepare
      for {

        contextOpt <- dataHelpers.createContext()
        user1Opt <- dataHelpers.createUser()
        myGroup1Opt <- dataHelpers.createGroup(contextOpt = contextOpt, ownerOpt = user1Opt)
        myGroup2Opt <- dataHelpers.createGroup(contextOpt = contextOpt, ownerOpt = user1Opt)
        user2Opt <- dataHelpers.createUser()
        allowedGroup1Opt <- dataHelpers.createGroup(contextOpt = contextOpt, ownerOpt = user2Opt, allowedUsersOpt = user1Opt)
        allowedGroup2Opt <- dataHelpers.createGroup(contextOpt = contextOpt, ownerOpt = user2Opt, allowedUsersOpt = user1Opt)

        user = user1Opt.get
        userContext = UserContext(
          context = contextOpt.get.displayName,
          providerId = user.providerId,
          userId = user.externalId
        )

        // test
        result <- UserInfoManager.getInfo(userContext)

      } yield {

        // verify
        val myGroups = UserInfoUtil.toUserInfoGroups(Set(myGroup1Opt.get, myGroup2Opt.get))
        val allowedGroups = UserInfoUtil.toUserInfoGroups(Set(allowedGroup1Opt.get, allowedGroup2Opt.get))
        val expected = UserInfo(
          displayName = user.displayName,
          myGroups = myGroups,
          allowedGroups = allowedGroups
        )
        result shouldBe Some(expected)

      }

    }

  }

}
