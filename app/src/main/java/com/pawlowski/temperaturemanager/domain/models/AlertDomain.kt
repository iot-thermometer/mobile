package com.pawlowski.temperaturemanager.domain.models

data class AlertDomain(
    val id: Long,
    val name: String,
    val soilMoistureMin: Float,
    val soilMoistureMax: Float,
    val temperatureMin: Float,
    val temperatureMax: Float,
)
