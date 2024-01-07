package com.pawlowski.temperaturemanager.ui.screens.alerts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.models.AlertDomain
import com.pawlowski.temperaturemanager.ui.components.Loader
import com.pawlowski.temperaturemanager.ui.components.Toolbar

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
    Column {
    }
}
