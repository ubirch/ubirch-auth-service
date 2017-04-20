# ubirch-auth-service


## General Information

Not wanting to implement all aspects of a user management logins are done with OpenID Connect. This service bundles the
related functionality.

The ubirch AuthService is responsible for:

* list available OpenID Connect providers
* remember new valid tokens and userIds


## Release History

### Version 0.1.0 (tbd)

* initial release


## Scala Dependencies

### `cmdtools`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "cmdtools" % "0.1.0-SNAPSHOT"
)
```### `config`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "config" % "0.1.0-SNAPSHOT"
)
```

### `core`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "core" % "0.1.0-SNAPSHOT"
)
```

### `model`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "model" % "0.1.0-SNAPSHOT"
)
```

### `openid-util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "openid-util" % "0.1.0-SNAPSHOT"
)
```

### `server`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "server" % "0.1.0-SNAPSHOT"
)
```

### `test-tools`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "test-tools" % "0.1.0-SNAPSHOT"
)
```

### `test-tools-ext`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "test-tools-ext" % "0.1.0-SNAPSHOT"
)
```

### `util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "util" % "0.1.0-SNAPSHOT"
)
```


## REST Methods

### Welcome / Health

    curl localhost:8091/
    curl localhost:8091/api/authService/v1

If healthy the server response is:

    200 {"version":"1.0","status":"OK","message":"Welcome to the ubirchAuthService"}

If not healthy the server response is:

    400 {"version":"1.0","status":"NOK","message":"$ERROR_MESSAGE"}


### Provider Infos

Gives us a list of providers configured for a specific context (e.g. `trackle`, `trackle-dev`, `ubirch`, `ubirch-dev`,
...).

    curl localhost:8091/api/authService/v1/providerInfo/list/$CONTEXT


### Verify Code

After a successful login users are being redirect to the Frontend. That call includes a _context_, _providerId_, _code_
and _state_. Calling this method has the effect that our system verifies the code and responds with the resulting token.
This token can then be used to request protected resources in other backend services.

    curl -XPOST localhost:8091/api/authService/v1/verify/code -H "Content-Type: application/json" -d'{
      "context": "$CONTEXT",
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

To register a new user:

    curl localhost:8091/api/authService/v1/register -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{
      "displayName": "some string being displayed in frontend as my display name",
      "myGroup": "my-ubirch-group" // (optional)
    }'

If the registration is successful the response is:

    200
    {
      "displayName": "some string being displayed in frontend as my display name",
      "myGroups": ["my-ubirch-group"],
      "allowedGroups": ["by-best-friends-ubirch-group", "another-friends-ubirch-group"]
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

If the query is successful the response is:

    200
    {
      "displayName": "some string being displayed in frontend as my display name",
      "myGroups": ["my-ubirch-group"],
      "allowedGroups": ["by-best-friends-ubirch-group", "another-friends-ubirch-group"]
    }

If the query fails the response is:

    400
    {
      "apiVersion": "1.0.0",
      "status": "NOK",
      "error": {
        "errorId": "QueryError", // errorId can be different
        "errorMessage": "user does not exist" // errorMessage can be different
      }
    }

#### Update

    curl -XPOST localhost:8091/api/authService/v1/userInfo -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{
      "displayName": "my new display name"
    }'

If the query is successful the response is:

    200
    {
      "displayName": "my new display name",
      "myGroups": ["my-ubirch-group"],
      "allowedGroups": ["by-best-friends-ubirch-group", "another-friends-ubirch-group"]
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

    set oidc.provider.google "{\"id\":\"google\",\"name\":\"Google\",\"scope\":\"openid\",\"endpointConfig\":\"https://accounts.google.com/.well-known/openid-configuration\",\"tokenSigningAlgorithms\":[\"RS256\"],\"endpoints\":{\"authorization\":\"https://accounts.google.com/o/oauth2/v2/auth\",\"token\":\"https://www.googleapis.com/oauth2/v4/token\",\"jwks\":\"https://www.googleapis.com/oauth2/v3/certs\"}}"

More human readable the JSON looks as follows:

    {
      id = "google", # the $PROVIDER from the key
      name = "Google",
      scope = "openid",
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

To add the config we create a record with the key `oidc.context.$CONTEXT.$PROVIDER`. It's value is a JSON, too.

    set oidc.context.trackle-dev.google "{\"context\":\"trackle-dev\",\"provider\":\"google\",\"clientId\":\"370115332091-kqf5hu698s4sodrvv03ka3bule530rp5.apps.googleusercontent.com\",\"clientSecret\":\"M86oj4LxV-CcEDd3ougKSbsV\",\"callbackUrl\":\"https://localhost:10000/oidc-callback-google\"}"

More human-readable the JSON looks as follows:

    {
      "context": "trackle-dev", # the $CONTEXT from the key
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


## Automated Tests

run all tests

    ./sbt test

### generate coverage report

    ./sbt clean coverage test coverageReport

more details here: https://github.com/scoverage/sbt-scoverage

## Local Setup

1) Start [Redis|https://redis.io/] on the default port (tested with version 3.2.7 and 3.2.8)

2) Configure OpenConnectID Providers

If you still have old data you want to delete first please run

    ./sbt "cmdtools/runMain com.ubirch.auth.cmd.RedisDelete"

To init default providers and contexts for development please run

    ./sbt "cmdtools/runMain com.ubirch.auth.cmd.InitData"

generated contexts

* trackle-admin-ui-dev
* ubirch-admin-ui-dev

3) Start AuthService

    ./sbt server/run

4) (optional) Create Test User Token

This step saves us from having to login with any of the OpenID Connect providers. To get a valid test user token run:

    ./sbt "cmdtools/runMain com.ubirch.auth.cmd.CreateDevToken"

The token, userId and context are printed out and can be changed with the following configuration keys:

* ubirchAuthService.testUser.token
* ubirchAuthService.testUser.userId
* ubirchAuthService.testUser.context

All tokens expire at some point. This TTL (time-to-live) can be configured as well. Whenever you use the token
successfully (calling a Bearer token protected REST resource) the expiry date will be reset.

## Create Docker Image

    ./sbt server/docker
