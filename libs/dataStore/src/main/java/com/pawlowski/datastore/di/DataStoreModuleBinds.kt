package com.pawlowski.datastore.di

import com.pawlowski.datastore.IAuthTokenRepository
import com.pawlowski.datastore.IPushTokenRepository
import com.pawlowski.datastore.authToken.AuthTokenRepository
import com.pawlowski.datastore.pushToken.PushTokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

private const val TOKEN_DATA_STORE_FILE_NAME = "tokenDataStore"

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataStoreModuleBinds {

    @Binds
    abstract fun tokenRepository(authTokenRepository: AuthTokenRepository): IAuthTokenRepository

    @Binds
    abstract fun pushTokenRepository(pushTokenRepository: PushTokenRepository): IPushTokenRepository
}
