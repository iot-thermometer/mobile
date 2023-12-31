package com.pawlowski.temperaturemanager.ui.screens.deviceSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.temperaturemanager.R
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.DeviceDomain
import com.pawlowski.temperaturemanager.ui.components.Loader
import com.pawlowski.temperaturemanager.ui.components.Toolbar
import com.pawlowski.temperaturemanager.ui.utils.formatDDMMYYYYHHmm
import java.util.Date

@Composable
fun DeviceSettingsScreen(
    state: DeviceSettingsState,
    onEvent: (DeviceSettingsEvent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color(0xFF9ECBFF)),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        ) {
            Toolbar(
                transparentBackground = true,
                leading = Toolbar.ToolbarLeading.Back(
                    iconColor = Color.Black,
                    onClick = {
                        onEvent(DeviceSettingsEvent.BackClick)
                    },
                ),
                toolbarText = "Device settings",
            )

            when (state.deviceResource) {
                is Resource.Success -> {
                    Content(device = state.deviceResource.data)
                }

                is Resource.Loading -> {
                    Loader(modifier = Modifier.fillMaxSize())
                }

                is Resource.Error -> {
                    Text(text = "Something went wrong")
                }
            }
        }
    }
}

@Composable
private fun Content(device: DeviceDomain) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
    ) {
        DeviceInfo(
            name = device.name,
            lastSeen = device.recentlySeenAt,
        )
        SettingsCard()
    }
}

@Composable
private fun DeviceInfo(
    name: String,
    lastSeen: Long,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(size = 62.dp)
                .background(
                    color = Color(0xFF355CA8),
                    shape = CircleShape,
                ).padding(all = 10.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.device),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(space = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = name,
                fontSize = 15.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFF000000),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Ostatnio widziane: ${Date(lastSeen).formatDDMMYYYYHHmm()}",
                fontSize = 10.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF5C5C5C),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun SettingsCard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF8F7F7),
                shape = RoundedCornerShape(
                    topStart = 40.dp,
                    topEnd = 40.dp,
                ),
            ).padding(top = 20.dp),
    ) {
        SettingsSection(
            items = listOf(
                SettingsItem(
                    iconId = R.drawable.signal_icon,
                    iconBackgroundColor = Color(0xFFF18A5C),
                    title = "Częstotliwość odczytów",
                    subtitle = "Ustaw częstotliwość odczytów",
                    onClick = {
                        // TODO
                    },
                ),
                SettingsItem(
                    iconId = R.drawable.device,
                    iconBackgroundColor = Color(0xFF5CF198),
                    title = "Nazwa urządzenia",
                    subtitle = "Zmień nazwę urządzenia",
                    onClick = {
                        // TODO
                    },
                ),
                SettingsItem(
                    iconId = R.drawable.notification,
                    iconBackgroundColor = Color(0xFFFF00C4),
                    title = "Alerty",
                    subtitle = "Dostawaj alerty o pomiarach",
                    onClick = {
                        // TODO
                    },
                ),
            ),
        )

        SettingsSection(
            items = listOf(
                SettingsItem(
                    iconId = R.drawable.delete,
                    iconBackgroundColor = Color(0xFFF15C5C),
                    title = "Usuń urządzenie",
                    subtitle = "Usuń urządzenie wraz z pomiarami",
                    onClick = {
                        // TODO
                    },
                ),
            ),
        )
    }
}
