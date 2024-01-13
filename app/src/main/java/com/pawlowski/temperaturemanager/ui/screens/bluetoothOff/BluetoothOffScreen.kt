package com.pawlowski.temperaturemanager.ui.screens.bluetoothOff

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pawlowski.temperaturemanager.R
import com.pawlowski.temperaturemanager.ui.components.Toolbar

@Composable
fun BluetoothOffScreen(onBackClick: () -> Unit) {
    Column {
        Toolbar(
            leading =
                Toolbar.ToolbarLeading.Back(
                    onClick = {
                        onBackClick()
                    },
                ),
        )
        Column {
            val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bluetooth_animations))
            val progress by animateLottieCompositionAsState(
                lottieComposition,
                iterations = LottieConstants.IterateForever,
            )
            LottieAnimation(
                modifier =
                    Modifier
                        .padding(top = 70.dp)
                        .height(200.dp)
                        .width(300.dp)
                        .align(Alignment.CenterHorizontally),
                composition = lottieComposition,
                progress = { progress },
            )
            Text(
                text = "Bluetooth is turned off",
                modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp, horizontal = 40.dp),
                textAlign = TextAlign.Center,
                color = Color(0xFF757780),
            )
        }
    }
}
