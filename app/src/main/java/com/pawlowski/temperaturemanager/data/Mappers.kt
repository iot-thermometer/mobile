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
import kotlin.math.absoluteValue

fun Alert.toDomain(): AlertDomain =
    AlertDomain(
        id = id,
        name = name,
        soilMoistureMin = soilMoistureMin.takeIf { it.toLong() != -1000L },
        soilMoistureMax = soilMoistureMax.takeIf { it.toLong() != 1000L },
        temperatureMin = temperatureMin.takeIf { it.toLong() != -1000L },
        temperatureMax = temperatureMax.takeIf { it.toLong().absoluteValue != 1000L },
    )

fun Device.toDomain(): DeviceDomain =
    DeviceDomain(
        id = id,
        name = name,
        recentlySeenAt = recentlySeenAt.takeIf { it != 0L }?.let { it * 1000L },
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
        temperature = value.takeIf { type == "TEMPERATURE" },
        soilMoisture = value.takeIf { type == "SOIL_MOISTURE" },
        measuredAt = measuredAt * 1000L,
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
