package com.pawlowski.datastore.authToken

import kotlinx.serialization.Serializable

@Serializable
internal data class TokenDataStoreModel(
    val token: String? = null,
)
