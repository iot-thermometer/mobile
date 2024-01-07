package com.pawlowski.temperaturemanager.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun PlusButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        shape = CircleShape,
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001D4B)),
    ) {
        Text(
            text = "+",
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
        )
    }
}
