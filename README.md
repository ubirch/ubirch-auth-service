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

    curl localhost:8080/
    curl localhost:8080/api/loginService/v1

If healthy the server response is:

    200 {"version":"1.0","status":"OK","message":"Welcome to the ubirchLoginService"}


## Configuration

TODO


## Deployment Notes

TODO


## Automated Tests

TODO


## Local Setup

TODO


## Create Docker Image

    ./sbt server/docker
