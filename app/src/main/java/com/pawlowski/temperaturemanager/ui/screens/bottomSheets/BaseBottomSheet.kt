package com.pawlowski.temperaturemanager.ui.screens.bottomSheets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetScope constructor(
    private val scope: CoroutineScope,
    private val bottomSheetState: SheetState,
    private val onDismiss: () -> Unit,
) {

    fun dismissBottomSheet() {
        scope.launch {
            bottomSheetState.hide()
            onDismiss()
        }
    }

    fun hideBottomSheetWithAction(onFinish: () -> Unit) {
        scope.launch {
            bottomSheetState.hide()
            onFinish()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    content: @Composable BottomSheetScope.() -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    if (show) {
        val scope = rememberCoroutineScope()
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
        ) {
            val scope = remember(scope, bottomSheetState) {
                BottomSheetScope(
                    scope = scope,
                    bottomSheetState = bottomSheetState,
                    onDismiss = onDismiss,
                )
            }

            scope.content()
        }
    }
}
