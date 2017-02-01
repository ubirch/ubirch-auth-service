# ubirch-auth-service


## General Information

Not wanting to implement all aspects of a user management logins are done with OpenID Connect. This service bundles the
related functionality.

The ubirch AuthService is responsible for:

* list available OpenID Connect providers
* remember new valid tokens and userIds


## Release History

### Version 0.0.1 (tbd)

* tbd


## Scala Dependencies

### `config`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "config" % "0.0.1-SNAPSHOT"
)
```

### `core`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "core" % "0.0.1-SNAPSHOT"
)
```

### `model`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "model" % "0.0.1-SNAPSHOT"
)
```

### `openid-util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "openid-util" % "0.0.1-SNAPSHOT"
)
```

### `server`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "server" % "0.0.1-SNAPSHOT"
)
```

### `util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "util" % "0.0.1-SNAPSHOT"
)
```


## REST Methods

### Welcome / Health

    curl localhost:8091/
    curl localhost:8091/api/authService/v1

If healthy the server response is:

    200 {"version":"1.0","status":"OK","message":"Welcome to the ubirchAuthService"}


### Provider Infos

Gives us a list of configured providers.

    curl localhost:8091/api/authService/v1/providerInfo/list


### Verify Code

After a successful login users are being redirect to the Frontend. That call includes a _code_ and _state_. Calling this
method has the effect that our system verifies the code and responds with the resulting token. This token can then be
used to request protected resources in other backend services.

    curl -XPOST localhost:8091/api/authService/v1/verify/code -d'{
      "providerId": "$PROVIDER_ID",
      "code": "$CODE",
      "state": "$STATE"
    }'

If the code could be verified the response will be:

    200 {"token": "a_token_1234"}

In case of an error the response will be:

    400 {"version":"1.0","status":"NOK","errorType":"VerificationError","errorMessage":"invalid code"}


### Logout

**TODO implement with UBD-276**
To invalidate a token (logout) please call:

    curl -XPOST localhost:8091/api/authService/v1/logout -d'{
        "providerId": "$PROVIDER_ID",
        "token": "$TOKEN"
    }'

## Configuration

### OpenID Connect Providers

We can configure as many OpenID Connect providers as we want by using the following pattern:

    ubirchAuthService {

      openIdConnectProviders {

        providerList = ["generic1", "generic2"]

        openIdConnectProviders {

          gemeric {
            name = "Generic1"
            scope = "openid"
            clientId = "12341234"
            clientSecret = "asdfölkjasdf"
            endpointConfig = "https://login.example.com/.well-known/openid-configuration"
            endpoints {
              // TODO query dynamically from endpointConfig
              authorization = "https://login.example.com/oidc/auth"
              token = "https://login.example.com/oidctoken"
              userInfo = "https://login.example.com/oidc/userinfo"
            }
            callbackUrl = "http://client.com/callback-generic1"
          }
        }

      }

    }

Provider configs are read dynamically based on the list defined in
_ubirchauthService.openIdConnectProviders.providerList_. When adding a new one please don't forget to add it's name to
that list, too. In the above example _generic2_ has no configuration which will result in runtime errors.


## Deployment Notes

TODO


## Automated Tests

TODO (if necessary)


## Local Setup

To start the server run:

    ./sbt server/run


## Create Docker Image

    ./sbt server/docker
