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

    curl -XPOST localhost:8091/api/authService/v1/verify/code -d'{
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

    curl -XPOST localhost:8091/api/authService/v1/logout -d'{
        "providerId": "$PROVIDER_ID",
        "token": "$TOKEN"
    }'

If the logout is successful (or the token already expired) the response is:

    200 OK

In case of an error the response is:

    400 {"version":"1.0","status":"NOK","errorType":"LogoutError","errorMessage":"logout failed"}


## Configuration

### OpenID Connect Providers

Configuring OpenID Connect providers requires two parts:

* generic provider config
* context specific config

A context can be `trackle` or `trackle-dev` for example. Here's an example for a config with one provider used in three
different contexts with only two activate ones (including comments with further explanations):

    ubirchAuthService {

      openIdConnect {

        provider {

          google { # referenced later in `ubirchAuthService.openIdConnect.context.$CONTEXT_NAME.providers`
            name = "Google"
            scope = "openid"
            endpointConfig = "https://accounts.google.com/.well-known/openid-configuration"
            tokenSigningAlgorithms = ["RS256"]
            endpoints {
              authorization = "https://accounts.google.com/o/oauth2/v2/auth"
              token = "https://www.googleapis.com/oauth2/v4/token"
              jwks = "https://www.googleapis.com/oauth2/v3/certs"
            }
          }

        }

        context {

          activeList = ["trackle", "trackle-dev"] # querying `/providerInfo/list` only the contexts in this list are returned

          trackle { # same key is in `ubirchAuthService.openIdConnect.context.activeList`

            providers = ["google"] # list of providers activated for this context (same keys as in `ubirchAuthService.openIdConnect.provider`)

            google { # provider as configured in `ubirchAuthService.openIdConnect.context.trackle.providers`
              clientId = "clientId_for_context_trackle"
              clientSecret = "secret_for_context_trackle"
              callbackUrl = "https://trackle.ubirch.com:9000/oidc-callback-trackle"
            }

          }

          trackle-dev { # same key is in `ubirchAuthService.openIdConnect.context.activeList`

          providers = ["google"] # list of providers activated for this context (same keys as in `ubirchAuthService.openIdConnect.provider`)

            google { # provider as configured in `ubirchAuthService.openIdConnect.context.trackle-dev.providers`
              clientId = "clientId_for_context_trackle-dev"
              clientSecret = "secret_for_context_trackle-dev"
              callbackUrl = "http://localhost:10000/oidc-callback-trackle-dev"
            }

          }

          projectX-dev { # key is not in `ubirchAuthService.openIdConnect.context.activeList` --> not returned by `/providerInfo/list`

            providers = ["google"] # list of providers activated for this context (same keys as in `ubirchAuthService.openIdConnect.provider`)

            google { # provider as configured in `ubirchAuthService.openIdConnect.context.projectX-dev.providers`
              clientId = "clientId_for_context_projectX"
              clientSecret = "secret_for_context_projectX"
              callbackUrl = "http://localhost:10000/oidc-callback-projectX-dev"
            }

          }

        }

      }

    }


### Redis

Since we're using the [rediscala library|https://github.com/etaty/rediscala] we can use it's
[configuration options|https://github.com/etaty/rediscala/blob/master/src/main/resources/reference.conf]. 


## Deployment Notes

This service has the following dependencies:
 
* Redis 3.2.x (verification needed; definitely works with 3.2.7 and 3.2.8)


## Automated Tests

TODO (if necessary)


## Local Setup

1) Start [Redis|https://redis.io/] on the default port (tested with version 3.2.7 and 3.2.8)

2) Start AuthService by running:

    ./sbt server/run


## Create Docker Image

    ./sbt server/docker
