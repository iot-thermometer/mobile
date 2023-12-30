package com.pawlowski.datastore.di

import com.pawlowski.datastore.ITokenRepository
import com.pawlowski.datastore.authToken.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

private const val TOKEN_DATA_STORE_FILE_NAME = "tokenDataStore"

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataStoreModuleBinds {
    @Binds
    abstract fun tokenRepository(tokenRepository: TokenRepository): ITokenRepository
}
