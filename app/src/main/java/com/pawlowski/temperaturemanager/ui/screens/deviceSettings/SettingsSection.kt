package com.pawlowski.temperaturemanager.ui.screens.deviceSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.temperaturemanager.R

data class SettingsItem(
    val iconId: Int,
    val iconBackgroundColor: Color,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit,
)

@Composable
fun SettingsSection(
    items: List<SettingsItem>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .shadow(
                elevation = 10.dp,
                spotColor = Color(0x1F000000),
                ambientColor = Color(0x1F000000),
            ).background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 22.dp)),
    ) {
        items.forEachIndexed { index, settingsItem ->
            SettingsSectionItem(
                item = settingsItem,
                onClick = settingsItem.onClick,
            )
            if (index != items.size - 1) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun SettingsSectionItem(
    item: SettingsItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        modifier = modifier.clickable {
            onClick()
        }.padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingsItemIcon(
            icon = item.iconId,
            backgroundColor = item.iconBackgroundColor,
        )

        Column(
            modifier = Modifier.weight(
                weight = 1f,
                fill = true,
            ),
        ) {
            Text(
                text = item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF000000),
            )
            Text(
                text = item.subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight(300),
                color = Color(0xFF949494),
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.next),
            contentDescription = null,
            tint = Color(0xB2AEAEAE),
        )
    }
}

@Composable
private fun SettingsItemIcon(
    icon: Int,
    backgroundColor: Color,
) {
    Box(
        modifier = Modifier.background(
            color = backgroundColor,
            shape = RoundedCornerShape(size = 6.dp),
        )
            .size(size = 30.dp)
            .padding(all = 6.dp),
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun HorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 1.dp)
            .background(color = Color(0x80C4C4C4)),
    ) {}
}
