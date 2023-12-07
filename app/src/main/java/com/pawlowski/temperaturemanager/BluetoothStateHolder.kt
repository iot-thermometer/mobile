package com.pawlowski.temperaturemanager

import android.bluetooth.BluetoothAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BluetoothStateHolder @Inject constructor() {

    val isBluetoothEnabledFlow: StateFlow<Boolean>
        get() = _isBluetoothEnabledFlow.asStateFlow()

    fun onEnabledChange(newValue: Boolean) {
        _isBluetoothEnabledFlow.value = newValue
    }

    private val _isBluetoothEnabledFlow by lazy {
        MutableStateFlow(isBluetoothEnabled())
    }

    private fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter?.isEnabled == true
    }
}
