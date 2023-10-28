package com.pawlowski.temperaturemanager.ui.screens.login

data class LoginState(
    val email: String = "",
    val password: String = "",
)

sealed interface LoginEvent {

    object LoginClick : LoginEvent

    object RegisterClick : LoginEvent

    data class EmailChange(val newEmail: String) : LoginEvent

    data class PasswordChange(val newPassword: String) : LoginEvent
}
