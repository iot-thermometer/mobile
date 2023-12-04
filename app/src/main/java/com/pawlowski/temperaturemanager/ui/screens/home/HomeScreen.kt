package com.pawlowski.temperaturemanager.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.temperaturemanager.R
import com.pawlowski.temperaturemanager.domain.Resource

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    ToolBox(onBackClick = {})
    Column(modifier = Modifier.fillMaxSize()) {
        when (state.devicesOverviewResource) {
            is Resource.Success -> {
                val devices = state.devicesOverviewResource.data

                LazyColumn(modifier = Modifier.weight(weight = 1f).padding(vertical = 80.dp, horizontal = 15.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
                    items(devices) {
                        DeviceParameters(it.device.name, 32.0, 20.1) // // potem zmienic na parametr z czujnika
//                        Text(text = "DeviceName ${it.device.name}")
                    }
                }
            }

            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Error -> {
                Text(text = "Error")
            }
        }
//        Canvas(modifier = Modifier.size(100.dp), onDraw = {
//            drawCircle(color = Color.Red)
//        })

        Button(
            shape = CircleShape,
            onClick = {
                onEvent(HomeEvent.AddNewDeviceClick)
            },
            modifier = Modifier.absolutePadding(left = 320.dp, bottom = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001D4B))
        ) {
            Text(text = "+", fontWeight = FontWeight.SemiBold, fontSize = 40.sp)
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
        horizontalArrangement = Arrangement.spacedBy(220.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.home),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .clickable { onBackClick.invoke() }.size(100.dp).fillMaxWidth(),
        )
        Icon(
            painter = painterResource(id = R.drawable.person),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .clickable { onBackClick.invoke() }.size(100.dp),
        )
    }
}

@Composable
fun DeviceParameters(DeviceName: String, Temperature: Double, Density: Double) {
    Row(modifier = Modifier.fillMaxWidth().height(60.dp).clip(shape = RoundedCornerShape(size = 5.dp)).background(color = Color(0xFFD9E2FF)).padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(painter = painterResource(id = R.drawable.device), contentDescription = null, tint = Color.Black, modifier = Modifier.size(50.dp))
        Column(modifier = Modifier.height(50.dp).width(150.dp).clip(shape = RoundedCornerShape(size = 5.dp)).background(color = Color(0xFF3F4759))) {
            Text(
                DeviceName,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                fontSize = 15.sp,
            )
        }
        Column(modifier = Modifier.height(50.dp).width(60.dp).clip(shape = RoundedCornerShape(size = 5.dp)).background(color = Color(0xFF355CA8))) {
            var Temp = Temperature.toString()
            Text(
                Temp + "°C",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                fontSize = 15.sp,
            )
        }
        Column(modifier = Modifier.height(50.dp).width(60.dp).clip(shape = RoundedCornerShape(size = 5.dp)).background(color = Color(0xFF355CA8))) {
            var Dens = Density.toString()
            Text(
                Dens + "%",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth().absolutePadding(top = 10.dp),
                fontSize = 15.sp,
            )
            Text(
                "[kg/kg]",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 6.sp,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth(),
            )
        }
    }
}
