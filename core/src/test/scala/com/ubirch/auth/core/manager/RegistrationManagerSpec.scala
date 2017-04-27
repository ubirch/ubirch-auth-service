package com.ubirch.auth.core.manager

import com.ubirch.auth.model.UserInfo
import com.ubirch.auth.testTools.db.MongoSpec
import com.ubirch.user.config.Config
import com.ubirch.user.model.db.{Context, Group, User}
import com.ubirch.user.testTools.external.DataHelpers
import com.ubirch.util.oidc.model.UserContext
import com.ubirch.util.uuid.UUIDUtil

/**
  * author: cvandrei
  * since: 2017-04-24
  */
class RegistrationManagerSpec extends MongoSpec {

  private val dataHelpers = new DataHelpers

  feature("register()") {

    scenario(
      """exists?
        |  context: no
        |  user: no
        |  group: no
      """.stripMargin
    ) {

      // prepare
      val userContext = defaultUserContext()

      // test
      RegistrationManager.register(userContext) flatMap { result =>

        // verify
        result shouldBe None

        mongoTestUtils.countAll(Config.mongoCollectionContext) map (_ shouldBe 0)
        mongoTestUtils.countAll(Config.mongoCollectionUser) map (_ shouldBe 0)
        mongoTestUtils.countAll(Config.mongoCollectionGroup) map (_ shouldBe 0)

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
      val userContext = defaultUserContext()

      dataHelpers.createUser(
        displayName = userContext.userName,
        providerId = userContext.providerId,
        externalId = userContext.userId
      ) flatMap {

        case None => fail("failed to create user during preparation")

        case Some(_: User) =>

          // test
          RegistrationManager.register(userContext) flatMap { result =>

            // verify
            result shouldBe None

            mongoTestUtils.countAll(Config.mongoCollectionContext) map (_ shouldBe 0)
            mongoTestUtils.countAll(Config.mongoCollectionUser) map (_ shouldBe 1)
            mongoTestUtils.countAll(Config.mongoCollectionGroup) map (_ shouldBe 0)

          }

      }

    }

    scenario(
      """exists?
        |  context: no
        |  user: yes
        |  group: yes
      """.stripMargin
    ) {

      // prepare
      val userContext = defaultUserContext()

      dataHelpers.createUser(
        displayName = userContext.userName,
        providerId = userContext.providerId,
        externalId = userContext.userId
      ) flatMap { userOpt =>

        dataHelpers.createGroup(
          contextId = UUIDUtil.uuid,
          ownerOpt = userOpt
        ) flatMap {

          case None => fail("failed to create group during preparation")

          case Some(_: Group) =>

            // test
            RegistrationManager.register(userContext) flatMap { result =>

              // verify
              result shouldBe None

              mongoTestUtils.countAll(Config.mongoCollectionContext) map (_ shouldBe 0)
              mongoTestUtils.countAll(Config.mongoCollectionUser) map (_ shouldBe 1)
              mongoTestUtils.countAll(Config.mongoCollectionGroup) map (_ shouldBe 1)

            }

        }

      }

    }

    scenario(
      """exists?
        |  context: yes
        |  user: no
        |  group: no
      """.stripMargin
    ) {

      // prepare
      val userContext: UserContext = defaultUserContext()

      dataHelpers.createContext(displayName = userContext.context) flatMap {

        case None => fail("failed to create context during preparation")

        case Some(_: Context) =>

          // test
          RegistrationManager.register(userContext) flatMap {

            // verify
            case None => fail("expected a Some result")

            case Some(result: UserInfo) =>

              mongoTestUtils.countAll(Config.mongoCollectionContext) map (_ shouldBe 1)
              mongoTestUtils.countAll(Config.mongoCollectionUser) map (_ shouldBe 1)
              mongoTestUtils.countAll(Config.mongoCollectionGroup) map (_ shouldBe 1)

              val userName = userContext.userName
              result.displayName shouldBe userName
              result.myGroups.size shouldBe 1
              result.myGroups.head.displayName shouldBe userName
              result.allowedGroups should be('isEmpty)

          }

      }

    }

    scenario(
      """exists?
        |  context: yes
        |  user: yes (different name and locale than existing user...as it's being ignored)
        |  group: no
      """.stripMargin
    ) {

      // prepare
      val userContext = defaultUserContext()

      dataHelpers.createContext(displayName = userContext.context) flatMap {

        case None => fail("failed to create context during preparation")

        case Some(_: Context) =>

          val otherLocale = if (userContext.locale == "en") {
            "de"
          } else {
            "en"
          }
          dataHelpers.createUser(
            displayName = s"${userContext.userName}-actual",
            providerId = userContext.providerId,
            externalId = userContext.userId,
            locale = otherLocale
          ) flatMap {

            case None => fail("failed to create user during preparation")

            case Some(user: User) =>

              // test
              RegistrationManager.register(userContext) flatMap {

                // verify
                case None => fail("expected a Some result")

                case Some(result: UserInfo) =>

                  mongoTestUtils.countAll(Config.mongoCollectionContext) map (_ shouldBe 1)
                  mongoTestUtils.countAll(Config.mongoCollectionUser) map (_ shouldBe 1)
                  mongoTestUtils.countAll(Config.mongoCollectionGroup) map (_ shouldBe 1)

                  result.displayName shouldBe user.displayName
                  result.myGroups.size shouldBe 1
                  result.myGroups.head.displayName shouldBe userContext.userName
                  result.allowedGroups should be('isEmpty)

              }

          }

      }

    }

    scenario(
      """exists?
        |  context: yes
        |  user: yes
        |  group: yes
      """.stripMargin
    ) {

      // prepare
      val userContext = defaultUserContext()

      dataHelpers.createContext(displayName = userContext.context) flatMap { contextOpt =>

        dataHelpers.createUser(
          displayName = userContext.userName,
          providerId = userContext.providerId,
          externalId = userContext.userId
        ) flatMap { userOpt =>

          dataHelpers.createGroup(
            contextOpt = contextOpt,
            ownerOpt = userOpt
          ) flatMap {

            case None => fail("failed to create group during preparation")

            case Some(_: Group) =>

              // test
              RegistrationManager.register(userContext) flatMap { result =>

                // verify
                result shouldBe None

                mongoTestUtils.countAll(Config.mongoCollectionContext) map (_ shouldBe 1)
                mongoTestUtils.countAll(Config.mongoCollectionUser) map (_ shouldBe 1)
                mongoTestUtils.countAll(Config.mongoCollectionGroup) map (_ shouldBe 1)

              }

          }

        }

      }

    }

  }

  private def defaultUserContext(): UserContext = {

    UserContext(
      context = "trackle-dev",
      providerId = "google",
      userId = "asdf-1234",
      userName = "user display name",
      locale = "en"
    )

  }

}
