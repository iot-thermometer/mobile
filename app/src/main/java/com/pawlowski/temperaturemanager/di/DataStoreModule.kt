package com.pawlowski.temperaturemanager.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.pawlowski.temperaturemanager.data.datastore.token.TokenDataStoreModel
import com.pawlowski.temperaturemanager.data.datastore.token.TokenSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val TOKEN_DATA_STORE_FILE_NAME = "tokenDataStore"

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun tokenDataStore(
        tokenSerializer: TokenSerializer,
        application: Application,
    ): DataStore<TokenDataStoreModel> = DataStoreFactory.create(
        serializer = tokenSerializer,
        produceFile = { application.dataStoreFile(TOKEN_DATA_STORE_FILE_NAME) },
    )
}
