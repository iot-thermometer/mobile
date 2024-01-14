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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.temperaturemanager.R
import com.pawlowski.temperaturemanager.domain.models.ReadingDomain
import com.pawlowski.temperaturemanager.ui.components.ErrorItem
import com.pawlowski.temperaturemanager.ui.components.Toolbar
import com.pawlowski.temperaturemanager.ui.screens.bottomSheets.share.ShareBottomSheet
import com.pawlowski.temperaturemanager.ui.screens.readings.ReadingsState.ContentState
import com.pawlowski.temperaturemanager.ui.utils.formatHHmm
import java.util.Date

@Composable
fun ReadingsScreen(
    state: ReadingsState,
    onEvent: (ReadingsEvent) -> Unit,
) {
    Column {
        val showShareBottomSheet =
            remember {
                mutableStateOf(false)
            }
        ShareBottomSheet(
            show = showShareBottomSheet.value,
            onDismiss = { showShareBottomSheet.value = false },
        )

        Toolbar(
            leading =
                Toolbar.ToolbarLeading.Back(
                    onClick = {
                        onEvent(ReadingsEvent.BackClick)
                    },
                ),
            trailing =
                if (state.amIOwner) {
                    Toolbar.ToolbarTrailing.DoubleIcon(
                        iconId1 = R.drawable.add_people,
                        onClick1 = {
                            showShareBottomSheet.value = true
                        },
                        iconId2 = R.drawable.settings,
                        onClick2 = {
                            onEvent(ReadingsEvent.SettingsClick)
                        },
                    )
                } else {
                    Toolbar.ToolbarTrailing.Icon(
                        iconId = R.drawable.add_people,
                        onClick = {
                            showShareBottomSheet.value = true
                        },
                    )
                },
        )

        when (state.contentState) {
            is ContentState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is ContentState.Content -> {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(space = 24.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
                        ) {
                            ReadingCircle(
                                mainText =
                                    state.contentState.lastTemperature?.let { lastTemperature ->
                                        "$lastTemperature°C"
                                    } ?: "-",
                                subText = null,
                                label = "Temperature",
                                contentColor = Color(0xFFDE3730),
                                modifier = Modifier.weight(weight = 1f),
                            )

                            ReadingCircle(
                                mainText =
                                    state.contentState.lastSoilMoisture?.let { lastSoilMoisture ->
                                        "$lastSoilMoisture%"
                                    } ?: "-",
                                subText = "[kg/kg]",
                                label = "Soil Moisture",
                                contentColor = Color(0xFF355CA8),
                                modifier = Modifier.weight(weight = 1f),
                            )
                        }

                        HorizontalDivider()
                    }

                    LazyColumn {
                        items(state.contentState.readings.toList()) { (day, readings) ->
                            ReadingsSection(
                                day = day,
                                readings = readings,
                            )
                        }
                    }
                }
            }

            ContentState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "You have got no readings!")
                }
            }

            ContentState.Error -> {
                ErrorItem(
                    onRetry = {
                        onEvent(ReadingsEvent.RetryClick)
                    },
                )
            }
        }
    }
}

@Composable
internal fun HorizontalDivider() {
    Box(
        modifier =
            Modifier
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
            modifier =
                Modifier.fillMaxWidth().aspectRatio(ratio = 1f).border(
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
                modifier =
                    Modifier
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
                ReadingsCard(
                    text =
                        it.temperature?.let { temperature ->
                            "$temperature°C"
                        } ?: "-",
                    color = Color(0xFFDE3730),
                )

                Icon(
                    imageVector = Icons.Filled.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier.size(size = 20.dp),
                )

                ReadingsCard(
                    text =
                        it.soilMoisture?.let { soilMoisture ->
                            "${soilMoisture.toInt()}%"
                        } ?: "-",
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
        modifier =
            Modifier
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
