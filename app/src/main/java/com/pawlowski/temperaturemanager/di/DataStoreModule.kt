package com.pawlowski.temperaturemanager.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.pawlowski.network.datastore.TokenDataStoreModel
import com.pawlowski.network.datastore.TokenSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.osipxd.security.crypto.createEncrypted
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
    ): DataStore<TokenDataStoreModel> = DataStoreFactory.createEncrypted(
        serializer = tokenSerializer,
        produceFile = {
            EncryptedFile.Builder(
                application.dataStoreFile(TOKEN_DATA_STORE_FILE_NAME),
                application.applicationContext,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB,
            ).build()
        },
    )
}
