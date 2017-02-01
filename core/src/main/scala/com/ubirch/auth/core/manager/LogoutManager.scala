package com.ubirch.auth.core.manager

import com.ubirch.auth.model.Logout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

/**
  * author: cvandrei
  * since: 2017-02-01
  */
object LogoutManager {

  def logout(logout: Logout): Future[Boolean] = {

    // TODO delete logout.token from token/userId cache
    // TODO call provider's logout method where necessary

    // TODO return true if deletion was successful
    Future(Random.nextBoolean())

  }

}
