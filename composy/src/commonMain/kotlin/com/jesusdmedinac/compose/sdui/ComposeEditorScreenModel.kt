package com.jesusdmedinac.compose.sdui

import cafe.adriel.voyager.core.model.ScreenModel
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ComposeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ComposeEditorScreenModel : ScreenModel, ComposeEditorBehavior {
    private val _state = MutableStateFlow(ComposeEditorState())
    val state: StateFlow<ComposeEditorState> = _state.asStateFlow()

    private val _sideEffect = MutableStateFlow<ComposeEditorSideEffect>(ComposeEditorSideEffect.Idle)
    val sideEffect: StateFlow<ComposeEditorSideEffect> = _sideEffect.asStateFlow()

    override fun onAddNewNodeClick() {
        _state.update { state ->
            state.copy(
                isAddNewNodeMenuDisplayed = true
            )
        }
    }

    override fun onAddNewNodeMenuDismiss() {
        _state.update { state ->
            state.copy(
                isAddNewNodeMenuDisplayed = false
            )
        }
    }

    override fun onAddNewNode(composeNode: ComposeNode) {
        _state.update { state ->
            val composeNodeRoot = state.composeNodeRoot.copy(
                children = state.composeNodeRoot.children?.plus(composeNode)
            )
            state.copy(
                composeNodeRoot = composeNodeRoot,
                isAddNewNodeMenuDisplayed = false
            )
        }
    }

    override fun onEditNodeClick(composeNode: ComposeNode) {
        _sideEffect.update {
            ComposeEditorSideEffect.DisplayEditNodeDialog
        }
    }
}

data class ComposeEditorState(
    val composeNodeRoot: ComposeNode = ComposeNode(
        ComposeType.Layout.Column,
        children = emptyList()
    ),
    val isAddNewNodeMenuDisplayed: Boolean = false,
)

sealed class ComposeEditorSideEffect {
    data object Idle : ComposeEditorSideEffect()
    data object DisplayEditNodeDialog : ComposeEditorSideEffect()
}