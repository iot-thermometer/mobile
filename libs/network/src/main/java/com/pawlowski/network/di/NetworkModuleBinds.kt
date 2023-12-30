package com.pawlowski.network.di

import com.pawlowski.network.ILoginRepository
import com.pawlowski.network.IThermometerServiceProvider
import com.pawlowski.network.channel.GetGrpcChannelUseCase
import com.pawlowski.network.channel.IGetGrpcChannelUseCase
import com.pawlowski.network.login.LoginRepository
import com.pawlowski.network.service.ThermometerServiceProvider
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

    @Binds
    abstract fun loginRepository(loginRepository: LoginRepository): ILoginRepository

    @Singleton
    @Binds
    abstract fun thermometerServiceProvider(thermometerServiceProvider: ThermometerServiceProvider): IThermometerServiceProvider
}
