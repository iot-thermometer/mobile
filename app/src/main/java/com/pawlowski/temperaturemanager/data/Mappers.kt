package com.pawlowski.temperaturemanager.data

import com.pawlowski.temperaturemanager.domain.models.DeviceDomain
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import com.thermometer.proto.DeviceOuterClass.Device
import com.thermometer.proto.ReadingOuterClass.Reading

/*fun List<Device>.toDomain(): List<DeviceDomain> = map {
    it.toDomain()
}*/

fun Device.toDomain(): DeviceDomain = DeviceDomain(
    id = id,
    name = name,
    recentlySeenAt = recentlySeenAt,
    pushInterval = pushInterval,
    readingInterval = readingInterval,
    token = token,
)

fun List<Reading>.toDomain(): List<ReadingDomain> = map {
    it.toDomain()
}

fun Reading.toDomain(): ReadingDomain = ReadingDomain(
    temperature = temperature,
    soilMoisture = soilMoisture,
    measuredAt = measuredAt,
)
