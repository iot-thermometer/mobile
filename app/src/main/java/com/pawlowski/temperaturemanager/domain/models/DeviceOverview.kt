package com.pawlowski.temperaturemanager.domain.models

data class DeviceOverview(
    val name: String,
    val currentTemperature: Int,
    val currentSoilMoisture: Int,
)
