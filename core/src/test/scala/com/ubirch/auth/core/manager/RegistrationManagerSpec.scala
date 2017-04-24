package com.ubirch.auth.core.manager

import com.ubirch.auth.model.NewUser
import com.ubirch.auth.testTools.db.MongoSpec
import com.ubirch.user.core.manager.{ContextManager, UserManager}
import com.ubirch.user.model.db.{Context, User}
import com.ubirch.util.oidc.model.UserContext

/**
  * author: cvandrei
  * since: 2017-04-24
  */
class RegistrationManagerSpec extends MongoSpec {

  feature("register()") {

    scenario(
      """exists?
        |  context: no
        |  user: no
        |  group: no
      """.stripMargin
    ) {

      // prepare
      val userContext = UserContext(context = "trackle-dev", providerId = "google", userId = "asdf-1234")
      val newUser = NewUser("user display name", "my private group")

      // test
      RegistrationManager.register(userContext, newUser) map { result =>

        // verify
        result shouldBe None

      }

    }

    scenario(
      """exists?
        |  context: no
        |  user: yes
        |  group: no
      """.stripMargin
    ) {

      // prepare
      val userContext = UserContext(context = "trackle-dev", providerId = "google", userId = "asdf-1234")
      val newUser = NewUser("user display name", "my private group")

      val user = User(displayName = newUser.displayName, providerId = userContext.providerId, externalId = userContext.userId)
      UserManager.create(user) flatMap {

        case None => fail("failed to create user during preparation")

        case Some(_: User) =>

          // test
          RegistrationManager.register(userContext, newUser) map { result =>

            // verify
            result shouldBe None

          }

      }

    }

    // TODO (test case) context=no; user=yes; group=yes

    scenario(
      """exists?
        |  context: yes
        |  user: no
        |  group: no
      """.stripMargin
    ) {

      // prepare
      val userContext: UserContext = UserContext(context = "trackle-dev", providerId = "google", userId = "asdf-1234")
      val newUser = NewUser("user display name", "my private group")

      val context = Context(displayName = userContext.context)
      ContextManager.create(context) flatMap {

        case None => fail("failed to create context during preparation")

        case Some(_: Context) =>

              // test
              RegistrationManager.register(userContext, newUser) map { result =>

                // verify
                result should be('isDefined)
                // TODO verify user and group
                // TODO verify count of db records

              }

      }

    }

    scenario(
      """exists?
        |  context: yes
        |  user: yes
        |  group: no
      """.stripMargin
    ) {

      // prepare
      val userContext = UserContext(context = "trackle-dev", providerId = "google", userId = "asdf-1234")
      val newUser = NewUser("user display name", "my private group")

      val context: Context = Context(displayName = userContext.context)
      ContextManager.create(context) flatMap {

        case None => fail("failed to create context during preparation")

        case Some(_: Context) =>

          val user = User(displayName = newUser.displayName, providerId = userContext.providerId, externalId = userContext.userId)
          UserManager.create(user) flatMap {

            case None => fail("failed to create user during preparation")

            case Some(_: User) =>

              // test
              RegistrationManager.register(userContext, newUser) map { result =>

                // verify
                result should be('isDefined)
                // TODO verify userInfo
                // TODO verify count of db records

              }

          }

      }

    }

    // TODO (test case) context=yes; user=yes; group=yes

  }

}
