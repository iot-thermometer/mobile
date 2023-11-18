package com.pawlowski.temperaturemanager.data

import com.pawlowski.temperaturemanager.domain.models.DeviceOverview
import com.thermometer.proto.DeviceOuterClass.Device

private fun Device.toDomain(): DeviceOverview {
    return this.name
}