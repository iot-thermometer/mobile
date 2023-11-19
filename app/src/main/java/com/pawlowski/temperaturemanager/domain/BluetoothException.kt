package com.pawlowski.temperaturemanager.domain

sealed class BluetoothException : Exception() {

    object Timeout : BluetoothException()

    object CharacteristicsNotFound : BluetoothException()

    data class Other(val status: String) : BluetoothException()
}
