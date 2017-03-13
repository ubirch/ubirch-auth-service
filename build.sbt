packagedArtifacts in file(".") := Map.empty // disable publishing of root/default project

// see http://www.scala-sbt.org/0.13/docs/Parallel-Execution.html for details
concurrentRestrictions in Global := Seq(
  Tags.limit(Tags.Test, 1)
)

val projectVersion = "0.0.1-SNAPSHOT"
lazy val commonSettings = Seq(

  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-feature"),
  organization := "com.ubirch.auth",

  homepage := Some(url("http://ubirch.com")),
  scmInfo := Some(ScmInfo(
    url("https://github.com/ubirch/ubirch-auth-service"),
    "scm:git:git@github.com:ubirch/ubirch-auth-service.git"
  )),
  version := "0.0.1-SNAPSHOT",
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
  .aggregate(server, cmdtools, config, core, openIdUtil, model, modelDb, util)

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
      generateDockerFile(baseDirectory.value / ".." / "Dockerfile", name.value, version.value, (assemblyOutputPath in assembly).value)
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
  .dependsOn(config, modelDb)
  .settings(
    description := "command line tools",
    libraryDependencies ++= depCmdtools
  )

lazy val core = project
  .settings(commonSettings: _*)
  .dependsOn(model, modelDb, util, openIdUtil)
  .settings(
    description := "business logic",
    libraryDependencies ++= depCore
  )

lazy val openIdUtil = (project in file("openid-util"))
  .settings(commonSettings: _*)
  .dependsOn(config, modelDb)
  .settings(
    name := "openid-util",
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

lazy val util = project
  .settings(commonSettings: _*)
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

  ubirchUtilJsonAutoConvert,
  ubirchUtilResponse

)

lazy val depCmdtools = Seq(
  rediscala,
  akkaActor,
  akkaSlf4j,
  json4sNative,
  ubirchUtilJsonAutoConvert
) ++ scalaLogging

lazy val depCore = Seq(
  akkaActor,
  rediscala,
  ubirchUtilResponse,
  json4sNative,
  ubirchUtilJson,
  ubirchUtilFutures,
  scalatest % "test"
) ++ scalaLogging

lazy val depOpenIdUtil = Seq(
  nimbusOidc
) ++ scalaLogging

lazy val depModel = Seq(
  ubirchUtilJsonAutoConvert,
  json4sNative
)

lazy val depUtils = Seq(
  ubirchUtilCrypto
)

/*
 * DEPENDENCIES
 ********************************************************/

// VERSIONS
val akkaV = "2.4.17"
val akkaHttpV = "10.0.3"
val json4sV = "3.4.2"

val scalaTestV = "3.0.1"

// GROUP NAMES
val ubirchUtilG = "com.ubirch.util"
val json4sG = "org.json4s"

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

lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaV
lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpV
lazy val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaV

lazy val nimbusOidc = "com.nimbusds" % "oauth2-oidc-sdk" % "5.22"

lazy val rediscala = "com.github.etaty" %% "rediscala" % "1.8.0" excludeAll(
  ExclusionRule(organization = "com.typesafe.akka")
)

lazy val ubirchUtilConfig = ubirchUtilG %% "config" % "0.1" excludeAll(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback")
)

lazy val ubirchUtilCrypto = ubirchUtilG %% "crypto" % "0.3.3" excludeAll(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback")
)
lazy val ubirchUtilRestAkkaHttp = ubirchUtilG %% "rest-akka-http" % "0.3.3" excludeAll(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback")
)
lazy val ubirchUtilRestAkkaHttpTest = ubirchUtilG %% "rest-akka-http-test" % "0.3.3" excludeAll(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback")
)
lazy val ubirchUtilResponse = ubirchUtilG %% "response-util" % "0.1.2" excludeAll(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback")
)
lazy val ubirchUtilJson = ubirchUtilG %% "json" % "0.3.2" excludeAll(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback")
)
lazy val ubirchUtilJsonAutoConvert = ubirchUtilG %% "json-auto-convert" % "0.3.2" excludeAll(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback")
)
lazy val ubirchUtilFutures = ubirchUtilG %% "futures" % "0.1.0" excludeAll(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback")
)

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

def generateDockerFile(file: File, nameString: String, versionString: String, jarFile: sbt.File): Seq[File] = {
  val jarTargetPath = s"/opt/jar/${jarFile.name}"
  val contents =
    s"""FROM ubirch/java
       		  |RUN mkdir -p /opt/ubirch/etc
       	 |ADD server/target/scala-2.11/${jarFile.getName} $jarTargetPath
       		  |ADD config/src/main/resources/application.docker.conf /opt/ubirch/etc/application.conf
       		  |ADD config/src/main/resources/logback.docker.xml /opt/ubirch/etc/logback.xml
       |EXPOSE 8080
       	 |ENTRYPOINT ["java","-Dlogback.configurationFile=/opt/ubirch/etc/logback.xml", "-Dconfig.file=/opt/ubirch/etc/application.conf","-jar", "$jarTargetPath","-Dfile.encoding=UTF-8", "-XX:+UseCMSInitiatingOccupancyOnly","-XX:+DisableExplicitGC","-XX:CMSInitiatingOccupancyFraction=75", "-XX:+UseParNewGC","-XX:+UseConcMarkSweepGC", "-Xms1g", "-Xmx2g", "-Djava.awt.headless=true"]
       	 |""".stripMargin
  IO.write(file, contents)
  Seq(file)
}

