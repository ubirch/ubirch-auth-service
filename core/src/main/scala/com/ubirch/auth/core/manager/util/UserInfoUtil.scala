package com.ubirch.auth.core.manager.util

import com.ubirch.auth.model.UserInfoGroup
import com.ubirch.user.model.db.Group
import com.ubirch.util.uuid.UUIDUtil

/**
  * author: cvandrei
  * since: 2017-04-25
  */
object UserInfoUtil {

  def toUserInfoGroups(groups: Set[Group]): Set[UserInfoGroup] = {

    // TODO refactor: move to user-service
    // TODO automated tests
    groups map { group =>
      UserInfoGroup(
        id = UUIDUtil.fromString(group.id),
        displayName = group.displayName,
        adminGroup = group.adminGroup
      )
    }

  }

}
