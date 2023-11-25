package com.pawlowski.temperaturemanager.domain

sealed class BluetoothException : Exception() {

    object Timeout : BluetoothException()

    object CharacteristicsNotFound : BluetoothException()

    object MissingAdvertisement : BluetoothException()

    data class Other(val status: String) : BluetoothException()
}
