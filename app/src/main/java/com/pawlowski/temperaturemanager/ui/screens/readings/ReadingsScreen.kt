package com.pawlowski.temperaturemanager.ui.screens.readings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import com.pawlowski.temperaturemanager.ui.components.Toolbar

@Composable
fun ReadingsScreen(
    state: ReadingsState,
    onEvent: (ReadingsEvent) -> Unit,
) {
    Column {
        Toolbar(
            leading = Toolbar.ToolbarLeading.Back(
                onClick = {
                    onEvent(ReadingsEvent.BackClick)
                },
            ),
        )

        when (state) {
            is ReadingsState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is ReadingsState.Content -> {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(space = 24.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
                        ) {
                            ReadingCircle(
                                mainText = "${state.lastTemperature}°C",
                                subText = null,
                                label = "Temperature",
                                contentColor = Color(0xFFDE3730),
                                modifier = Modifier.weight(weight = 1f),
                            )

                            ReadingCircle(
                                mainText = "${state.lastSoilMoisture}%",
                                subText = "[kg/kg]",
                                label = "Soil Moisture",
                                contentColor = Color(0xFF355CA8),
                                modifier = Modifier.weight(weight = 1f),
                            )
                        }

                        HorizontalDivider()
                    }

                    LazyColumn {
                        items(state.readings.toList()) { (day, readings) ->
                            ReadingsSection(
                                day = day,
                                readings = readings,
                            )
                        }
                    }
                }
            }

            ReadingsState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "You have got no readings!")
                }
            }

            ReadingsState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Something went wrong!")
                }
            }
        }
    }
}

@Composable
private fun HorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(color = Color(0xFF001944))
            .shadow(
                elevation = 4.dp,
                spotColor = Color(0x40000000),
                ambientColor = Color(0x40000000),
            ),
    )
}

@Composable
private fun ReadingCircle(
    mainText: String,
    subText: String?,
    label: String,
    contentColor: Color,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().aspectRatio(ratio = 1f).border(
                width = 5.dp,
                shape = CircleShape,
                color = contentColor,
            ),
            contentAlignment = Alignment.Center,
        ) {
            Column(modifier = Modifier.padding(all = 10.dp)) {
                Text(
                    text = mainText,
                    fontSize = 50.sp,
                    fontWeight = FontWeight(weight = 400),
                    color = contentColor,
                )
                subText?.let {
                    Text(
                        text = subText,
                        fontSize = 30.sp,
                        fontWeight = FontWeight(weight = 400),
                        color = contentColor,
                    )
                }
            }
        }
        Text(
            text = label,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight(300),
            color = Color(0xFF000000),
            fontSize = 13.sp,
        )
    }
}

@Composable
private fun ReadingsSection(
    day: String,
    readings: List<ReadingDomain>,
) {
    Column {
        Text(text = day)
        readings.forEach {
            ReadingsItem(reading = it)
        }
    }
}

@Composable
private fun ReadingsItem(
    reading: ReadingDomain,
) {
    Column(modifier = Modifier.background(color = Color.Blue)) {
        Text(text = reading.temperature.toString())
        Text(text = reading.soilMoisture.toString())
    }
}
