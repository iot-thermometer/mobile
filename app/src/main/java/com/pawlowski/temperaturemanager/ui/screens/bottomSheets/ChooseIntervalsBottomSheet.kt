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
fun ChooseIntervalsBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    initialReadingInterval: Long,
    initialPushInterval: Long,
    onConfirm: (Long, Long) -> Unit,
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
                text = "Edytuj częstotliwość odczytów",
                style = MaterialTheme.typography.titleMedium,
            )
            val showErrorsIfAny =
                remember {
                    mutableStateOf(false)
                }
            val readingIntervalState =
                remember(initialReadingInterval) {
                    mutableStateOf(initialReadingInterval.toString())
                }
            TextField(
                value = readingIntervalState.value,
                label = {
                    Text(text = "Częstotliwość pomiarów (sekundy)")
                },
                onValueChange = {
                    readingIntervalState.value = it
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                isError =
                    showErrorsIfAny.value && !readingIntervalState.value.isReadIntervalCorrect(),
                supportingText = {
                    Text(text = "Wpisz wartość większą lub równą 4")
                },
                modifier = Modifier.fillMaxWidth(),
            )
            val pushIntervalState =
                remember(initialPushInterval) {
                    mutableStateOf(initialPushInterval.toString())
                }
            TextField(
                value = pushIntervalState.value,
                label = {
                    Text(text = "Częstotliwość wysyłania (co ile pomiarów)")
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                onValueChange = {
                    pushIntervalState.value = it
                },
                isError = showErrorsIfAny.value && !pushIntervalState.value.isPushIntervalCorrect(),
                supportingText = {
                    Text(text = "Wpisz wartość większą lub równą 1")
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    if (readingIntervalState.value.isReadIntervalCorrect() &&
                        pushIntervalState.value.isPushIntervalCorrect()
                    ) {
                        hideBottomSheetWithAction {
                            onConfirm(
                                readingIntervalState.value.toLong(),
                                pushIntervalState.value.toLong(),
                            )
                        }
                    } else {
                        showErrorsIfAny.value = true
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

private fun String.isReadIntervalCorrect() =
    isNotBlank() && toIntOrNull()?.let {
        it >= 4
    } ?: false

private fun String.isPushIntervalCorrect() =
    isNotBlank() && toIntOrNull()?.let {
        it != 0
    } ?: false
