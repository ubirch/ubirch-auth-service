# ubirch-auth-service


## General Information

Not wanting to implement all aspects of a user management logins are done with OpenID Connect. This service bundles the
related functionality.

The ubirch AuthService is responsible for:

* list available OpenID Connect providers
* remember new valid tokens and userIds


## Configuration

### OpenID Connect Providers

OpenID Connect providers are configured in a Redis database requiring two parts:

* generic provider config
* context specific config

For a programmatic example please refer to the class `InitData`. Changes to the defaults used by `InitData` may be made
in:

* `OidcProviders`
* `OidcContextProvider`

#### Generic Provider Config

To configure a provider we have to create an Redis record with the key `oidc.provider.$PROVIDER`. It's value is a JSON
with all context independent information.

    set oidc.provider.google "{\"id\":\"google\",\"name\":\"Google\",\"scope\":\"openid profile\",\"endpointConfig\":\"https://accounts.google.com/.well-known/openid-configuration\",\"tokenSigningAlgorithms\":[\"RS256\"],\"endpoints\":{\"authorization\":\"https://accounts.google.com/o/oauth2/v2/auth\",\"token\":\"https://www.googleapis.com/oauth2/v4/token\",\"jwks\":\"https://www.googleapis.com/oauth2/v3/certs\"}}"

More human readable the JSON looks as follows:

    {
      id = "google", # the $PROVIDER from the key
      name = "Google",
      scope = "openid profile",
      endpointConfig = "https://accounts.google.com/.well-known/openid-configuration",
      tokenSigningAlgorithms = ["RS256"],
      endpoints {
        authorization = "https://accounts.google.com/o/oauth2/v2/auth",
        token = "https://www.googleapis.com/oauth2/v4/token",
        jwks = "https://www.googleapis.com/oauth2/v3/certs"
      }
    }

We can enable a provider by adding it to the list stored in the key `oidc.provider.list` (using the `id` from the
above JSON).

    lpush oidc.provider.list google

#### Context Specific Config

A context can be `trackle-dev` for example and requires it's own `clientId`, `clientSecret` and `callbackUrl`.

To add the config we create a record with the key `oidc.context.$CONTEXT.$APP_ID.$PROVIDER`. It's value is a JSON, too.

    set oidc.context.trackle-dev.admin-ui.google "{\"context\":\"trackle-dev\",\"appId\":\"admin-ui\",\"provider\":\"google\",\"clientId\":\"370115332091-kqf5hu698s4sodrvv03ka3bule530rp5.apps.googleusercontent.com\",\"clientSecret\":\"M86oj4LxV-CcEDd3ougKSbsV\",\"callbackUrl\":\"https://localhost:10000/oidc-callback-google\"}"

More human-readable the JSON looks as follows:

    {
      "context": "trackle-dev", # the $CONTEXT from the key
      "appId": "admin-ui", # the $APP_ID from the key
      "provider": "google", # the $PROVIDER from the key which also has to exist in `oidc.provider.$PROVIDER`
      "clientId": "370115332091-kqf5hu698s4sodrvv03ka3bule530rp5.apps.googleusercontent.com",
      "clientSecret": "M86oj4LxV-CcEDd3ougKSbsV",
      "callbackUrl": "https://localhost:10000/oidc-callback-google"
    }

We can enable a context wby adding it to the set stored in the key `oidc.context.list` (using the `context` from the
above JSON).

    sadd oidc.context.list trackle-dev


### Redis

Since we're using the [rediscala library|https://github.com/etaty/rediscala] we can use it's
[configuration options|https://github.com/etaty/rediscala/blob/master/src/main/resources/reference.conf]. 


## Deployment Notes

This service has the following dependencies:
 
* Redis 3.2.x (verification needed; definitely works with 3.2.7 and 3.2.8)
  * download redis & extract *.tar.gz
  * cd {redis-root}
  * make
  * ./src/redis-server

* MongoDB as used by the `user-service` (through the dependency `com.ubich.user:core`)

* providers and contexts have to exist in Redis (see #Configuration section for details)

* all contexts in which you want to register users have to exist in the MongoDB. Here's two examples:

    {
        "_id" : ObjectId("590a25ce63a845f6501f4128"),
        "id" : UUID("4f72edd2-ec27-4df6-858e-3911d8f5207b"),
        "displayName" : "ubirch-dev",
        "created" : ISODate("2017-05-03T18:47:42.100Z"),
        "updated" : ISODate("2017-05-03T18:47:42.100Z")
    }
    
    {
        "_id" : ObjectId("590a25ce63a845f6501f412a"),
        "id" : UUID("91823652-e530-4908-a445-f2da660538f8"),
        "displayName" : "ubirch-demo",
        "created" : ISODate("2017-05-03T18:47:42.105Z"),
        "updated" : ISODate("2017-05-03T18:47:42.105Z")
    }


## Automated Tests

run all tests

    ./sbt test

### generate coverage report

    ./sbt clean coverage test coverageReport

more details here: https://github.com/scoverage/sbt-scoverage


## Create Docker Image

    ./sbt server/docker
