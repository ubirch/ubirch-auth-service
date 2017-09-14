## REST Methods

### Welcome / Health / Check

    curl localhost:8091/
    curl localhost:8091/api/authService/v1
    curl localhost:8091/api/authService/v1/check

If healthy the server response is:

    200 {"version":"1.0","status":"OK","message":"Welcome to the ubirchAuthService ( $GO_PIPELINE_NAME / $GO_PIPELINE_LABEL / $GO_PIPELINE_REVISION )"}

If not healthy the server response is:

    400 {"version":"1.0","status":"NOK","message":"$ERROR_MESSAGE"}

### Deep Check / Server Health

    curl localhost:8091/api/authService/v1/deepCheck

If healthy the response is:

    200 {"version":"1.0","status":"OK","messages":[]}

If not healthy the status is `false` and the `messages` array not empty:

    503 {"version":"1.0","status":"NOK","messages":["unable to connect to the database"]}


### Provider Infos

Gives us a list of providers configured for a specific context and app id (e.g. `trackle-dev/ui`, `ubirch-dev/admin-ui`,
...).

    curl localhost:8091/api/authService/v1/providerInfo/list/$CONTEXT/$APP_ID


### Verify Code

After a successful login users are being redirect to the Frontend. That call includes a _context_, _appId_,
_providerId_, _code_ and _state_. Calling this method has the effect that our system verifies the code and responds with
the resulting token. This token can then be used to request protected resources in other backend services.

    curl -XPOST localhost:8091/api/authService/v1/verify/code -H "Content-Type: application/json" -d'{
      "context": "$CONTEXT",
      "appId": "$APP_ID",
      "providerId": "$PROVIDER_ID",
      "code": "$CODE",
      "state": "$STATE"
    }'

If the code could be verified the response will be:

    200 {"token": "a_token_1234"}

In case of an error the response will be:

    400 {"version":"1.0","status":"NOK","errorType":"VerificationError","errorMessage":"invalid code"}


### Logout

To invalidate a token (logout) please call:

    curl localhost:8091/api/authService/v1/logout/$TOKEN

If the logout is successful (or the token already expired) the response is:

    200 OK

In case of an error the response is:

    400 {"version":"1.0","status":"NOK","errorType":"LogoutError","errorMessage":"logout failed"}


### Registration

The auth-service relies on external OpenID Connect providers for authentication.
To be able to remember the connection between devices and users users need to be known in our system, too. This happens
in two steps:
1) login with external OpenID Connect provider (authentication)
2) register in our system

Users are independent of a context and it can happen that they have been registered in another context already. All user
specific information (user's and group' displayName) is dynamically determined based on information received from the
OpenID Connect provider).

To register a new user:

    curl -XPOST localhost:8091/api/authService/v1/register -H "Authorization: Bearer $TOKEN"

If the registration is successful the response is:

    200
    {
      "displayName": "some user name",
      "myGroups": [
        {
          "id": "a4c08d88-7c43-4984-a568-0672b4431016", // UUID
          "displayName": "some user name" // same as displayName above
        }
      ],
      "allowedGroups": [] // being a new user it is impossible to have been added to another group
    }

If the registration fails the response is:

    400
    {
      "apiVersion": "1.0.0",
      "status": "NOK",
      "error": {
        "errorId": "RegistrationError", // errorId can be different
        "errorMessage": "failed to register new user" // errorMessage can be different
      }
    }


### User Info

#### Get

    curl localhost:8091/api/authService/v1/userInfo -H "Authorization: Bearer $TOKEN"

If the query is successful the response is (user exists but is not registered not in this context):

    200
    {
      "displayName": "some string being displayed in frontend as my display name",
      "locale": "en",
      "myGroups": [],
      "allowedGroups": []
    }

If the query is successful the response is (user is registered in this context and has been added to other group
including the admin group of this context):

    200
    {
      "displayName": "some string being displayed in frontend as my display name",
      "myGroups": [
        {
          "id": "a4c08d88-7c43-4984-a568-0672b4431016", // UUID
          "displayName": "my-ubirch-group"
        }
      ],
      "allowedGroups": [
        {
          "id": "f2d4280d-336f-438d-9b2a-70337723a3e7", // UUID
          "displayName": "my-best-friends-ubirch-group"
        },
        {
          "id": "32c5c928-97a0-49b7-9d71-0b0517b7d13e", // UUID
          "displayName": "admin group",
          "adminGroup": true // means user has admin rights for this context
        }
      ]
    }

If no user is found the response is:

    400
    {
      "apiVersion": "1.0.0",
      "status": "NOK",
      "error": {
        "errorId": "NoUserInfoFound"
        "errorMessage": "failed to get user info"
      }
    }

If the query fails the response is:

    500
    {
      "apiVersion": "1.0.0",
      "status": "NOK",
      "error": {
        "errorId": "ServerError",
        "errorMessage": "failed to get user info"
      }
    }

#### Update

    curl -XPUT localhost:8091/api/authService/v1/userInfo -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{
      "displayName": "my new display name",
      "locale": "en"
    }'

If the query is successful the response is:

    200
    {
      "displayName": "my new display name",
      "locale": "en",
      "myGroups": [
        {
          "id": "a4c08d88-7c43-4984-a568-0672b4431016", // UUID
          "displayName": "my-ubirch-group"
        }
      ],
      "allowedGroups": [
        {
          "id": "f2d4280d-336f-438d-9b2a-70337723a3e7", // UUID
          "displayName": "my-best-friends-ubirch-group"
        },
        {
          "id": "32c5c928-97a0-49b7-9d71-0b0517b7d13e", // UUID
          "displayName": "another-friends-ubirch-group"
        }
      ]
    }

If the query fails the response is:

    400
    {
      "apiVersion": "1.0.0",
      "status": "NOK",
      "error": {
        "errorId": "UpdateError", // errorId can be different
        "errorMessage": "failed to update user" // errorMessage can be different
      }
    }

### Group Info

#### Update

TODO: group management will follow at a later time