package com.pawlowski.network

interface ILoginRepository {
    suspend fun login(
        email: String,
        password: String,
    )

    suspend fun register(
        email: String,
        password: String,
    )
}
