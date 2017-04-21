package com.ubirch.auth.model

import java.util.UUID

/**
  * author: cvandrei
  * since: 2017-04-20
  */
case class UserInfo(displayName: String,
                    myGroups: Set[UserInfoGroup] = Set.empty,
                    allowedGroups: Set[UserInfoGroup] = Set.empty
                   )

case class UserInfoGroup(id: UUID,
                         displayName: String
                        )
