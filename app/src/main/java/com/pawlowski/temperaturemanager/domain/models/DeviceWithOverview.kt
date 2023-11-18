package com.pawlowski.temperaturemanager.domain.models

data class DeviceWithOverview(
    val device: DeviceDomain,
    val currentTemperature: Int,
    val currentSoilMoisture: Int,
)
