package com.pawlowski.datastore.authToken

import com.pawlowski.datastore.KotlinxJsonSerializer
import javax.inject.Inject

internal class AuthTokenSerializer @Inject constructor() :
    KotlinxJsonSerializer<AuthTokenDataStoreModel>(
        kSerializer = AuthTokenDataStoreModel.serializer(),
    ) {

    override val defaultValue: AuthTokenDataStoreModel = AuthTokenDataStoreModel()
}
