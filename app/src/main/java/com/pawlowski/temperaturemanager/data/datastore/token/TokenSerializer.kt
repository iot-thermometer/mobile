package com.pawlowski.temperaturemanager.data.datastore.token

import javax.inject.Inject

class TokenSerializer @Inject constructor() : KotlinxJsonSerializer<TokenDataStoreModel>(
    kSerializer = TokenDataStoreModel.serializer(),
) {

    override val defaultValue: TokenDataStoreModel = TokenDataStoreModel()
}
