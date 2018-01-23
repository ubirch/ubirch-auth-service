## Release History

### Version 0.2.19 (2018-01-22)

* update to `com.ubirch.user:*:0.6.4`
* update to `com.ubirch.util:oidc-utils:0.4.14`
* added `AuthServiceClientRest`
* update to `com.ubirch.util:crypto:0.4.2`

### Version 0.2.18 (2017-09-18)

* update to `com.ubirch.user:*:0.6.3`
* admin-ui in dev environment now has https 
* added user-service related key to docker conf: `ubirchUserService.providersWithUsersActivated`
* change display name of Keycload OIDC provider to `ubirch`

### Version 0.2.17 (2017-09-14)

* remove deprecated endpoint _GET /providerInfo/list/$CONTEXT_
* improve performance by not performing an unnecessary http request in _AuthRequest.redirectUrl()_

### Version 0.2.16 (2017-09-14)

* update to `com.ubirch.util:oidc-utils:0.4.11`
* moved some of README's documentation to separate files in newly created folder _docs_
* fixed bug where we failed to read the user name and language claims from our own Keycloak response
* added Keycloak related OpenID Connect configs

### Version 0.2.15 (2017-08-09)

* add trackle related clientIds and clientSecrets for demo environment
* add route `providerInfo/list/$CONTEXT/$APP_ID`
* refactored the now deprecated `providerInfo/list/$CONTEXT` path to be compatible with it's replacement (with `/$APP_ID` postfix)
* add _appId_ field to input of `/verify/code` (which defaults to "legacy" until we can remove the `providerInfo/list/$CONTEXT/$APP_ID` route
* update to `com.ubirch.user:*:0.6.1`

### Version 0.2.14 (2017-07-31)

* fixed broken logging (would lead to a Throwable not being logged)
* update to `com.ubirch.util:deep-check-model:0.2.0`
* update to `com.ubirch.util:mongo-utils:0.3.6`
* update to `com.ubirch.util:mongo-test-utils:0.3.6`
* update to `com.ubirch.util:redis-util:0.3.4`
* update to `com.ubirch.util:redis-test-util:0.3.4`
* update to `com.ubirch.util:oidc-utils:0.4.10`
* update to `com.ubirch.user:*:0.5.1`

### Version 0.2.13 (2017-07-27)

* configure trackle-admin-ui-* contexts for dev and demo (except for the client secret)
* update to `com.ubirch.util:json:0.4.3`
* update to `com.ubirch.util:deep-check-model:0.1.3`
* update to `com.ubirch.util:response-util:0.2.4`
* update to `com.ubirch.util:mongo-test-utils:0.3.5`
* update to `com.ubirch.util:mongo-utils:0.3.5`
* update to `com.ubirch.util:redis-test-util:0.3.3`
* update to `com.ubirch.util:redis-util:0.3.3`
* update to `com.ubirch.util:oidc-utils:0.4.9`
* update to `com.ubirch.user:*:0.4.19`

### Version 0.2.12 (2017-07-19)

* update to `com.nimbusds:oauth2-oidc-sdk:5.30`

### Version 0.2.11 (2017-07-19)

* improved logging when creating provider and context related configs during server boot

### Version 0.2.10 (2017-07-18)

* update to Akka 2.4.19
* update to `com.ubirch.user:*:0.4.17`
* update _com.ubirch.util:mongo(-test)-utils_ to 0.3.4

### Version 0.2.9 (2017-07-17)

* update Akka HTTP to 10.0.9
* update _com.ubirch.util:rest-akka-http(-test)_ to 0.3.8
* update _com.ubirch.util:response-util_ to 0.2.3
* update _com.ubirch.util:oidc-utils_ to 0.4.8
* update _com.ubirch.user:*_ to 0.4.15

### Version 0.2.8 (2017-07-13)

* update logging dependencies
* update logback configs
* update _com.ubirch.util:mongo(-test)-utils_ to 0.3.3
* update _com.ubirch.user:*_ to 0.4.14

### Version 0.2.7 (2017-06-29)

* add scripts `dev-scripts/resetDatabase.sh` and `dev-scripts/createDevtoken.sh`
* add _props()_ method to actors
* updated to _com.ubirch.util:json:0.4.2_ and all ubirch util libs depending on it, too

### Version 0.2.6 (2017-06-22)

* endpoint `GET /userInfo` now responds with errorType _NoUserInfoFound_
* updated curl call documentation in README

### Version 0.2.5 (2017-06-19)

* update json4s to 3.5.2
* update to _com.ubirch.util:json:0.4.1_
* update to _com.ubirch.util:deep-check-model:0.1.1_
* update to _com.ubirch.util:response-util:0.2.1_
* update to _com.ubirch.util:mongo-utils:0.3.1_
* update to _com.ubirch.util:mongo-test-utils:0.3.1_
* update to _com.ubirch.util:redis-util:0.3.1_
* update to _com.ubirch.util:redis-test-util:0.3.1_
* update to _com.ubirch.util:oidc-utils:0.4.6_
* update to _com.ubirch.util:response-util:0.2.1_
* update to _com.ubirch.user:*:0.4.10_

### Version 0.2.4 (2017-06-12)

* endpoint `/api/userService/v1/deepCheck` responds with http status 503 if deep check finds problems
* update _com.ubirch.user:*_ to 0.4.9

### Version 0.2.3 (2017-06-09)

* migrate to _com.ubirch.util:deep-check-model:0.1.0_
* update to Akka 2.4.18
* update _com.ubirch.user:*_ to 0.4.8

### Version 0.2.2 (2017-06-08)

* introduce endpoint `/api/authService/v1/check`
* update to sbt 0.13.15
* update _com.ubirch.util:json_ to version 0.4.0
* update _com.ubirch.util:oidc-utils_ to version 0.4.4
* update _com.ubirch.util:response-util_ to version 0.1.6
* update _com.ubirch.util:mongo-test-utils_ to version 0.2.3
* update _com.ubirch.util:mongo-utils_ to version 0.2.3
* update _com.ubirch.util:redis-test-utils_ to version 0.2.3
* update _com.ubirch.util:redis-utils_ to version 0.2.3
* update _com.ubirch.user:*_ to 0.4.7
* introduce endpoint `/api/userService/v1/deepCheck`

### Version 0.2.1 (2017-05-31)

* added activeUser flag to userInfo
* updated to ubirchUserService 0.4.5

### Version 0.2.0 (2017-05-29)

* update _com.ubirch.user:*_ to 0.4.1
* added optional boolean field: `UserInfoGroup.adminGroup`
* update _com.nimbusds:oauth2-oidc-sdk_ to version 5.26

### Version 0.1.2 (2017-05-29)

* change default test user created by `CreateDevToken`
* update _com.ubirch.user:*_ to 0.3.2

### Version 0.1.1 (2017-05-22)

* update Akka Http to 10.0.6
* update to Akka 2.4.18
* update _com.ubirch.util:rest-akka-http_ to 0.3.7
* update _com.ubirch.util:rest-akka-http-test_ to 0.3.7
* update _com.ubirch.util:oidc-utils_ to 0.4.1
* update _com.ubirch.util:redis-test-util_ to 0.2.2
* update _com.ubirch.util:redis-util_ to 0.2.2
* update _com.ubirch.util:response-util_ to 0.1.4
* update _com.ubirch.user:*_ to 0.3.1

### Version 0.1.0 (2017-05-05)

* initial release
