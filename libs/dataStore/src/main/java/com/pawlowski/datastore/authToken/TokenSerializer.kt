package com.pawlowski.datastore.authToken

import com.pawlowski.datastore.KotlinxJsonSerializer
import javax.inject.Inject

internal class TokenSerializer @Inject constructor() : KotlinxJsonSerializer<TokenDataStoreModel>(
    kSerializer = TokenDataStoreModel.serializer(),
) {

    override val defaultValue: TokenDataStoreModel = TokenDataStoreModel()
}
