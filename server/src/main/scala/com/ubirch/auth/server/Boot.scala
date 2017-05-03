package com.ubirch.auth.server

import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.{Config, ConfigKeys}
import com.ubirch.auth.server.route.MainRoute
import com.ubirch.auth.util.db.config.{OidcContextProviderUtil, OidcProviderUtil}
import com.ubirch.user.core.manager.ContextManager
import com.ubirch.user.model.db.Context
import com.ubirch.util.mongo.connection.MongoUtil
import com.ubirch.util.redis.RedisClientUtil

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import redis.RedisClient

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * author: cvandrei
  * since: 2017-01-19
  */
object Boot extends App with StrictLogging {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val timeout = Timeout(Config.timeout seconds)

  implicit val mongo: MongoUtil = new MongoUtil(ConfigKeys.MONGO_PREFIX)
  implicit val redis: RedisClient = RedisClientUtil.getRedisClient()

  val bindingFuture = start()
  registerShutdownHooks()

  private def start(): Future[ServerBinding] = {

    val interface = Config.interface
    val port = Config.port
    implicit val timeout = Timeout(5, TimeUnit.SECONDS)

    OidcProviderUtil.initProviders()
    OidcContextProviderUtil.initContexts()

    prepareMongo() map { mongoPrepareStatus =>
      logger.info(s"======= Mongo Prepare: status=$mongoPrepareStatus")
    }

    logger.info(s"start http server on $interface:$port")
    Http().bindAndHandle((new MainRoute).myRoute, interface, port)

  }

  private def registerShutdownHooks() = {

    Runtime.getRuntime.addShutdownHook(new Thread() {

      override def run(): Unit = {

        bindingFuture
          .flatMap(_.unbind())
          .onComplete(_ => system.terminate())

        mongo.close()

        redis.stop()

      }

    })

  }

  private def prepareMongo()(implicit mongo: MongoUtil): Future[Boolean] = {


    for {
      contextUbirchAdminUIDev <- ContextManager.create(Context(displayName = "ubirch-admin-ui-dev"))
      contextUbirchAdminUIDemo <- ContextManager.create(Context(displayName = "ubirch-admin-ui-demo"))
    } yield {

      if (contextUbirchAdminUIDev.isDefined) {
        logger.info(s"====== Mongo Context: created (contextName=${contextUbirchAdminUIDev.get.displayName})")
      } else {
        logger.error(s"====== Mongo Context: failed to create (contextName=${contextUbirchAdminUIDev.get.displayName})")
      }

      if (contextUbirchAdminUIDemo.isDefined) {
        logger.info(s"====== Mongo Context: created (contextName=${contextUbirchAdminUIDemo.get.displayName})")
      } else {
        logger.error(s"====== Mongo Context: failed to create (contextName=${contextUbirchAdminUIDemo.get.displayName})")
      }

      contextUbirchAdminUIDev.isDefined && contextUbirchAdminUIDemo.isDefined

    }

  }

}
