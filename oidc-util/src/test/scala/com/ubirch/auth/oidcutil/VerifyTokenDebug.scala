package com.ubirch.auth.oidcutil

import java.net.URL

import com.nimbusds.jose._
import com.nimbusds.jose.jwk.source._
import com.nimbusds.jose.proc.{JWSVerificationKeySelector, SimpleSecurityContext}
import com.nimbusds.jwt._
import com.nimbusds.jwt.proc._

/**
  * based on: https://connect2id.com/products/nimbus-jose-jwt/examples/validating-jwt-access-tokens
  *
  * author: cvandrei
  * since: 2017-02-16
  */
object VerifyTokenDebug {

  def main(args: Array[String]): Unit = {

    val accessToken: JWT = SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6IjgxM2QzNjVmZWJjZDhkZjE2YjZlZTlhOGNhZThjNTQ1NmUxYzNjYjYifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJpYXQiOjE0ODcyNDg3NTUsImV4cCI6MTQ4NzI1MjM1NSwiYXRfaGFzaCI6ImJKZHQ0ZHlJcWhCTmlqZ3FMSUFQVUEiLCJhdWQiOiIzNzAxMTUzMzIwOTEta3FmNWh1Njk4czRzb2RydnYwM2thM2J1bGU1MzBycDUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDk1Njc0MDU0MDYyOTM1MzU2MjgiLCJhenAiOiIzNzAxMTUzMzIwOTEta3FmNWh1Njk4czRzb2RydnYwM2thM2J1bGU1MzBycDUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20ifQ.PwuGrTNJy6JKK09eOXUT9GZDTWwfg4ZM0rYUoQRDevnHHn1AbKPq5DN0EWwP_WzvJUaoyZdOjysWZPjABF97yX4ZxEBGn6XY90tzMMzYwZAmlYgJ3l5Goe46dQs5ABbObdYaVgro94qAzqkvWdJ9GZ0xvk5RsRV8joeP9vLuPS8YPa33_dLk9P7_xQT7N-_WKH3s844zCYaZkge6Rgog35pTmpzdA1JrPhiTs_jmPL_BgtJQMxonNhTJr6tEnywhafmjaCPXct0fW2UOo4l3vs-8iAEWRGwrGJOp-EUzFo9LfWwP17ONIn-WSX-bZTbxjBS0ouC-l_EUxp5a93BVwA")

    // Set up a JWT processor to parse the tokens and then check their signature
    // and validity time window (bounded by the "iat", "nbf" and "exp" claims)
    val jwtProcessor = new DefaultJWTProcessor[SimpleSecurityContext]()

    // The public RSA keys to validate the signatures will be sourced from the
    // OAuth 2.0 server's JWK set, published at a well-known URL. The RemoteJWKSet
    // object caches the retrieved keys to speed up subsequent look-ups and can
    // also gracefully handle key-rollover
    val keySource = new RemoteJWKSet[SimpleSecurityContext](new URL("https://www.googleapis.com/oauth2/v3/certs"))

    // The expected JWS algorithm of the access tokens (agreed out-of-band)
    val algName = accessToken.getHeader.getAlgorithm.getName
    val expectedJWSAlg: JWSAlgorithm = new JWSAlgorithm(algName, Requirement.RECOMMENDED)

    // Configure the JWT processor with a key selector to feed matching public
    // RSA keys sourced from the JWK set URL
    val keySelector: JWSVerificationKeySelector[SimpleSecurityContext] =
    new JWSVerificationKeySelector[SimpleSecurityContext](expectedJWSAlg, keySource)
    jwtProcessor.setJWSKeySelector(keySelector)

    // Process the token
    val ctx: SimpleSecurityContext = new SimpleSecurityContext()
    // optional context parameter, not required here
    val claimsSet: JWTClaimsSet = jwtProcessor.process(accessToken, ctx)

    // Print out the token claims set
    println(claimsSet.toJSONObject)

  }

}
