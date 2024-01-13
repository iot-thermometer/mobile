package com.pawlowski.temperaturemanager.domain.useCase.authentication

import com.pawlowski.network.ILoginRepository
import com.pawlowski.notificationservice.synchronization.INotificationTokenSynchronizer
import javax.inject.Inject

class LogOutUseCase
    @Inject
    constructor(
        private val loginRepository: ILoginRepository,
        private val notificationTokenSynchronizer: INotificationTokenSynchronizer,
    ) {
        suspend operator fun invoke() {
            loginRepository.logOut()
            notificationTokenSynchronizer.deleteCurrentToken()
        }
    }
