package com.pawlowski.network.datastore

import javax.inject.Inject

class TokenSerializer @Inject constructor() : KotlinxJsonSerializer<TokenDataStoreModel>(
    kSerializer = TokenDataStoreModel.serializer(),
) {

    override val defaultValue: TokenDataStoreModel = TokenDataStoreModel()
}
