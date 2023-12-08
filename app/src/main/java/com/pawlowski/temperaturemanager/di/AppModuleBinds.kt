package com.pawlowski.temperaturemanager.di

import com.pawlowski.temperaturemanager.data.ble.BLEManager
import com.pawlowski.temperaturemanager.data.ble.IBLEManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppModuleBinds {

    @Binds
    @Singleton
    abstract fun bleManager(bleManager: BLEManager): IBLEManager
}
