package com.jesusdmedinac.compose.sdui

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainScreenModel : ScreenModel {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    fun onDisplayLeftPanelClick() {
        _state.update { state ->
            state.copy(isLeftPanelDisplayed = !state.isLeftPanelDisplayed)
        }
    }
}

data class MainScreenState(
    val isLeftPanelDisplayed: Boolean = true,
)