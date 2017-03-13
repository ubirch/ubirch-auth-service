package com.ubirch.auth.model.db

/**
  * author: cvandrei
  * since: 2017-03-13
  */
case class OidcProviderConfig(id: String,
                              name: String,
                              scope: String,
                              endpointConfig: String,
                              tokenSigningAlgorithms: Seq[String],
                              endpoints: OidcProviderEndpoints
                             )
