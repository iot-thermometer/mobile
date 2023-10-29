package com.pawlowski.temperaturemanager.ui.screens.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pawlowski.temperaturemanager.ui.components.Loader

@Composable
fun SplashScreen() {
    Loader(modifier = Modifier.fillMaxSize())
}
