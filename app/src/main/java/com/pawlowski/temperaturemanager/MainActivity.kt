package com.pawlowski.temperaturemanager

import android.bluetooth.BluetoothAdapter
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.pawlowski.temperaturemanager.ui.navigation.RootComposable
import com.pawlowski.temperaturemanager.ui.theme.TemperatureManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var bluetoothStateHolder: BluetoothStateHolder

    private val bluetoothStateReceiver by lazy {
        BluetoothStateBroadcastReceiver { isEnabled ->
            bluetoothStateHolder.onEnabledChange(newValue = isEnabled)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    RootComposable(isBluetoothEnabled = bluetoothStateHolder.isBluetoothEnabledFlow.collectAsState().value)
                }
            }
        }

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothStateReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothStateReceiver)
    }
}
