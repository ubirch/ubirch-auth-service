package com.ubirch.auth.core.manager

import com.ubirch.auth.model.{AfterLogin, Token}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object TokenManager {

  def verifyCode(afterLogin: AfterLogin): Future[Option[Token]] = {

//    val redis = Redis()
//    val key = s"state:${afterLogin.providerId}:${afterLogin.state}"
//    redis.exists(key) onComplete {
//
//      case Success(stateExists) =>
//
//        if (stateExists) {
//          // TODO verify "afterLogin.code" w/ OpenID Connect provider
//          // TODO remember token that we get from OpenID Connect provider
//        } else {
//          // TODO return error
//        }
//
//      case Failure(e) =>
//        // TODO return server error
//
//    }

    Random.nextBoolean() match {
      case true => Future(Some(Token(s"${afterLogin.providerId}-${afterLogin.code}-${afterLogin.state}")))
      case false => Future(None)
    }

  }

}
