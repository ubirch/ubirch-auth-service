package com.ubirch.auth.oidcutil

import java.io.IOException
import java.net.{URI, URL}

import com.nimbusds.jose.jwk.source.RemoteJWKSet
import com.nimbusds.jose.proc.{BadJOSEException, JWSVerificationKeySelector, SimpleSecurityContext}
import com.nimbusds.jose.{JOSEException, JWSAlgorithm}
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import com.nimbusds.jwt.{JWT, JWTClaimsSet}
import com.nimbusds.oauth2.sdk.auth.{ClientSecretPost, Secret}
import com.nimbusds.oauth2.sdk.http.HTTPResponse
import com.nimbusds.oauth2.sdk.id.ClientID
import com.nimbusds.oauth2.sdk.{AuthorizationCode, AuthorizationCodeGrant, ParseException, SerializeException, TokenErrorResponse, TokenRequest}
import com.nimbusds.openid.connect.sdk.{OIDCTokenResponse, OIDCTokenResponseParser}
import com.typesafe.scalalogging.slf4j.StrictLogging

import com.ubirch.auth.model.db.{ContextProviderConfig, OidcProviderConfig}

/**
  * author: cvandrei
  * since: 2017-02-14
  */
object TokenUtil extends StrictLogging {

  def requestToken(contextProvider: ContextProviderConfig,
                   providerConf: OidcProviderConfig,
                   authCode: String
                  ): Option[TokenUserId] = {

    sendTokenRequest(contextProvider = contextProvider, providerConf = providerConf, authCode = authCode) match {

      case None => None

      case Some(tokenHTTPResp) =>

        try {

          OIDCTokenResponseParser.parse(tokenHTTPResp) match {

            case error: TokenErrorResponse =>
              logger.error(s"oidc code verification failed (provider replied with an error): ${tokenHTTPResp.getStatusCode} - ${tokenHTTPResp.getContent} - ${error.toJSONObject.toJSONString}")
              None

            case accessTokenResponse: OIDCTokenResponse =>

              val accessToken = accessTokenResponse.getOIDCTokens.getAccessToken
              val idToken = accessTokenResponse.getOIDCTokens.getIDToken
              val context = contextProvider.context
              val provider = contextProvider.provider

              verifyIdToken(providerConf = providerConf, idToken = idToken) match {

                case None =>
                  logger.error(s"failed to get verified token: context=$context, provider=$provider")
                  logger.debug(s"accessToken=$accessToken, userId=$None, idToken=${idToken.getParsedString}")
                  None

                case Some(claims) =>
                  val userId = claims.getSubject
                  logger.debug(s"got verified token: context=$context, provider=$provider, userId=$userId, accessToken=$accessToken, userId=$userId, idToken=${idToken.getParsedString}")
                  logger.info(s"got verified token from provider=$provider (context=$context)")
                  Some(TokenUserId(accessToken.getValue, userId))

              }

          }

        } catch {
          case e: ParseException =>
            logger.error(s"oidc code verification failed (failed to parse response from provider)", e)
            None
        }

    }

  }

  private def sendTokenRequest(contextProvider: ContextProviderConfig,
                               providerConf: OidcProviderConfig,
                               authCode: String
                              ): Option[HTTPResponse] = {

    try {

      val tokenReq = tokenRequest(contextProviderConf = contextProvider, providerConf = providerConf, authCode = authCode)
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

  private def tokenRequest(contextProviderConf: ContextProviderConfig,
                           providerConf: OidcProviderConfig,
                           authCode: String
                          ): TokenRequest = {

    val redirectUri = contextProviderConf.callbackUrl
    val grant = new AuthorizationCodeGrant(new AuthorizationCode(authCode), redirectUri)

    val tokenEndpoint = new URI(providerConf.endpoints.token)
    logger.debug(s"token endpoint: provider=${providerConf.id}, url=$tokenEndpoint")

    val clientId = new ClientID(contextProviderConf.clientId)
    val secret = new Secret(contextProviderConf.clientSecret)
    val auth = new ClientSecretPost(clientId, secret)

    new TokenRequest(tokenEndpoint, auth, grant)

  }

  private def verifyIdToken(providerConf: OidcProviderConfig, idToken: JWT): Option[JWTClaimsSet] = {

    jwtProcessor(providerConf, idToken) match {

      case None =>
        logger.error(s"failed to load jwtProcessor: provider=${providerConf.id}")
        None

      case Some(jwtProcessor) =>

        try {

          val ctx: SimpleSecurityContext = new SimpleSecurityContext()
          Some(jwtProcessor.process(idToken, ctx))

        } catch {
          case e: BadJOSEException =>
            logger.error("verifyIdToken() failed with a BadJOSEException", e)
            None
          case e: JOSEException =>
            logger.error("verifyIdToken() failed with a JOSEException", e)
            None
        }

    }

  }

  private def jwtProcessor(providerConf: OidcProviderConfig, idToken: JWT): Option[DefaultJWTProcessor[SimpleSecurityContext]] = {

    val jwtProcessor = new DefaultJWTProcessor[SimpleSecurityContext]()
    val keySource = new RemoteJWKSet[SimpleSecurityContext](new URL(providerConf.endpoints.jwks))
    val algorithm = idToken.getHeader.getAlgorithm

    if (providerConf.tokenSigningAlgorithms contains algorithm.getName) {

      try {

        val requirement = algorithm.getRequirement
        val jwsAlg: JWSAlgorithm = new JWSAlgorithm(algorithm.getName, requirement)
        val keySelector: JWSVerificationKeySelector[SimpleSecurityContext] =
          new JWSVerificationKeySelector[SimpleSecurityContext](jwsAlg, keySource)
        jwtProcessor.setJWSKeySelector(keySelector)

        Some(jwtProcessor)

      } catch {
        case iae: IllegalArgumentException =>
          logger.error("jwtProcessor() failed with an IllegalArgumentException", iae)
          None
      }

    } else {

      logger.error(s"signing algorithm does not match those allowed by our configuration: provider=${providerConf.id}, algorithm=$algorithm")
      None

    }

  }

}

case class TokenUserId(token: String, userId: String)
