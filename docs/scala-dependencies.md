## Scala Dependencies

### `client-rest`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "client-rest" % "0.4.3-SNAPSHOT
)
```

#### Configuration
   
| Config Item                        | Mandatory  | Description       |
|:-----------------------------------|:-----------|:------------------|
| ubirchAuthService.client.rest.host | yes        | key-service host  |

#### Usage

The REST client class is `KeyServiceClientRest` and the host it connects to needs to be configured:

    ubirchAuthService.client.rest.host = "http://localhost:8091"


### `cmdtools`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "cmdtools" % "0.4.3-SNAPSHOT
)
```### `config`


### `config`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "config" % "0.4.3-SNAPSHOT
)
```

### `core`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "core" % "0.4.3-SNAPSHOT
)
```

### `model`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "model" % "0.4.3-SNAPSHOT
)
```

### `model-db`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "model-db" % "0.4.3-SNAPSHOT
)
```

### `oidc-util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "oidc-util" % "0.4.3-SNAPSHOT
)
```

### `server`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "server" % "0.4.3-SNAPSHOT
)
```

### `test-tools`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "test-tools" % "0.4.3-SNAPSHOT
)
```

### `test-tools-ext`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "test-tools-ext" % "0.4.3-SNAPSHOT
)
```

### `util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "util" % "0.4.3-SNAPSHOT
)
```
