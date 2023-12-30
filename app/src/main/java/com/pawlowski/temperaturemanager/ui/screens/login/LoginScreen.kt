package com.pawlowski.temperaturemanager.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize(),
    ) {
        TextField(
            value = state.email,
            onValueChange = {
                onEvent(LoginEvent.EmailChange(it))
            },
            singleLine = true,
        )

        TextField(
            value = state.password,
            onValueChange = {
                onEvent(LoginEvent.PasswordChange(it))
            },
            singleLine = true,
        )

        Button(onClick = { onEvent(LoginEvent.LoginClick) }) {
            Text(text = "Login")
        }
        Button(onClick = { onEvent(LoginEvent.RegisterClick) }) {
            Text(text = "Register")
        }
    }
}
