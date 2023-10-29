package com.pawlowski.temperaturemanager.data.datastore.token

import kotlinx.serialization.Serializable

@Serializable
data class TokenDataStoreModel(
    val token: String? = null,
)
