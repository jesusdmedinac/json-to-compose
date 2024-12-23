package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainScreenModel : ScreenModel {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

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
}

data class MainScreenState(
    val isLeftPanelDisplayed: Boolean = true,
    val isRightPanelDisplayed: Boolean = false,
)