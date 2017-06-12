packagedArtifacts in file(".") := Map.empty // disable publishing of root/default project

// see http://www.scala-sbt.org/0.13/docs/Parallel-Execution.html for details
concurrentRestrictions in Global := Seq(
  Tags.limit(Tags.Test, 1)
)

lazy val commonSettings = Seq(

  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-feature"),
  organization := "com.ubirch.auth",

  homepage := Some(url("http://ubirch.com")),
  scmInfo := Some(ScmInfo(
    url("https://github.com/ubirch/ubirch-auth-service"),
    "scm:git:git@github.com:ubirch/ubirch-auth-service.git"
  )),
  version := "0.2.4",
  test in assembly := {},
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )

)

/*
 * MODULES
 ********************************************************/

lazy val authService = (project in file("."))
  .settings(commonSettings: _*)
  .aggregate(
    server,
    cmdtools,
    config,
    core,
    oidcUtil,
    model,
    modelDb,
    testTools,
    util
  )

lazy val server = project
  .settings(commonSettings: _*)
  .settings(mergeStrategy: _*)
  .dependsOn(config, core, model, util)
  .enablePlugins(DockerPlugin)
  .settings(
    description := "REST interface and Akka HTTP specific code",
    libraryDependencies ++= depServer,
    fork in run := true,
    resolvers ++= Seq(
      resolverSeebergerJson
    ),
    mainClass in(Compile, run) := Some("com.ubirch.auth.server.Boot"),
    resourceGenerators in Compile += Def.task {
      generateDockerFile(baseDirectory.value / ".." / "Dockerfile.input", (assemblyOutputPath in assembly).value)
    }.taskValue
  )

lazy val config = project
  .settings(commonSettings: _*)
  .settings(
    description := "trackle specific config and config tools",
    libraryDependencies += ubirchUtilConfig
  )

lazy val cmdtools = project
  .settings(commonSettings: _*)
  .dependsOn(config, modelDb, testTools, testToolsExt)
  .settings(
    description := "command line tools"
  )

lazy val core = project
  .settings(commonSettings: _*)
  .dependsOn(model, modelDb, util, oidcUtil, testTools % "test")
  .settings(
    description := "business logic",
    libraryDependencies ++= depCore
  )

lazy val oidcUtil = (project in file("oidc-util"))
  .settings(commonSettings: _*)
  .dependsOn(config, modelDb)
  .settings(
    name := "oidc-util",
    description := "OpenID Connect utils",
    libraryDependencies ++= depOpenIdUtil
  )

lazy val model = project
  .settings(commonSettings: _*)
  .settings(
    description := "JSON models"
  )

lazy val modelDb = (project in file("model-db"))
  .settings(commonSettings: _*)
  .settings(
    name := "model-db",
    description := "database models"
  )

lazy val testTools = (project in file("test-tools"))
  .settings(commonSettings: _*)
  .dependsOn(config, modelDb, util)
  .settings(
    name := "test-tools",
    description := "tools useful in automated tests",
    libraryDependencies ++= depTestTools
  )

lazy val testToolsExt = (project in file("test-tools-ext"))
  .settings(commonSettings: _*)
  .dependsOn(core)
  .settings(
    name := "test-tools-ext",
    description := "tools useful in automated tests (not in test-tools to avoid circular dependencies between _test-tools_ and _core_)"
  )

lazy val util = project
  .settings(commonSettings: _*)
  .dependsOn(modelDb)
  .settings(
    description := "utils",
    resolvers ++= Seq(
      roundeightsHasher
    ),
    libraryDependencies ++= depUtils
  )

/*
 * MODULE DEPENDENCIES
 ********************************************************/

lazy val depServer = Seq(

  akkaSlf4j,
  akkaHttp,
  ubirchUtilRestAkkaHttp,
  ubirchUtilRestAkkaHttpTest % "test",

  ubirchUtilMongo,

  ubirchUtilResponse

)

lazy val depCore = Seq(
  akkaActor,
  json4sNative,
  ubirchUtilFutures,
  ubirchUtilJson,
  ubirchUtilOidcUtils,
  ubirchUtilRedisUtil,
  ubirchUtilResponse,
  ubirchUserCore,
  ubirchUserTestToolsExt % "test",
  scalatest % "test"
) ++ scalaLogging

lazy val depOpenIdUtil = Seq(
  nimbusOidc
) ++ scalaLogging

lazy val depModel = Seq(
  json4sNative
)

lazy val depTestTools = Seq(
  json4sNative,
  ubirchUtilRedisTestUtils,
  ubirchUtilMongoTestUtils,
  scalatest
) ++ scalaLogging

lazy val depUtils = Seq(
  ubirchUtilCrypto,
  ubirchUtilJson,
  ubirchUtilFutures,
  rediscala
) ++ scalaLogging

/*
 * DEPENDENCIES
 ********************************************************/

// VERSIONS
val akkaV = "2.4.18"
val akkaHttpV = "10.0.6"
val json4sV = "3.5.1"
val ubirchUserV = "0.4.9"

val scalaTestV = "3.0.1"

// GROUP NAMES
val ubirchUtilG = "com.ubirch.util"
val ubirchUserG = "com.ubirch.user"
val json4sG = "org.json4s"
val akkaG = "com.typesafe.akka"

lazy val scalatest = "org.scalatest" %% "scalatest" % scalaTestV

lazy val json4sNative = json4sG %% "json4s-native" % json4sV

lazy val scalaLogging = Seq(
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2" exclude("org.slf4j", "slf4j-api"),
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0" exclude("org.slf4j", "slf4j-api"),
  "ch.qos.logback" % "logback-core" % "1.1.7",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.internetitem" % "logback-elasticsearch-appender" % "1.4"
)

lazy val akkaActor = akkaG %% "akka-actor" % akkaV
lazy val akkaHttp = akkaG %% "akka-http" % akkaHttpV
lazy val akkaSlf4j = akkaG %% "akka-slf4j" % akkaV

lazy val nimbusOidc = "com.nimbusds" % "oauth2-oidc-sdk" % "5.26"

lazy val rediscala = "com.github.etaty" %% "rediscala" % "1.8.0" excludeAll ExclusionRule(organization = "com.typesafe.akka")

lazy val excludedLoggers = Seq(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback")
)

lazy val ubirchUtilConfig = ubirchUtilG %% "config" % "0.1" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilCrypto = ubirchUtilG %% "crypto" % "0.3.3" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilDeepCheckModel = ubirchUtilG %% "deep-check-model" % "0.1.0" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilFutures = ubirchUtilG %% "futures" % "0.1.1" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilJson = ubirchUtilG %% "json" % "0.4.0" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilMongo = ubirchUtilG %% "mongo-utils" % "0.3.0" excludeAll(
  excludedLoggers++ Seq(ExclusionRule(organization = akkaActor.organization, name = akkaActor.name)): _*
  )
lazy val ubirchUtilMongoTestUtils = ubirchUtilG %% "mongo-test-utils" % "0.3.0" excludeAll(
  excludedLoggers++ Seq(ExclusionRule(organization = akkaActor.organization, name = akkaActor.name)): _*
  )
lazy val ubirchUtilOidcUtils = ubirchUtilG %% "oidc-utils" % "0.4.5" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilRedisTestUtils = ubirchUtilG %% "redis-test-util" % "0.3.0" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilRedisUtil = ubirchUtilG %% "redis-util" % "0.3.0" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilRestAkkaHttp = ubirchUtilG %% "rest-akka-http" % "0.3.7" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilRestAkkaHttpTest = ubirchUtilG %% "rest-akka-http-test" % "0.3.7" excludeAll(excludedLoggers: _*)
lazy val ubirchUtilResponse = ubirchUtilG %% "response-util" % "0.2.0" excludeAll(excludedLoggers: _*)
lazy val ubirchUserCore = ubirchUserG %% "core" % ubirchUserV
lazy val ubirchUserTestToolsExt = ubirchUserG %% "test-tools-ext" % ubirchUserV

/*
 * RESOLVER
 ********************************************************/

lazy val resolverSeebergerJson = Resolver.bintrayRepo("hseeberger", "maven")
lazy val roundeightsHasher = "RoundEights" at "http://maven.spikemark.net/roundeights"

/*
 * MISC
 ********************************************************/

lazy val mergeStrategy = Seq(
  assemblyMergeStrategy in assembly := {
    case PathList("org", "joda", "time", xs@_*) => MergeStrategy.first
    case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
    case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
    case m if m.toLowerCase.endsWith("application.conf") => MergeStrategy.concat
    case m if m.toLowerCase.endsWith("application.dev.conf") => MergeStrategy.first
    case m if m.toLowerCase.endsWith("application.base.conf") => MergeStrategy.first
    case m if m.toLowerCase.endsWith("logback.xml") => MergeStrategy.first
    case m if m.toLowerCase.endsWith("logback-test.xml") => MergeStrategy.discard
    case "reference.conf" => MergeStrategy.concat
    case _ => MergeStrategy.first
  }
)

def generateDockerFile(file: File, jarFile: sbt.File): Seq[File] = {
  val contents =
    s"""SOURCE=server/target/scala-2.11/${jarFile.getName}
       |TARGET=${jarFile.getName}
       |""".stripMargin
  IO.write(file, contents)
  Seq(file)
}
