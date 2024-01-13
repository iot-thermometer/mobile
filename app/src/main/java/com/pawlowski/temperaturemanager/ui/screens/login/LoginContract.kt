package com.pawlowski.temperaturemanager.ui.screens.login

import com.pawlowski.temperaturemanager.domain.Resource

data class LoginState(
    val email: String = "",
    val password: String = "",
    val requestResource: Resource<Unit>? = null,
    val showErrorsIfAny: Boolean = false,
)

sealed interface LoginEvent {
    object LoginClick : LoginEvent

    object RegisterClick : LoginEvent

    data class EmailChange(val newEmail: String) : LoginEvent

    data class PasswordChange(val newPassword: String) : LoginEvent
}
