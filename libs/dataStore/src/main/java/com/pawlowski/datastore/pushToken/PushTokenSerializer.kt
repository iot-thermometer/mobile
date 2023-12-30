package com.pawlowski.datastore.pushToken

import com.pawlowski.datastore.KotlinxJsonSerializer
import javax.inject.Inject

internal class PushTokenSerializer @Inject constructor() :
    KotlinxJsonSerializer<PushTokenDataStoreModel>(
        kSerializer = PushTokenDataStoreModel.serializer(),
    ) {

    override val defaultValue: PushTokenDataStoreModel = PushTokenDataStoreModel()
}
