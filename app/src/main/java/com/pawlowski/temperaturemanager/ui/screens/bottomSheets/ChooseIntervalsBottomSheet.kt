package com.pawlowski.temperaturemanager.ui.screens.bottomSheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseIntervalsBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    initialReadingInterval: Long,
    initialPushInterval: Long,
    onConfirm: (Long, Long) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    if (show) {
        val scope = rememberCoroutineScope()
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            windowInsets = WindowInsets.safeContent,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 50.dp)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = "Edytuj częstotliwość odczytów",
                    style = MaterialTheme.typography.titleMedium,
                )
                val readingIntervalState = remember(initialReadingInterval) {
                    mutableStateOf(initialReadingInterval.toString())
                }
                TextField(
                    value = readingIntervalState.value,
                    label = {
                        Text(text = "Reading interval")
                    },
                    onValueChange = {
                        readingIntervalState.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                val pushIntervalState = remember(initialPushInterval) {
                    mutableStateOf(initialPushInterval.toString())
                }
                TextField(
                    value = pushIntervalState.value,
                    label = {
                        Text(text = "Push interval")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    onValueChange = {
                        pushIntervalState.value = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                Button(
                    onClick = {
                        scope.launch {
                            bottomSheetState.hide()
                            onConfirm(
                                readingIntervalState.value.toLong(),
                                pushIntervalState.value.toLong(),
                            )
                        }
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.White,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Edytuj")
                }

                Button(
                    onClick = {
                        scope.launch {
                            bottomSheetState.hide()
                            onDismiss()
                        }
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Anuluj")
                }
            }
        }
    }
}
