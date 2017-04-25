package com.ubirch.auth.core.manager.util

import com.ubirch.auth.model.UserInfoGroup
import com.ubirch.user.model.db.Group

/**
  * author: cvandrei
  * since: 2017-04-25
  */
object UserInfoUtil {

  def toUserInfoGroups(groups: Set[Group]): Set[UserInfoGroup] = {

    // TODO automated tests
    groups map { group =>
      UserInfoGroup(group.id, group.displayName)
    }

  }

}
