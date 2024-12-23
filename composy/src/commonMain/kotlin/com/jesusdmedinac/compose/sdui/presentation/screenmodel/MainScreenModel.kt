package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.kotlin.fibonacci.ComposeNode
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainScreenModel : ScreenModel {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    private val _sideEffect = MutableStateFlow<MainScreenSideEffect>(MainScreenSideEffect.Idle)
    val sideEffect: StateFlow<MainScreenSideEffect> = _sideEffect

    fun onDisplayLeftPanelChange(isLeftPanelDisplayed: Boolean) {
        _state.update { state ->
            state.copy(isLeftPanelDisplayed = isLeftPanelDisplayed)
        }
    }

    fun onDisplayRightPanelChange(isRightPanelDisplayed: Boolean) {
        _state.update { state ->
            state.copy(isRightPanelDisplayed = isRightPanelDisplayed)
        }
    }

    fun exportAsJSON(composeTree: ComposeNode) {
        screenModelScope.launch {
            _sideEffect.emit(MainScreenSideEffect.ExportAsJSON(
                baseName = "composeAsJSON",
                extension = "json",
                initialDirectory = "",
                content = Json.encodeToString(composeTree)
            ))
            delay(100)
            _sideEffect.emit(MainScreenSideEffect.Idle)
        }
    }
}

data class MainScreenState(
    val isLeftPanelDisplayed: Boolean = true,
    val isRightPanelDisplayed: Boolean = false,
)

sealed class MainScreenSideEffect {
    data object Idle : MainScreenSideEffect()
    data class ExportAsJSON(
        val baseName: String,
        val extension: String,
        val initialDirectory: String,
        val content: String,
    ) : MainScreenSideEffect()
}