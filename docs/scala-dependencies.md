## Scala Dependencies

### `client-rest`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "client-rest" % "0.2.22-SNAPSHOT"
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
  "com.ubirch.auth" %% "cmdtools" % "0.2.22-SNAPSHOT"
)
```### `config`


### `config`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "config" % "0.2.22-SNAPSHOT"
)
```

### `core`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "core" % "0.2.22-SNAPSHOT"
)
```

### `model`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "model" % "0.2.22-SNAPSHOT"
)
```

### `model-db`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "model-db" % "0.2.22-SNAPSHOT"
)
```

### `oidc-util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "oidc-util" % "0.2.22-SNAPSHOT"
)
```

### `server`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "server" % "0.2.22-SNAPSHOT"
)
```

### `test-tools`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "test-tools" % "0.2.22-SNAPSHOT"
)
```

### `test-tools-ext`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("hseeberger", "maven")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "test-tools-ext" % "0.2.22-SNAPSHOT"
)
```

### `util`

```scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies ++= Seq(
  "com.ubirch.auth" %% "util" % "0.2.22-SNAPSHOT"
)
```
