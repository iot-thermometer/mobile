package com.pawlowski.datastore.authToken

import kotlinx.serialization.Serializable

@Serializable
internal data class AuthTokenDataStoreModel(
    val token: String? = null,
)
