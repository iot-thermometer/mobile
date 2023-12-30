package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.datastore.IAuthTokenRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val authTokenRepository: IAuthTokenRepository,
) {

    suspend operator fun invoke(): Boolean = authTokenRepository.getToken() != null
}
