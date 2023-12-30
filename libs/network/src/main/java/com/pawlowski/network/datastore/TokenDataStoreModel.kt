package com.pawlowski.network.datastore

import kotlinx.serialization.Serializable

@Serializable
data class TokenDataStoreModel(
    val token: String? = null,
)
