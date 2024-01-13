package com.pawlowski.temperaturemanager.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.ui.components.Loader
import com.pawlowski.temperaturemanager.ui.components.PasswordTextField

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
            label = {
                Text(text = "E-mail")
            },
            isError = state.email.isBlank() && state.showErrorsIfAny,
        )

        PasswordTextField(
            text = state.password,
            onValueChange = {
                onEvent(LoginEvent.PasswordChange(it))
            },
            label = "Hasło",
            showErrorsIfAny = state.showErrorsIfAny,
        )

        Button(onClick = { onEvent(LoginEvent.LoginClick) }) {
            Text(text = "Login")
        }
        Button(onClick = { onEvent(LoginEvent.RegisterClick) }) {
            Text(text = "Register")
        }

        when (state.requestResource) {
            null -> {
            }

            Resource.Loading -> {
                Loader(modifier = Modifier.fillMaxSize())
            }

            is Resource.Error -> {
                Text(text = "Something went wrong", color = Color.Red)
            }

            else -> {}
        }
    }
}
