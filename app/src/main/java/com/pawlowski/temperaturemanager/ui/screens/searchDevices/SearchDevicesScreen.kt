package com.pawlowski.temperaturemanager.ui.screens.searchDevices

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.temperaturemanager.R

@Composable
fun SearchDevicesScreen(
    state: SearchDevicesState,
    onEvent: (SearchDevicesEvent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        ToolBox(onBackClick = {})
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            Text(
                text = "Wybierz dostępne\nurządzenie",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 27.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 44.sp,
                    shadow = Shadow(
                        color = Color.Gray,
                        offset = Offset(2.0f, 5.0f),
                        blurRadius = 2f,
                    ),
                ),
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                items(state.devices) { device ->
                    val name = device.name
                    val address = device.macAddress
                    val shape = RoundedCornerShape(size = 12.dp)

                    Column(
                        modifier = Modifier
                            .clip(shape = shape)
                            .background(color = Color(0xFF70778B))
                            .clickable { onEvent(SearchDevicesEvent.DeviceClick(device)) }
                            .padding(horizontal = 5.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(painter = painterResource(id = R.drawable.device), contentDescription = null, tint = Color.White)
                            Text(
                                text = name,
                                color = Color.White,

                            )
                        }
                        Text(text = "MAC: " + address, color = Color.White, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ToolBox(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 56.dp)
            .background(color = Color(0xFF001D4B)),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(11.dp).clickable { onBackClick.invoke() },
        )
    }
}
