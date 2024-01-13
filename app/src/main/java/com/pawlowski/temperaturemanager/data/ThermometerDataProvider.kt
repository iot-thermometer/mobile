package com.pawlowski.temperaturemanager.data

import com.pawlowski.datastore.IAuthTokenRepository
import com.pawlowski.network.BaseAuthorizedDataProvider
import com.pawlowski.network.IThermometerServiceProvider
import com.pawlowski.temperaturemanager.domain.models.AlertDomain
import com.pawlowski.temperaturemanager.domain.models.DeviceDomain
import com.pawlowski.temperaturemanager.domain.models.Member
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import com.thermometer.proto.AddMemberRequest
import com.thermometer.proto.CreateAlertRequest
import com.thermometer.proto.CreateDeviceRequest
import com.thermometer.proto.DeleteDeviceRequest
import com.thermometer.proto.ListAlertsRequest
import com.thermometer.proto.ListDevicesRequest
import com.thermometer.proto.ListMembersRequest
import com.thermometer.proto.ListReadingsRequest
import com.thermometer.proto.RemoveMemberRequest
import com.thermometer.proto.ThermometerServiceGrpcKt
import com.thermometer.proto.UpdateDeviceRequest
import javax.inject.Inject

class ThermometerDataProvider
    @Inject
    constructor(
        thermometerServiceProvider: IThermometerServiceProvider,
        tokenRepository: IAuthTokenRepository,
    ) : BaseAuthorizedDataProvider(
            thermometerServiceProvider = thermometerServiceProvider,
            authTokenRepository = tokenRepository,
        ) {
        suspend fun listDevices(): List<DeviceDomain> =
            authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::listDevices,
                request =
                    ListDevicesRequest.newBuilder()
                        .build(),
            ).devicesList.map {
                it.toDomain()
            }

        suspend fun listReadings(deviceId: Long): List<ReadingDomain> {
            return authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::listReadings,
                request =
                    ListReadingsRequest.newBuilder()
                        .setId(deviceId)
                        .build(),
            ).readingsList.toDomain()
        }

        suspend fun createDevice(
            name: String,
            readingInterval: Int,
            pushInterval: Int,
        ): DeviceDomain =
            authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::createDevice,
                request =
                    CreateDeviceRequest.newBuilder()
                        .setName(name)
                        .setPushInterval(pushInterval)
                        .setReadingInterval(readingInterval)
                        .build(),
            ).device.toDomain()

        suspend fun listAlerts(deviceId: Long): List<AlertDomain> =
            authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::listAlerts,
                request = ListAlertsRequest.newBuilder().build(),
            ).alertsList.filter {
                it.deviceID == deviceId
            }.map {
                it.toDomain()
            }

        suspend fun createAlerts(
            deviceId: Long,
            name: String,
            minTemp: Float?,
            maxTemp: Float?,
            minSoil: Float?,
            maxSoil: Float?,
        ) = authorizedUnary(
            method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::createAlert,
            request =
                CreateAlertRequest.newBuilder().apply {
                    setName(name)
                    deviceID = deviceId
                    temperatureMin = minTemp ?: -1000f
                    temperatureMax = maxTemp ?: 1000f
                    soilMoistureMin = minSoil ?: -1000f
                    soilMoistureMax = maxSoil ?: 1000f
                }.build(),
        )

        suspend fun deleteDevice(deviceId: Long) {
            authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::deleteDevice,
                request =
                    DeleteDeviceRequest.newBuilder()
                        .setId(deviceId)
                        .build(),
            )
        }

        suspend fun updateDevice(
            deviceId: Long,
            readingInterval: Long,
            pushInterval: Long,
            name: String,
        ) {
            authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::updateDevice,
                request =
                    UpdateDeviceRequest.newBuilder()
                        .setId(deviceId)
                        .setPushInterval(pushInterval)
                        .setReadingInterval(readingInterval)
                        .setName(name)
                        .build(),
            )
        }

        suspend fun getDeviceMembers(deviceId: Long): List<Member> {
            return authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::listMembers,
                request =
                    ListMembersRequest.newBuilder()
                        .setDeviceID(deviceId)
                        .build(),
            ).ownershipsList.also(::println).map { it.toDomain() }
        }

        suspend fun addDeviceMember(
            deviceId: Long,
            email: String,
        ) {
            authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::addMember,
                request =
                    AddMemberRequest.newBuilder()
                        .setDeviceID(deviceId)
                        .setEmail(email)
                        .build(),
            )
        }

        suspend fun deleteDeviceMember(
            deviceId: Long,
            userId: Long,
        ) {
            authorizedUnary(
                method = ThermometerServiceGrpcKt.ThermometerServiceCoroutineStub::removeMember,
                request =
                    RemoveMemberRequest.newBuilder()
                        .setDeviceID(deviceId)
                        .setUserID(userId)
                        .build(),
            )
        }
    }
