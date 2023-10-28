package com.pawlowski.temperaturemanager

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.juul.kable.AndroidAdvertisement
import com.pawlowski.temperaturemanager.data.BLEManager
import com.pawlowski.temperaturemanager.ui.screens.login.LoginScreen
import com.pawlowski.temperaturemanager.ui.screens.login.LoginViewModel
import com.pawlowski.temperaturemanager.ui.theme.TemperatureManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var bleManager: BLEManager

    private val viewModel by viewModels<MainViewModel>()

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    // FeatureThatRequiresBluetoothPermission()
                    LoginScreen(
                        state = loginViewModel.state.collectAsState().value,
                        onEvent = loginViewModel::onEvent,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun FeatureThatRequiresBluetoothPermission() {
        // Camera permission state
        val bluetoothScanPermissionState = rememberPermissionState(
            android.Manifest.permission.BLUETOOTH_SCAN,
        )

        if (bluetoothScanPermissionState.status.isGranted) {
            Column {
                Text("Bluetooth permission Granted")
                ScanningDevices()
            }
        } else {
            Column {
                val textToShow = if (bluetoothScanPermissionState.status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "The bluetooth scan is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Bluetooth scan permission required for this feature to be available. " +
                        "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { bluetoothScanPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun ScanningDevices() {
        Column {
            val bluetoothConnectPermissionState = rememberPermissionState(
                android.Manifest.permission.BLUETOOTH_CONNECT,
            )
            val state = viewModel.state.collectAsState()
            Text(text = "List of devices:")

            LazyColumn(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
            ) {
                items(state.value) {
                    AdvertisementItem(advertisement = it) {
                        if (!bluetoothConnectPermissionState.status.isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            bluetoothConnectPermissionState.launchPermissionRequest()
                        } else {
                            viewModel.sentDataToDevice(it)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertisementItem(
    advertisement: AndroidAdvertisement,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(all = 10.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(text = advertisement.name ?: "Unknown")
            Text(text = advertisement.address)
        }
    }
}
