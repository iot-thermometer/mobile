package com.pawlowski.temperaturemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juul.kable.AndroidAdvertisement
import com.pawlowski.temperaturemanager.data.BLEManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val bleManager: BLEManager,
): ViewModel() {

    private val _state = MutableStateFlow<List<AndroidAdvertisement>>(listOf())
    val state: StateFlow<List<AndroidAdvertisement>>
        get() = _state

    val discovered = MutableStateFlow("")

    val errors = MutableStateFlow("")


    fun sentDataToDevice(advertisement: AndroidAdvertisement) {
        viewModelScope.launch {
            val services = try {
                bleManager.connectToDevice(
                    advertisement = advertisement,
                    coroutineScope = viewModelScope,
                )
            } catch (e: Exception) {
                e.printStackTrace()
                ensureActive()
                errors.value = e.message ?: "Some error"
                ""
            }
            discovered.value = services
        }
    }

    init {
        viewModelScope.launch {
            bleManager.getScannedDevices()
                .collect {
                    _state.value = it
                }
        }
    }

}