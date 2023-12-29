package com.pawlowski.notificationservice.dataStore

internal interface IDeviceIdAndTokenDataStore {
    fun updateDeviceIdAndToken(calculateNewValue: (DeviceIdAndToken) -> DeviceIdAndToken)
    fun getDeviceIdAndToken(): DeviceIdAndToken
}
