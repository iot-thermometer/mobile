package com.pawlowski.datastore.pushToken

import androidx.datastore.core.DataStore
import com.pawlowski.datastore.IPushTokenRepository
import com.pawlowski.datastore.PushToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class PushTokenRepository @Inject constructor(
    private val dataStore: DataStore<PushTokenDataStoreModel>,
) : IPushTokenRepository {

    override suspend fun updatePushToken(calculateNewValue: (PushToken) -> PushToken) {
        dataStore.updateData {
            calculateNewValue(it.toDomain()).toDataStore()
        }
    }

    override suspend fun getPushToken(): PushToken = dataStore.data
        .first()
        .let {
            PushToken(
                token = it.token,
                deviceId = it.token,
            )
        }

    private fun PushTokenDataStoreModel.toDomain(): PushToken =
        PushToken(
            token = token,
            deviceId = deviceId,
        )

    private fun PushToken.toDataStore(): PushTokenDataStoreModel =
        PushTokenDataStoreModel(
            token = token,
            deviceId = deviceId,
        )
}
