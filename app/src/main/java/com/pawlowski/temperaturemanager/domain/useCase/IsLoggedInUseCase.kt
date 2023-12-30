package com.pawlowski.temperaturemanager.domain.useCase

import com.pawlowski.network.datastore.TokenRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {

    suspend operator fun invoke(): Boolean = tokenRepository.getToken() != null
}
