package com.ubirch.auth.server

import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config
import com.ubirch.auth.server.route.MainRoute
import com.ubirch.auth.util.db.config.{OidcContextProviderUtil, OidcProviderUtil}
import com.ubirch.util.redis.RedisClientUtil

import akka.actor.ActorSystem
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.{Http, HttpExt}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import redis.RedisClient

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object Boot extends App with StrictLogging {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val httpClient: HttpExt = Http()

  implicit val timeout: Timeout = Timeout(Config.timeout seconds)

  implicit val redis: RedisClient = RedisClientUtil.getRedisClient()

  val bindingFuture = start()
  registerShutdownHooks()

  private def start(): Future[ServerBinding] = {

    val interface = Config.interface
    val port = Config.port
    implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

    OidcProviderUtil.initProviders()
    OidcContextProviderUtil.initContexts()

    logger.info(s"start http server on $interface:$port")
    Http().bindAndHandle((new MainRoute).myRoute, interface, port)

  }

  private def registerShutdownHooks(): Unit = {

    Runtime.getRuntime.addShutdownHook(new Thread() {

      override def run(): Unit = {

        bindingFuture
          .flatMap(_.unbind())
          .onComplete(_ => system.terminate())

        redis.stop()

      }

    })

  }

}
