package com.ubirch.auth.util.server

/**
  * author: cvandrei
  * since: 2017-01-26
  */
object RouteConstants {

  final val apiPrefix = "api"
  final val serviceName = "authService"
  final val currentVersion = "v1"
  final val check = "check"
  final val deepCheck = "deepCheck"

  // $prefix/providerInfo/*
  final val providerInfo = "providerInfo"
  final val list = "list"

  // $prefix/code/*
  final val code = "code"
  final val verify = "verify"

  // $prefix/logout
  final val logout = "logout"

  // $prefix/register
  final val register = "register"

  // $prefix/userInfo
  final val userInfo = "userInfo"

  final val pathPrefix = s"/$apiPrefix/$serviceName/$currentVersion"

  val pathCheck = s"$pathPrefix/$check"
  val pathDeepCheck = s"$pathPrefix/$deepCheck"

  final val pathProviderInfoList = s"$pathPrefix/$providerInfo/$list"
  final val pathCodeVerify = s"$pathPrefix/$code/$verify"
  final val pathLogout = s"$pathPrefix/$logout"
  final val pathRegister = s"$pathPrefix/$register"
  final val pathUserInfo = s"$pathPrefix/$userInfo"

}
