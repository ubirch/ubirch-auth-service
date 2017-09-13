## Local Setup

1) Start [Redis|https://redis.io/] on the default port (tested with version 3.2.7 and 3.2.8)

2) Configure OpenConnectID Providers

*Running `dev-scripts/resetDatabase.sh` does everything in this step.*

If you still have old data you want to delete first please run

    ./sbt "cmdtools/runMain com.ubirch.auth.cmd.RedisDelete"

To init default providers and contexts for development please run

    ./sbt "cmdtools/runMain com.ubirch.auth.cmd.InitData"

generated contexts

* trackle-local
* trackle-admin-ui-local
* ubirch-admin-ui-local
* ubirch-admin-ui-dev
* trackle-admin-ui-dev
* ubirch-admin-ui-demo
* trackle-admin-ui-demo

3) Start MongoDB

The `/register` and `/userInfo` calls use managers from the user-service and hence depend on MongoDB. Please follow the
local setup section in the user-service's README, too.

4) Start AuthService

    ./sbt server/run

5) (optional) Create Test User Token

*Everything in this step can be done by running `dev-scripts/createDevtoken.sh`, too.*

This step saves us from having to login with any of the OpenID Connect providers. To get a valid test user token run:

    ./sbt "cmdtools/runMain com.ubirch.auth.cmd.CreateDevToken"

The token, userId and context are printed out and can be changed with the following configuration keys:

* ubirchAuthService.testUser.token
* ubirchAuthService.testUser.providerId
* ubirchAuthService.testUser.userId
* ubirchAuthService.testUser.context
* ubirchAuthService.testUser.userName
* ubirchAuthService.testUser.locale

All tokens expire at some point. This TTL (time-to-live) can be configured as well. Whenever you use the token
successfully (calling a Bearer token protected REST resource) the expiry date will be reset.
