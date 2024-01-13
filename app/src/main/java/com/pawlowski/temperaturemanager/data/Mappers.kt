package com.pawlowski.temperaturemanager.data

import com.pawlowski.temperaturemanager.domain.models.AlertDomain
import com.pawlowski.temperaturemanager.domain.models.DeviceDomain
import com.pawlowski.temperaturemanager.domain.models.Member
import com.pawlowski.temperaturemanager.domain.models.OwnershipType
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import com.thermometer.proto.AlertOuterClass.Alert
import com.thermometer.proto.DeviceOuterClass.Device
import com.thermometer.proto.Ownership
import com.thermometer.proto.OwnershipRole
import com.thermometer.proto.ReadingOuterClass.Reading

fun Alert.toDomain(): AlertDomain =
    AlertDomain(
        id = id,
        name = name,
        soilMoistureMin = soilMoistureMin,
        soilMoistureMax = soilMoistureMax,
        temperatureMin = temperatureMin,
        temperatureMax = temperatureMax,
    )

fun Device.toDomain(): DeviceDomain =
    DeviceDomain(
        id = id,
        name = name,
        recentlySeenAt = recentlySeenAt.takeIf { it != 0L },
        pushInterval = pushInterval,
        readingInterval = readingInterval,
        token = token,
    )

fun List<Reading>.toDomain(): List<ReadingDomain> =
    map {
        it.toDomain()
    }

fun Reading.toDomain(): ReadingDomain =
    ReadingDomain(
        temperature = temperature,
        soilMoisture = soilMoisture,
        measuredAt = measuredAt,
    )

fun Ownership.toDomain(): Member =
    Member(
        email = email,
        ownership = role.toDomain(),
        userId = userID,
        isMe = isMe,
    )

private fun OwnershipRole.toDomain(): OwnershipType =
    when (this) {
        OwnershipRole.Owner -> OwnershipType.OWNER
        OwnershipRole.Member -> OwnershipType.VIEWER
        OwnershipRole.UNRECOGNIZED -> OwnershipType.VIEWER
    }
