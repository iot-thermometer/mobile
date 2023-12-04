package com.pawlowski.temperaturemanager.ui.screens.noBluetoothPermission

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.pawlowski.temperaturemanager.R
import com.pawlowski.temperaturemanager.ui.screens.wifiInfo.ToolBox
import com.pawlowski.temperaturemanager.ui.utils.rememberBluetoothMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoBluetoothPermissionScreen() {
    val bluetoothScanPermissionState = rememberBluetoothMultiplePermissionsState()
    ToolBox(onBackClick = {})
    Column {

        val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bluetooth_animations))
        val progress by animateLottieCompositionAsState(
            lottieComposition,
            iterations = LottieConstants.IterateForever,
        )
        LottieAnimation(
            modifier = Modifier
                .padding(start=55.dp, top = 80.dp)
                .height(200.dp).width(300.dp),
            composition = lottieComposition,
            progress = { progress },
        )
        val textToShow = if (bluetoothScanPermissionState.shouldShowRationale) {
            "The bluetooth is important for this app. Please grant the permission."
        } else {
            "Bluetooth permission required for this feature to be available. " +
                "Please grant the permission"
        }
        Text(textToShow, modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp, horizontal = 40.dp), textAlign = TextAlign.Center, color = Color(0xFF757780))
        Button(onClick = { bluetoothScanPermissionState.launchMultiplePermissionRequest() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 100.dp, vertical = 60.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001D4B))) {
            Text("Request permission")
        }
    }
}

@Composable
fun ToolBox(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 56.dp)
            .background(color = Color(0xFF001D4B)),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(11.dp)
                .clickable { onBackClick.invoke() },
        )
    }
}
