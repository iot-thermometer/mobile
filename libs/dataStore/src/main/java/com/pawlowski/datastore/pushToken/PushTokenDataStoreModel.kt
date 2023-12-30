package com.pawlowski.datastore.pushToken

import kotlinx.serialization.Serializable

@Serializable
internal data class PushTokenDataStoreModel(
    val token: String? = null,
    val deviceId: String? = null,
)
