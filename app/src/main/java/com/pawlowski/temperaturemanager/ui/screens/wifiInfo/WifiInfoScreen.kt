package com.pawlowski.temperaturemanager.ui.screens.wifiInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.temperaturemanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiInfoScreen(
    state: WifiInfoState,
    onEvent: (WifiInfoEvent) -> Unit,
) {
//    Text(text = "Wifi info")

    when (state) {
        is WifiInfoState.Initialising -> {
        }
        is WifiInfoState.Content -> {
            Column(verticalArrangement = Arrangement.spacedBy(37.dp)) {
                ToolBox(onBackClick = {})

                Column(
                    modifier = Modifier.padding(horizontal = 10.dp)
                        .clip(shape = RoundedCornerShape(size = 12.dp))
                        .background(color = Color(0xFF70778B))
                        .fillMaxWidth().padding(horizontal = 5.dp, vertical = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(painter = painterResource(id = R.drawable.device), contentDescription = null, tint = Color.White, modifier = Modifier.size(50.dp))
                        Text(text = state.chosenAdvertisement.name, color = Color.White, fontSize = 30.sp, modifier = Modifier.padding(vertical = 10.dp))
                    }
                    Text(text = "MAC: " + state.chosenAdvertisement.macAddress, color = Color.White, fontSize = 20.sp)
                }

                SingleInputColumn(state.ssidInput, "Wpisz SSID: ", "SSID", onValueChange = { onEvent(WifiInfoEvent.ChangeSsid(it)) })
                if (state.ssidError != null) {
                    Text(state.ssidError, color = Color.Red)
                }
                SingleInputColumn(state.passwordInput, "Wpisz Hasło: ", "Hasło", onValueChange = { onEvent(WifiInfoEvent.ChangePassword(it)) })
                if (state.passwordError != null) {
                    Text(state.passwordError, color = Color.Red)
                }
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
                Button(
                    onClick = { onEvent(WifiInfoEvent.ContinueClick) },
                    enabled = !state.isLoading,
                    modifier = Modifier.absolutePadding(left = 220.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001D4B)), // // center
                ) {
                    Text("Continue", modifier = Modifier.padding(all = 10.dp), fontSize = 20.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleInputColumn(ssidInput: String, header: String, label: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Text(
            text = header,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 23.sp,
            textAlign = TextAlign.Center,
        )
        TextField(
            text = ssidInput,
            label = label,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp),
//                            .background(color = Color(0xFFE3E2E6)).padding(vertical = 5.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFE9E6EB)),
            onValueChange = onValueChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    text: String,
    label: String,
    modifier: Modifier,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField( // // isError, supportingText?
        value = text,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        colors = colors,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
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
            modifier = Modifier
                .padding(11.dp)
                .clickable { onBackClick.invoke() },
        )
    }
}
