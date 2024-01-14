package com.pawlowski.temperaturemanager.domain.models

data class ReadingDomain(
    val temperature: Float?,
    val soilMoisture: Float?,
    val measuredAt: Long,
)
