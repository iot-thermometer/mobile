package com.pawlowski.temperaturemanager.ui.screens.bottomSheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddAlertBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Float?, Float?, Float?, Float?) -> Unit,
) {
    BaseBottomSheet(show = show, onDismiss = onDismiss) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 15.dp)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "Dodaj alert",
                style = MaterialTheme.typography.titleMedium,
            )
            val minTempState = remember {
                mutableStateOf("")
            }
            val isMinTempEnabledState = remember {
                mutableStateOf(false)
            }
            val maxTempState = remember {
                mutableStateOf("")
            }
            val isMaxTempEnabledState = remember {
                mutableStateOf(false)
            }
            Row {
                CheckBoxWithInput(
                    isEnabled = isMinTempEnabledState.value,
                    value = minTempState.value,
                    label = "Min temp",
                    onEnabledChange = {
                        isMinTempEnabledState.value = it
                    },
                    onValueChange = {
                        minTempState.value = it
                    },
                    modifier = Modifier.weight(weight = 1f),
                )
                CheckBoxWithInput(
                    isEnabled = isMaxTempEnabledState.value,
                    value = maxTempState.value,
                    label = "Max temp",
                    onEnabledChange = {
                        isMaxTempEnabledState.value = it
                    },
                    onValueChange = {
                        maxTempState.value = it
                    },
                    modifier = Modifier.weight(weight = 1f),
                )
            }

            val minSoilState = remember {
                mutableStateOf("")
            }
            val isMinSoilEnabledState = remember {
                mutableStateOf(false)
            }
            val maxSoilState = remember {
                mutableStateOf("")
            }
            val isMaxSoilEnabledState = remember {
                mutableStateOf(false)
            }
            Row {
                CheckBoxWithInput(
                    isEnabled = isMinSoilEnabledState.value,
                    value = minSoilState.value,
                    label = "Min soil",
                    onEnabledChange = {
                        isMinSoilEnabledState.value = it
                    },
                    onValueChange = {
                        minSoilState.value = it
                    },
                    modifier = Modifier.weight(weight = 1f),
                )

                CheckBoxWithInput(
                    isEnabled = isMaxSoilEnabledState.value,
                    value = maxSoilState.value,
                    label = "Max soil",
                    onEnabledChange = {
                        isMaxSoilEnabledState.value = it
                    },
                    onValueChange = {
                        maxSoilState.value = it
                    },
                    modifier = Modifier.weight(weight = 1f),
                )
            }

            Button(
                onClick = {
                    hideBottomSheetWithAction {
                        onConfirm(
                            minTempState.value.takeIf { isMinTempEnabledState.value }
                                ?.toFloatOrNull(),
                            maxTempState.value.takeIf { isMaxTempEnabledState.value }
                                ?.toFloatOrNull(),
                            minSoilState.value.takeIf { isMinSoilEnabledState.value }
                                ?.toFloatOrNull(),
                            maxSoilState.value.takeIf { isMaxSoilEnabledState.value }
                                ?.toFloatOrNull(),
                        )
                    }
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF355CA8),
                    contentColor = Color.White,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Dodaj")
            }

            Button(
                onClick = {
                    dismissBottomSheet()
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
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

@Composable
private fun CheckBoxWithInput(
    isEnabled: Boolean,
    value: String,
    label: String,
    onEnabledChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Checkbox(checked = isEnabled, onCheckedChange = onEnabledChange)
        TextField(
            value = value,
            label = {
                Text(text = label)
            },
            onValueChange = onValueChange,
            enabled = isEnabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun AddAlertBottomSheetPreview() {
    AddAlertBottomSheet(
        show = true,
        onDismiss = {},
        onConfirm = { minTemp, maxTemp, minSoil, maxSoil -> },
    )
}
