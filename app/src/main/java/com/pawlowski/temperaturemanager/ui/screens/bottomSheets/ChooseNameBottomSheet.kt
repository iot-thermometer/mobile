package com.pawlowski.temperaturemanager.ui.screens.bottomSheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ChooseNameBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    initialName: String,
    onConfirm: (String) -> Unit,
) {
    BaseBottomSheet(show = show, onDismiss = onDismiss) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier.fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 15.dp)
                    .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "Edytuj nazwę urządzenia",
                style = MaterialTheme.typography.titleMedium,
            )
            val showErrorIfAny =
                remember {
                    mutableStateOf(false)
                }
            val nameState =
                remember(initialName) {
                    mutableStateOf(initialName)
                }
            TextField(
                value = nameState.value,
                label = {
                    Text(text = "Nazwa urządzenia")
                },
                onValueChange = {
                    nameState.value = it
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                isError = nameState.value.isBlank() && showErrorIfAny.value,
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    if (nameState.value.isNotBlank()) {
                        hideBottomSheetWithAction {
                            onConfirm(nameState.value)
                        }
                    } else {
                        showErrorIfAny.value = true
                    }
                },
                shape = RectangleShape,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF355CA8),
                        contentColor = Color.White,
                    ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Edytuj")
            }

            Button(
                onClick = {
                    dismissBottomSheet()
                },
                shape = RectangleShape,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF15C5C),
                        contentColor = Color.White,
                    ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Anuluj")
            }
        }
    }
}
