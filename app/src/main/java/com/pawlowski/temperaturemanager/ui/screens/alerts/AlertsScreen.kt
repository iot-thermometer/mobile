package com.pawlowski.temperaturemanager.ui.screens.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.AlertDomain
import com.pawlowski.temperaturemanager.ui.components.Loader
import com.pawlowski.temperaturemanager.ui.components.PlusButton
import com.pawlowski.temperaturemanager.ui.components.Toolbar
import com.pawlowski.temperaturemanager.ui.screens.bottomSheets.AddAlertBottomSheet

@Composable
internal fun AlertsScreen(
    state: AlertsState,
    onEvent: (AlertsEvent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Toolbar(
            leading = Toolbar.ToolbarLeading.Back(
                onClick = {
                    onEvent(AlertsEvent.OnBackClick)
                },
            ),
        )

        Text(
            text = "Powiadomienia dla urządzenia:",
            style = MaterialTheme.typography.titleLarge,
        )
        when (state.alertsResource) {
            is Resource.Success -> {
                Content(
                    alerts = state.alertsResource.data,
                    onEvent = onEvent,
                )
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

@Composable
private fun Content(
    alerts: List<AlertDomain>,
    onEvent: (AlertsEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(alerts) {
                AlertCard(alert = it)
            }
        }
        val showAlertBottomSheet = remember {
            mutableStateOf(false)
        }
        AddAlertBottomSheet(
            show = showAlertBottomSheet.value,
            onDismiss = { showAlertBottomSheet.value = false },
            onConfirm = { minTemp, maxTemp, minSoil, maxSoil ->
                showAlertBottomSheet.value = false
                onEvent(
                    AlertsEvent.OnAddAlert(
                        minTemp = minTemp,
                        maxTemp = maxTemp,
                        minSoil = minSoil,
                        maxSoil = maxSoil,
                    ),
                )
            },
        )

        PlusButton(
            onClick = {
                showAlertBottomSheet.value = true
            },
            modifier = Modifier
                .padding(all = 20.dp)
                .align(Alignment.BottomEnd),
        )
    }
}

@Composable
private fun AlertCard(alert: AlertDomain) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFD9E2FF),
                shape = RoundedCornerShape(size = 12.dp),
            ).padding(all = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = null,
        )
        Column {
            Text(
                text = alert.name,
                style = MaterialTheme.typography.titleMedium,
            )
            alert.temperatureMin?.let {
                Text(
                    text = "Min Temp: ${alert.temperatureMin}°C",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            alert.temperatureMax?.let {
                Text(
                    text = "Max temp: ${alert.temperatureMax}°C",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            alert.temperatureMin?.let {
                Text(
                    text = "Min temp: ${alert.temperatureMin}°C",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            alert.soilMoistureMin?.let {
                Text(
                    text = "Min wilgotność gleby: ${alert.soilMoistureMin}%",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            alert.soilMoistureMax?.let {
                Text(
                    text = "Max wilgotność gleby: ${alert.soilMoistureMax}%",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
