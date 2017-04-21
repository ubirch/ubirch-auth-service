package com.ubirch.auth.model

/**
  * author: cvandrei
  * since: 2017-04-20
  */
case class UserInfo(displayName: String,
                    myGroups: Set[String] = Set.empty,
                    allowedGroups: Set[String] = Set.empty
                   )
