package com.jesusdmedinac.compose.sdui

import cafe.adriel.voyager.core.model.ScreenModel
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ComposeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainScreenModel : ScreenModel {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    fun onDisplayLeftPanelClick() {
        _state.update { state ->
            state.copy(isLeftPanelDisplayed = true)
        }
    }

    fun onDismissLeftPanelClick() {
        _state.update { state ->
            state.copy(isLeftPanelDisplayed = false)
        }
    }
}

data class MainScreenState(
    val composeNodeRoot: ComposeNode = ComposeNode(
        ComposeType.Layout.Column,
        children = emptyList()
    ),
    val isLeftPanelDisplayed: Boolean = true,
)