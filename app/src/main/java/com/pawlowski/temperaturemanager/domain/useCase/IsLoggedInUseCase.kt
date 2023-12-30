package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.datastore.ITokenRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val tokenRepository: ITokenRepository,
) {

    suspend operator fun invoke(): Boolean = tokenRepository.getToken() != null
}
