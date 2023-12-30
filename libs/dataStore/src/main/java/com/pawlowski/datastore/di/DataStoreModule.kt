package com.pawlowski.datastore.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.pawlowski.datastore.authToken.AuthTokenDataStoreModel
import com.pawlowski.datastore.authToken.AuthTokenSerializer
import com.pawlowski.datastore.pushToken.PushTokenDataStoreModel
import com.pawlowski.datastore.pushToken.PushTokenSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.osipxd.security.crypto.createEncrypted
import javax.inject.Singleton

private const val AUTH_TOKEN_DATA_STORE_FILE_NAME = "authTokenDataStore"
private const val PUSH_TOKEN_DATA_STORE_FILE_NAME = "pushTokenDataStore"

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    @Singleton
    fun authTokenDataStore(
        authTokenSerializer: AuthTokenSerializer,
        application: Application,
    ): DataStore<AuthTokenDataStoreModel> =
        DataStoreFactory.createEncrypted(
            serializer = authTokenSerializer,
            produceFile = {
                EncryptedFile.Builder(
                    application.dataStoreFile(AUTH_TOKEN_DATA_STORE_FILE_NAME),
                    application.applicationContext,
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB,
                ).build()
            },
        )

    @Provides
    @Singleton
    fun pushTokenDataStore(
        tokenSerializer: PushTokenSerializer,
        application: Application,
    ): DataStore<PushTokenDataStoreModel> =
        DataStoreFactory.createEncrypted(
            serializer = tokenSerializer,
            produceFile = {
                EncryptedFile.Builder(
                    application.dataStoreFile(PUSH_TOKEN_DATA_STORE_FILE_NAME),
                    application.applicationContext,
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB,
                ).build()
            },
        )
}
