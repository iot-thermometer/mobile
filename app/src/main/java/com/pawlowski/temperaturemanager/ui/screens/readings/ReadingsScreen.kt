package com.pawlowski.temperaturemanager.ui.screens.readings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import com.pawlowski.temperaturemanager.ui.components.Toolbar
import com.pawlowski.temperaturemanager.ui.utils.formatHHmm
import java.util.Date

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
            Row(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        spotColor = Color(0x40000000),
                        ambientColor = Color(0x40000000),
                    )
                    .fillMaxWidth()
                    .background(color = Color(0xFFD9E2FF), shape = RoundedCornerShape(size = 4.dp))
                    .padding(horizontal = 5.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ReadingsCard(text = Date(it.measuredAt).formatHHmm(), color = Color(0xFF3F4759))
                Icon(
                    imageVector = Icons.Filled.Thermostat,
                    contentDescription = null,
                    modifier = Modifier.size(size = 20.dp),
                )
                ReadingsCard(text = "${it.temperature}°C", color = Color(0xFFDE3730))

                Icon(
                    imageVector = Icons.Filled.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier.size(size = 20.dp),
                )

                ReadingsCard(
                    text = "${it.soilMoisture.toInt()}%",
                    color = Color(0xFF355CA8),
                    subtitle = "[kg/kg]",
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun ReadingsCard(
    text: String,
    subtitle: String? = null,
    color: Color,
) {
    Column(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(size = 4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            color = Color(0xFFFFFFFF),
            fontSize = 22.sp,
            fontWeight = FontWeight(400),
            textAlign = TextAlign.Center,
        )
        subtitle?.let {
            Text(
                text = subtitle,
                color = Color(0xFFFFFFFF),
                fontSize = 13.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
            )
        }
    }
}
