package com.ubirch.auth.core.manager

import com.ubirch.auth.testTools.db.redis.RedisSpec

/**
  * author: cvandrei
  * since: 2017-03-14
  */
class ProviderInfoManagerSpec extends RedisSpec {

  feature("providerInfoList()") {

    // TODO define test cases

    ignore("context does not exist") {
      // TODO
    }

    ignore("context exists and has providers --> return providers") {
      // TODO
    }

    ignore("context exists but without providers (none configured) --> return no providers") {
      // TODO
    }

    ignore("context exists but without providers (none activated) --> return no providers") {
      // TODO
    }

    ignore("context exists and has (inactive) providers --> return no providers") {
      // TODO
    }

    ignore("context exists, provider is enabled but provider config is missing --> return no providers") {
      // TODO
    }

    ignore("context exists and has providers but context is not enabled --> return no providers") {
      // TODO
    }

  }

}
