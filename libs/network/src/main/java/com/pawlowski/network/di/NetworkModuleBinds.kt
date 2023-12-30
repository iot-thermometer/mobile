package com.pawlowski.network.di

import com.pawlowski.network.channel.GetGrpcChannelUseCase
import com.pawlowski.network.channel.IGetGrpcChannelUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModuleBinds {

    @Binds
    @Singleton
    abstract fun getGrpcChannelUseCase(getGrpcChannelUseCase: GetGrpcChannelUseCase): IGetGrpcChannelUseCase
}
