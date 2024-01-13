package com.pawlowski.temperaturemanager.ui.screens.wifiInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.pawlowski.temperaturemanager.ui.components.Toolbar

@Composable
fun WifiInfoScreen(
    state: WifiInfoState,
    onEvent: (WifiInfoEvent) -> Unit,
) {
    when (state) {
        is WifiInfoState.Initialising -> {
        }

        is WifiInfoState.Content -> {
            Column(
                modifier =
                    Modifier
                        .imePadding(),
            ) {
                Toolbar(
                    leading =
                        Toolbar.ToolbarLeading.Back(
                            onClick = {
                                onEvent(WifiInfoEvent.BackClick)
                            },
                        ),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(space = 32.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                        .padding(top = 32.dp),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .padding(horizontal = 10.dp)
                                .clip(shape = RoundedCornerShape(size = 12.dp))
                                .background(color = Color(0xFF70778B))
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp, vertical = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.device),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(50.dp),
                            )
                            Text(
                                text = state.chosenAdvertisement.name,
                                color = Color.White,
                                fontSize = 30.sp,
                                modifier = Modifier.padding(vertical = 10.dp),
                            )
                        }
                        Text(
                            text = "MAC: " + state.chosenAdvertisement.macAddress,
                            color = Color.White,
                            fontSize = 20.sp,
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        SingleInputColumn(
                            ssidInput = state.ssidInput,
                            header = "Wpisz SSID: ",
                            label = "SSID",
                            onValueChange = { onEvent(WifiInfoEvent.ChangeSsid(it)) },
                        )
                        if (state.ssidError != null) {
                            Text(
                                text = state.ssidError,
                                color = Color.Red,
                                modifier = Modifier.absolutePadding(left = 45.dp),
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        SingleInputColumn(
                            state.passwordInput,
                            "Wpisz Hasło: ",
                            "Hasło",
                            onValueChange = { onEvent(WifiInfoEvent.ChangePassword(it)) },
                        )
                        if (state.passwordError != null) {
                            Text(
                                state.passwordError,
                                color = Color.Red,
                                modifier = Modifier.absolutePadding(left = 45.dp),
                            )
                        }
                    }
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.absolutePadding(left = 200.dp))
                    }
                    Button(
                        onClick = { onEvent(WifiInfoEvent.ContinueClick) },
                        enabled = !state.isLoading,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 90.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001D4B)),
                    ) {
                        Text("Continue", modifier = Modifier.padding(all = 10.dp), fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleInputColumn(
    ssidInput: String,
    header: String,
    label: String,
    onValueChange: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Text(
            text = header,
            modifier =
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 23.sp,
            textAlign = TextAlign.Center,
        )
        TextField(
            text = ssidInput,
            label = label,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
            //                            .background(color = Color(0xFFE3E2E6)).padding(vertical = 5.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFE9E6EB)),
            onValueChange = onValueChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextField(
    text: String,
    label: String,
    modifier: Modifier,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    onValueChange: (String) -> Unit,
) {
    var showPassword by remember { mutableStateOf(value = false) }

    OutlinedTextField(
        // // isError, supportingText?
        value = text,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        colors = colors,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation =
            if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPassword = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "hide_password",
                    )
                }
            } else {
                IconButton(
                    onClick = { showPassword = true },
                ) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = "hide_password",
                    )
                }
            }
        },
    )
}
