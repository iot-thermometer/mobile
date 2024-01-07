package com.pawlowski.temperaturemanager.domain.models

data class DeviceDomain(
    val id: Long,
    val name: String,
    val recentlySeenAt: Long?,
    val readingInterval: Long,
    val pushInterval: Long,
    val token: String,
)
