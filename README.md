# ubirch-login-service


## General Information

Not wanting to implement all aspects of a user management logins are done with OpenID Connect. This service bundles
functionalities related to this.

The ubirch Login Service is responsible for:

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
  "com.ubirch.login" %% "config" % "0.0.1-SNAPSHOT"
)
```

### `core`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.login" %% "core" % "0.0.1-SNAPSHOT"
)
```

### `model`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.login" %% "model" % "0.0.1-SNAPSHOT"
)
```

### `openid-util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.login" %% "openid-util" % "0.0.1-SNAPSHOT"
)
```

### `server`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.login" %% "server" % "0.0.1-SNAPSHOT"
)
```

### `util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.login" %% "util" % "0.0.1-SNAPSHOT"
)
```


## REST Methods

### Welcome / Health

    curl localhost:8091/
    curl localhost:8091/api/loginService/v1

If healthy the server response is:

    200 {"version":"1.0","status":"OK","message":"Welcome to the ubirchLoginService"}


### Provider Infos

Gives us a list of configured providers.

    curl localhost:8091/api/loginService/v1/providerInfo/list
    

## Configuration

### OpenID Connect Providers

We can configure as many OpenID Connect providers as we want by using the following pattern:

    ubirchLoginService {

      openIdConnectProviders {

        providerList = ["generic1", "generic2"]

        openIdConnectProviders {

          generic1 {
            name = "Generic-1"
            scope = "openid"
            logoUrl = "https://example.com/logo-generic1.jpg"
            clientId = "1234"
            loginUrl = "https://example.com/login-generic1"
            callbackUrl = "http://client.com/callback-generic1"
          }
        }

      }

    }

Provider configs are read dynamically based on the list defined in
_ubirchLoginService.openIdConnectProviders.providerList_. When adding a new one please don't forget to add it's name to
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
