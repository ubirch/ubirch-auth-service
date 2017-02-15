package com.ubirch.auth.oidcutil

import java.io.IOException
import java.net.URI

import com.nimbusds.oauth2.sdk.auth.{ClientSecretPost, Secret}
import com.nimbusds.oauth2.sdk.http.HTTPResponse
import com.nimbusds.oauth2.sdk.id.ClientID
import com.nimbusds.oauth2.sdk.{AuthorizationCode, AuthorizationCodeGrant, ParseException, SerializeException, TokenErrorResponse, TokenRequest}
import com.nimbusds.openid.connect.sdk.{OIDCTokenResponse, OIDCTokenResponseParser}
import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.config.Config

import scala.util.Random

/**
  * author: cvandrei
  * since: 2017-02-14
  */
object TokenUtil extends StrictLogging {

  def requestToken(provider: String, authCode: String): Option[TokenUserId] = {

    val redirectUri = new URI(Config.oidcProviderCallbackUrl(provider))
    val grant = new AuthorizationCodeGrant(new AuthorizationCode(authCode), redirectUri)
    val tokenReq: TokenRequest = tokenRequest(provider, grant)

    sendTokenRequest(tokenReq) match {

      case None => None

      case Some(tokenHTTPResp) =>

        try {

          OIDCTokenResponseParser.parse(tokenHTTPResp) match {

            case error: TokenErrorResponse =>
              logger.error(s"oidc code verification failed (provider replied with an error): ${tokenHTTPResp.getStatusCode} - ${error.toJSONObject.toJSONString}")
              None

            case accessTokenResponse: OIDCTokenResponse =>

              val accessToken = accessTokenResponse.getOIDCTokens.getAccessToken
              val idToken = accessTokenResponse.getOIDCTokens.getIDToken
              val userId = s"$provider-$authCode-${Random.nextInt}" // TODO extract from idToken

              // TODO validate idToken (should be signed)

              Some(TokenUserId(accessToken.getValue, userId))

          }

        } catch {
          case e: ParseException =>
            logger.error(s"oidc code verification failed (failed to parse response from provider)", e)
            None
        }

    }

  }

  private def tokenRequest(provider: String, grant: AuthorizationCodeGrant): TokenRequest = {

    val tokenEndpoint = new URI(Config.oidcProviderEndpointConfig(provider))

    val clientId = new ClientID(Config.oidcProviderClientId(provider))
    val secret = new Secret(Config.oidcProviderClientSecret(provider))
    val auth = new ClientSecretPost(clientId, secret)

    new TokenRequest(tokenEndpoint, auth, grant)

  }

  private def sendTokenRequest(tokenReq: TokenRequest): Option[HTTPResponse] = {

    try {

      Some(tokenReq.toHTTPRequest.send())

    } catch {

      case se: SerializeException =>
        logger.error(s"failed to send oidc code verification request (SerializeException)", se)
        None

      case e: IOException =>
        logger.error(s"failed to send oidc code verification request (SerializeException)", e)
        None
    }

  }

}

case class TokenUserId(token: String, userId: String)
