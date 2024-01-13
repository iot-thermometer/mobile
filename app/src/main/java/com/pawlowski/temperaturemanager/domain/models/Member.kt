package com.pawlowski.temperaturemanager.domain.models

data class Member(
    val userId: Long,
    val email: String,
    val ownership: OwnershipType,
    val isMe: Boolean,
)
