package com.pawlowski.temperaturemanager.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.temperaturemanager.R
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.ui.components.Loader
import com.pawlowski.temperaturemanager.ui.components.PlusButton
import com.pawlowski.temperaturemanager.ui.components.Toolbar

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        Toolbar(
            trailing =
                Toolbar.ToolbarTrailing.Icon(
                    iconId = R.drawable.log_out,
                    onClick = {
                        onEvent(HomeEvent.LogOutClick)
                    },
                ),
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when (state.devicesOverviewResource) {
                is Resource.Success -> {
                    val devices = state.devicesOverviewResource.data

                    LazyColumn(
                        modifier = Modifier.align(Alignment.TopCenter).padding(horizontal = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(13.dp),
                    ) {
                        items(devices) {
                            DeviceParameters(
                                deviceName = it.device.name,
                                temperature = it.currentTemperature,
                                soilMosture = it.currentSoilMoisture,
                                modifier =
                                    Modifier.clickable {
                                        onEvent(HomeEvent.DeviceClick(it.device.id))
                                    },
                            )
                        }
                    }
                }

                is Resource.Loading -> {
                    Loader(modifier = Modifier.fillMaxSize())
                }

                is Resource.Error -> {
                    Text(text = "Error")
                }
            }

            PlusButton(
                onClick = {
                    onEvent(HomeEvent.AddNewDeviceClick)
                },
                modifier =
                    Modifier
                        .padding(all = 20.dp)
                        .align(Alignment.BottomEnd),
            )
        }
    }
}

@Composable
private fun DeviceParameters(
    deviceName: String,
    temperature: Int?,
    soilMosture: Int?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier.fillMaxWidth()
                .height(60.dp)
                .clip(shape = RoundedCornerShape(size = 5.dp))
                .background(color = Color(0xFFD9E2FF))
                .padding(all = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.device),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(size = 50.dp),
        )
        Column(
            modifier =
                Modifier.height(height = 50.dp).weight(1f)
                    .clip(shape = RoundedCornerShape(size = 5.dp))
                    .background(color = Color(0xFF3F4759)),
        ) {
            Text(
                deviceName,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier =
                    Modifier
                        .padding(horizontal = 10.dp, vertical = 13.dp)
                        .fillMaxWidth(),
                fontSize = 10.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        Column(
            modifier =
                Modifier.height(height = 50.dp).width(width = 60.dp)
                    .clip(shape = RoundedCornerShape(size = 5.dp))
                    .background(color = Color(0xFF355CA8)),
        ) {
            Text(
                temperature?.let {
                    "$it°C"
                } ?: "-",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier =
                    Modifier
                        .padding(horizontal = 5.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                fontSize = 15.sp,
            )
        }
        Column(
            modifier =
                Modifier.height(height = 50.dp).width(width = 60.dp)
                    .clip(shape = RoundedCornerShape(size = 5.dp))
                    .background(color = Color(color = 0xFF355CA8)),
        ) {
            Text(
                soilMosture?.let {
                    "$soilMosture%"
                } ?: "-",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier =
                    Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth().absolutePadding(top = 10.dp),
                fontSize = 15.sp,
            )
            Text(
                "[kg/kg]",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 6.sp,
                modifier =
                    Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth(),
            )
        }
    }
}
