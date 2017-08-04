# ubirch-auth-service


## General Information

Not wanting to implement all aspects of a user management logins are done with OpenID Connect. This service bundles the
related functionality.

The ubirch AuthService is responsible for:

* list available OpenID Connect providers
* remember new valid tokens and userIds


## Release History

### Version 0.2.15 (tbd)

* add trackle related clientIds and clientSecrets for demo environment

### Version 0.2.14 (2017-07-31)

* fixed broken logging (would lead to a Throwable not being logged)
* update to `com.ubirch.util:deep-check-model:0.2.0`
* update to `com.ubirch.util:mongo-utils:0.3.6`
* update to `com.ubirch.util:mongo-test-utils:0.3.6`
* update to `com.ubirch.util:redis-util:0.3.4`
* update to `com.ubirch.util:redis-test-util:0.3.4`
* update to `com.ubirch.util:oidc-utils:0.4.10`
* update to `com.ubirch.user:*:0.5.1`

### Version 0.2.13 (2017-07-27)

* configure trackle-admin-ui-* contexts for dev and demo (except for the client secret)
* update to `com.ubirch.util:json:0.4.3`
* update to `com.ubirch.util:deep-check-model:0.1.3`
* update to `com.ubirch.util:response-util:0.2.4`
* update to `com.ubirch.util:mongo-test-utils:0.3.5`
* update to `com.ubirch.util:mongo-utils:0.3.5`
* update to `com.ubirch.util:redis-test-util:0.3.3`
* update to `com.ubirch.util:redis-util:0.3.3`
* update to `com.ubirch.util:oidc-utils:0.4.9`
* update to `com.ubirch.user:*:0.4.19`

### Version 0.2.12 (2017-07-19)

* update to `com.nimbusds:oauth2-oidc-sdk:5.30`

### Version 0.2.11 (2017-07-19)

* improved logging when creating provider and context related configs during server boot

### Version 0.2.10 (2017-07-18)

* update to Akka 2.4.19
* update to `com.ubirch.user:*:0.4.17`
* update _com.ubirch.util:mongo(-test)-utils_ to 0.3.4

### Version 0.2.9 (2017-07-17)

* update Akka HTTP to 10.0.9
* update _com.ubirch.util:rest-akka-http(-test)_ to 0.3.8
* update _com.ubirch.util:response-util_ to 0.2.3
* update _com.ubirch.util:oidc-utils_ to 0.4.8
* update _com.ubirch.user:*_ to 0.4.15

### Version 0.2.8 (2017-07-13)

* update logging dependencies
* update logback configs
* update _com.ubirch.util:mongo(-test)-utils_ to 0.3.3
* update _com.ubirch.user:*_ to 0.4.14

### Version 0.2.7 (2017-06-29)

* add scripts `dev-scripts/resetDatabase.sh` and `dev-scripts/createDevtoken.sh`
* add _props()_ method to actors
* updated to _com.ubirch.util:json:0.4.2_ and all ubirch util libs depending on it, too

### Version 0.2.6 (2017-06-22)

* endpoint `GET /userInfo` now responds with errorType _NoUserInfoFound_
* updated curl call documentation in README

### Version 0.2.5 (2017-06-19)

* update json4s to 3.5.2
* update to _com.ubirch.util:json:0.4.1_
* update to _com.ubirch.util:deep-check-model:0.1.1_
* update to _com.ubirch.util:response-util:0.2.1_
* update to _com.ubirch.util:mongo-utils:0.3.1_
* update to _com.ubirch.util:mongo-test-utils:0.3.1_
* update to _com.ubirch.util:redis-util:0.3.1_
* update to _com.ubirch.util:redis-test-util:0.3.1_
* update to _com.ubirch.util:oidc-utils:0.4.6_
* update to _com.ubirch.util:response-util:0.2.1_
* update to _com.ubirch.user:*:0.4.10_

### Version 0.2.4 (2017-06-12)

* endpoint `/api/userService/v1/deepCheck` responds with http status 503 if deep check finds problems
* update _com.ubirch.user:*_ to 0.4.9

### Version 0.2.3 (2017-06-09)

* migrate to _com.ubirch.util:deep-check-model:0.1.0_
* update to Akka 2.4.18
* update _com.ubirch.user:*_ to 0.4.8

### Version 0.2.2 (2017-06-08)

* introduce endpoint `/api/authService/v1/check`
* update to sbt 0.13.15
* update _com.ubirch.util:json_ to version 0.4.0
* update _com.ubirch.util:oidc-utils_ to version 0.4.4
* update _com.ubirch.util:response-util_ to version 0.1.6
* update _com.ubirch.util:mongo-test-utils_ to version 0.2.3
* update _com.ubirch.util:mongo-utils_ to version 0.2.3
* update _com.ubirch.util:redis-test-utils_ to version 0.2.3
* update _com.ubirch.util:redis-utils_ to version 0.2.3
* update _com.ubirch.user:*_ to 0.4.7
* introduce endpoint `/api/userService/v1/deepCheck`

### Version 0.2.1 (2017-05-31)

* added activeUser flag to userInfo
* updated to ubirchUserService 0.4.5

### Version 0.2.0 (2017-05-29)

* update _com.ubirch.user:*_ to 0.4.1
* added optional boolean field: `UserInfoGroup.adminGroup`
* update _com.nimbusds:oauth2-oidc-sdk_ to version 5.26

### Version 0.1.2 (2017-05-29)

* change default test user created by `CreateDevToken`
* update _com.ubirch.user:*_ to 0.3.2

### Version 0.1.1 (2017-05-22)

* update Akka Http to 10.0.6
* update to Akka 2.4.18
* update _com.ubirch.util:rest-akka-http_ to 0.3.7
* update _com.ubirch.util:rest-akka-http-test_ to 0.3.7
* update _com.ubirch.util:oidc-utils_ to 0.4.1
* update _com.ubirch.util:redis-test-util_ to 0.2.2
* update _com.ubirch.util:redis-util_ to 0.2.2
* update _com.ubirch.util:response-util_ to 0.1.4
* update _com.ubirch.user:*_ to 0.3.1

### Version 0.1.0 (2017-05-05)

* initial release


## Scala Dependencies

### `cmdtools`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "cmdtools" % "0.2.15-SNAPSHOT"
)
```### `config`


### `config`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "config" % "0.2.15-SNAPSHOT"
)
```

### `core`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "core" % "0.2.15-SNAPSHOT"
)
```

### `model`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "model" % "0.2.15-SNAPSHOT"
)
```

### `model-db`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "model-db" % "0.2.15-SNAPSHOT"
)
```

### `oidc-util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "oidc-util" % "0.2.15-SNAPSHOT"
)
```

### `server`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "server" % "0.2.15-SNAPSHOT"
)
```

### `test-tools`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "test-tools" % "0.2.15-SNAPSHOT"
)
```

### `test-tools-ext`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "test-tools-ext" % "0.2.15-SNAPSHOT"
)
```

### `util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "util" % "0.2.15-SNAPSHOT"
)
```


## REST Methods

### Welcome / Health / Check

    curl localhost:8091/
    curl localhost:8091/api/authService/v1
    curl localhost:8091/api/authService/v1/check

If healthy the server response is:

    200 {"version":"1.0","status":"OK","message":"Welcome to the ubirchAuthService ( $GO_PIPELINE_NAME / $GO_PIPELINE_LABEL / $GO_PIPELINE_REVISION )"}

If not healthy the server response is:

    400 {"version":"1.0","status":"NOK","message":"$ERROR_MESSAGE"}

### Deep Check / Server Health

    curl localhost:8092/api/authService/v1/deepCheck

If healthy the response is:

    200 {"version":"1.0","status":"OK","messages":[]}

If not healthy the status is `false` and the `messages` array not empty:

    503 {"version":"1.0","status":"NOK","messages":["unable to connect to the database"]}


### Provider Infos (DEPRECATED)

*This method is deprecated!!! Use _/providerInfo/list/$CONTEXT/$APP_ID instead_*

Gives us a list of providers configured for a specific context (e.g. `trackle`, `trackle-dev`, `ubirch`, `ubirch-dev`,
...).

    curl localhost:8091/api/authService/v1/providerInfo/list/$CONTEXT

### Provider Infos

Gives us a list of providers configured for a specific context and app id (e.g. `trackle-dev/ui`, `ubirch-dev/admin-ui`,
...).

    curl localhost:8091/api/authService/v1/providerInfo/list/$CONTEXT/$APP_ID


### Verify Code

After a successful login users are being redirect to the Frontend. That call includes a _context_, _appId_,
_providerId_, _code_ and _state_. Calling this method has the effect that our system verifies the code and responds with
the resulting token. This token can then be used to request protected resources in other backend services.

    curl -XPOST localhost:8091/api/authService/v1/verify/code -H "Content-Type: application/json" -d'{
      "context": "$CONTEXT",
      "appId": "$APP_ID",
      "providerId": "$PROVIDER_ID",
      "code": "$CODE",
      "state": "$STATE"
    }'

If the code could be verified the response will be:

    200 {"token": "a_token_1234"}

In case of an error the response will be:

    400 {"version":"1.0","status":"NOK","errorType":"VerificationError","errorMessage":"invalid code"}


### Logout

To invalidate a token (logout) please call:

    curl localhost:8091/api/authService/v1/logout/$TOKEN

If the logout is successful (or the token already expired) the response is:

    200 OK

In case of an error the response is:

    400 {"version":"1.0","status":"NOK","errorType":"LogoutError","errorMessage":"logout failed"}


### Registration

The auth-service relies on external OpenID Connect providers for authentication.
To be able to remember the connection between devices and users users need to be known in our system, too. This happens
in two steps:
1) login with external OpenID Connect provider (authentication)
2) register in our system

Users are independent of a context and it can happen that they have been registered in another context already. All user
specific information (user's and group' displayName) is dynamically determined based on information received from the
OpenID Connect provider).

To register a new user:

    curl -XPOST localhost:8091/api/authService/v1/register -H "Authorization: Bearer $TOKEN"

If the registration is successful the response is:

    200
    {
      "displayName": "some user name",
      "myGroups": [
        {
          "id": "a4c08d88-7c43-4984-a568-0672b4431016", // UUID
          "displayName": "some user name" // same as displayName above
        }
      ],
      "allowedGroups": [] // being a new user it is impossible to have been added to another group
    }

If the registration fails the response is:

    400
    {
      "apiVersion": "1.0.0",
      "status": "NOK",
      "error": {
        "errorId": "RegistrationError", // errorId can be different
        "errorMessage": "failed to register new user" // errorMessage can be different
      }
    }


### User Info

#### Get

    curl localhost:8091/api/authService/v1/userInfo -H "Authorization: Bearer $TOKEN"

If the query is successful the response is (user exists but is not registered not in this context):

    200
    {
      "displayName": "some string being displayed in frontend as my display name",
      "locale": "en",
      "myGroups": [],
      "allowedGroups": []
    }

If the query is successful the response is (user is registered in this context and has been added to other group
including the admin group of this context):

    200
    {
      "displayName": "some string being displayed in frontend as my display name",
      "myGroups": [
        {
          "id": "a4c08d88-7c43-4984-a568-0672b4431016", // UUID
          "displayName": "my-ubirch-group"
        }
      ],
      "allowedGroups": [
        {
          "id": "f2d4280d-336f-438d-9b2a-70337723a3e7", // UUID
          "displayName": "my-best-friends-ubirch-group"
        },
        {
          "id": "32c5c928-97a0-49b7-9d71-0b0517b7d13e", // UUID
          "displayName": "admin group",
          "adminGroup": true // means user has admin rights for this context
        }
      ]
    }

If no user is found the response is:

    400
    {
      "apiVersion": "1.0.0",
      "status": "NOK",
      "error": {
        "errorId": "NoUserInfoFound"
        "errorMessage": "failed to get user info"
      }
    }

If the query fails the response is:

    500
    {
      "apiVersion": "1.0.0",
      "status": "NOK",
      "error": {
        "errorId": "ServerError",
        "errorMessage": "failed to get user info"
      }
    }

#### Update

    curl -XPUT localhost:8091/api/authService/v1/userInfo -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{
      "displayName": "my new display name",
      "locale": "en"
    }'

If the query is successful the response is:

    200
    {
      "displayName": "my new display name",
      "locale": "en",
      "myGroups": [
        {
          "id": "a4c08d88-7c43-4984-a568-0672b4431016", // UUID
          "displayName": "my-ubirch-group"
        }
      ],
      "allowedGroups": [
        {
          "id": "f2d4280d-336f-438d-9b2a-70337723a3e7", // UUID
          "displayName": "my-best-friends-ubirch-group"
        },
        {
          "id": "32c5c928-97a0-49b7-9d71-0b0517b7d13e", // UUID
          "displayName": "another-friends-ubirch-group"
        }
      ]
    }

If the query fails the response is:

    400
    {
      "apiVersion": "1.0.0",
      "status": "NOK",
      "error": {
        "errorId": "UpdateError", // errorId can be different
        "errorMessage": "failed to update user" // errorMessage can be different
      }
    }

### Group Info

#### Update

TODO: group management will follow at a later time


## Configuration

### OpenID Connect Providers

OpenID Connect providers are configured in a Redis database requiring two parts:

* generic provider config
* context specific config

For a programmatic example please refer to the class `InitData`. Changes to the defaults used by `InitData` may be made
in:

* `OidcProviders`
* `OidcContextProvider`

#### Generic Provider Config

To configure a provider we have to create an Redis record with the key `oidc.provider.$PROVIDER`. It's value is a JSON
with all context independent information.

    set oidc.provider.google "{\"id\":\"google\",\"name\":\"Google\",\"scope\":\"openid profile\",\"endpointConfig\":\"https://accounts.google.com/.well-known/openid-configuration\",\"tokenSigningAlgorithms\":[\"RS256\"],\"endpoints\":{\"authorization\":\"https://accounts.google.com/o/oauth2/v2/auth\",\"token\":\"https://www.googleapis.com/oauth2/v4/token\",\"jwks\":\"https://www.googleapis.com/oauth2/v3/certs\"}}"

More human readable the JSON looks as follows:

    {
      id = "google", # the $PROVIDER from the key
      name = "Google",
      scope = "openid profile",
      endpointConfig = "https://accounts.google.com/.well-known/openid-configuration",
      tokenSigningAlgorithms = ["RS256"],
      endpoints {
        authorization = "https://accounts.google.com/o/oauth2/v2/auth",
        token = "https://www.googleapis.com/oauth2/v4/token",
        jwks = "https://www.googleapis.com/oauth2/v3/certs"
      }
    }

We can enable a provider by adding it to the list stored in the key `oidc.provider.list` (using the `id` from the
above JSON).

    lpush oidc.provider.list google

#### Context Specific Config

A context can be `trackle-dev` for example and requires it's own `clientId`, `clientSecret` and `callbackUrl`.

To add the config we create a record with the key `oidc.context.$CONTEXT.$APP_ID.$PROVIDER`. It's value is a JSON, too.

    set oidc.context.trackle-dev.admin-ui.google "{\"context\":\"trackle-dev\",\"appId\":\"admin-ui\",\"provider\":\"google\",\"clientId\":\"370115332091-kqf5hu698s4sodrvv03ka3bule530rp5.apps.googleusercontent.com\",\"clientSecret\":\"M86oj4LxV-CcEDd3ougKSbsV\",\"callbackUrl\":\"https://localhost:10000/oidc-callback-google\"}"

More human-readable the JSON looks as follows:

    {
      "context": "trackle-dev", # the $CONTEXT from the key
      "appId": "admin-ui", # the $APP_ID from the key
      "provider": "google", # the $PROVIDER from the key which also has to exist in `oidc.provider.$PROVIDER`
      "clientId": "370115332091-kqf5hu698s4sodrvv03ka3bule530rp5.apps.googleusercontent.com",
      "clientSecret": "M86oj4LxV-CcEDd3ougKSbsV",
      "callbackUrl": "https://localhost:10000/oidc-callback-google"
    }

We can enable a context wby adding it to the set stored in the key `oidc.context.list` (using the `context` from the
above JSON).

    sadd oidc.context.list trackle-dev


### Redis

Since we're using the [rediscala library|https://github.com/etaty/rediscala] we can use it's
[configuration options|https://github.com/etaty/rediscala/blob/master/src/main/resources/reference.conf]. 


## Deployment Notes

This service has the following dependencies:
 
* Redis 3.2.x (verification needed; definitely works with 3.2.7 and 3.2.8)
  * download redis & extract *.tar.gz
  * cd {redis-root}
  * make
  * ./src/redis-server

* MongoDB as used by the `user-service` (through the dependency `com.ubich.user:core`)

* providers and contexts have to exist in Redis (see #Configuration section for details)

* all contexts in which you want to register users have to exist in the MongoDB. Here's two examples:

    {
        "_id" : ObjectId("590a25ce63a845f6501f4128"),
        "id" : UUID("4f72edd2-ec27-4df6-858e-3911d8f5207b"),
        "displayName" : "ubirch-dev",
        "created" : ISODate("2017-05-03T18:47:42.100Z"),
        "updated" : ISODate("2017-05-03T18:47:42.100Z")
    }
    
    {
        "_id" : ObjectId("590a25ce63a845f6501f412a"),
        "id" : UUID("91823652-e530-4908-a445-f2da660538f8"),
        "displayName" : "ubirch-demo",
        "created" : ISODate("2017-05-03T18:47:42.105Z"),
        "updated" : ISODate("2017-05-03T18:47:42.105Z")
    }


## Automated Tests

run all tests

    ./sbt test

### generate coverage report

    ./sbt clean coverage test coverageReport

more details here: https://github.com/scoverage/sbt-scoverage

## Local Setup

1) Start [Redis|https://redis.io/] on the default port (tested with version 3.2.7 and 3.2.8)

2) Configure OpenConnectID Providers

*Running `dev-scripts/resetDatabase.sh` does everything in this step.*

If you still have old data you want to delete first please run

    ./sbt "cmdtools/runMain com.ubirch.auth.cmd.RedisDelete"

To init default providers and contexts for development please run

    ./sbt "cmdtools/runMain com.ubirch.auth.cmd.InitData"

generated contexts

* trackle-local
* trackle-admin-ui-local
* ubirch-admin-ui-local
* ubirch-admin-ui-dev
* trackle-admin-ui-dev
* ubirch-admin-ui-demo
* trackle-admin-ui-demo

3) Start MongoDB

The `/register` and `/userInfo` calls use managers from the user-service and hence depend on MongoDB. Please follow the
local setup section in the user-service's README, too.

4) Start AuthService

    ./sbt server/run

5) (optional) Create Test User Token

*Everything in this step can be done by running `dev-scripts/createDevtoken.sh`, too.*

This step saves us from having to login with any of the OpenID Connect providers. To get a valid test user token run:

    ./sbt "cmdtools/runMain com.ubirch.auth.cmd.CreateDevToken"

The token, userId and context are printed out and can be changed with the following configuration keys:

* ubirchAuthService.testUser.token
* ubirchAuthService.testUser.providerId
* ubirchAuthService.testUser.userId
* ubirchAuthService.testUser.context
* ubirchAuthService.testUser.userName
* ubirchAuthService.testUser.locale

All tokens expire at some point. This TTL (time-to-live) can be configured as well. Whenever you use the token
successfully (calling a Bearer token protected REST resource) the expiry date will be reset.

## Create Docker Image

    ./sbt server/docker
