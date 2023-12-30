package com.pawlowski.temperaturemanager.domain.models

data class AlertDomain(
    val id: String,
    val name: String,
    val soilMoistureMin: Float,
    val soilMoistureMax: Float,
    val temperatureMin: Float,
    val temperatureMax: Float,
)
