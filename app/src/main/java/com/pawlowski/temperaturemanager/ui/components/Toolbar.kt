package com.pawlowski.temperaturemanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pawlowski.temperaturemanager.R

interface Toolbar {
    sealed interface ToolbarLeading {

        data class Back(val onClick: () -> Unit) : ToolbarLeading
    }

    sealed interface ToolbarTrailing {

        data class Icon(
            val iconId: Int,
            val onClick: () -> Unit,
        ) : ToolbarTrailing
    }
}

@Composable
fun Toolbar(
    leading: Toolbar.ToolbarLeading? = null,
    trailing: Toolbar.ToolbarTrailing? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 56.dp)
            .background(color = Color(0xFF001D4B)),
    ) {
        if (leading != null) {
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                when (leading) {
                    is Toolbar.ToolbarLeading.Back -> {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(size = 56.dp)
                                .padding(all = 11.dp)
                                .clickable { leading.onClick() },
                        )
                    }
                }
            }
        }
        if (trailing != null) {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                when (trailing) {
                    is Toolbar.ToolbarTrailing.Icon -> {
                        Icon(
                            painter = painterResource(id = trailing.iconId),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(size = 56.dp)
                                .clickable { trailing.onClick() },
                        )
                    }
                }
            }
        }
    }
}
