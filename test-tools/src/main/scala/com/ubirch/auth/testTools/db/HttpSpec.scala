package com.ubirch.auth.testTools.db

import akka.actor.ActorSystem
import akka.http.scaladsl.{Http, HttpExt}
import akka.stream.ActorMaterializer

/**
  * author: cvandrei
  * since: 2018-01-19
  */
trait HttpSpec {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val httpClient: HttpExt = Http()

}
