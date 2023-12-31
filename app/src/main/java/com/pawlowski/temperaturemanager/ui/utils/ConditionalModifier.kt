package com.pawlowski.temperaturemanager.ui.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@SuppressLint("UnnecessaryComposedModifier")
inline fun Modifier.conditional(
    condition: Boolean,
    crossinline ifTrue: @Composable Modifier.() -> Modifier,
): Modifier = composed {
    if (condition) {
        then(Modifier.Companion.ifTrue())
    } else {
        this
    }
}
