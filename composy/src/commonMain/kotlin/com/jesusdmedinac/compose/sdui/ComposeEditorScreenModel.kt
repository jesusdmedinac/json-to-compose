package com.jesusdmedinac.compose.sdui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ComposeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
            val updatedParent = addNode(composeNode)
            val updatedRoot = updateNode(updatedParent)
            state.copy(
                composeNodeRoot = updatedRoot,
                isAddNewNodeMenuDisplayed = false
            )
        }
    }

    override fun onEditNodeClick(composeNode: ComposeNode) {
        screenModelScope.launch {
            _sideEffect.update {
                ComposeEditorSideEffect.DisplayEditNodeDialog(composeNode)
            }
            delay(100)
            _sideEffect.update {
                ComposeEditorSideEffect.Idle
            }
        }
    }

    override fun saveNode(composeNode: ComposeNode) {
        _state.update { state ->
            val updatedRoot = updateNode(composeNode)
            state.copy(
                composeNodeRoot = updatedRoot
            )
        }
    }

    private fun addNode(composeNode: ComposeNode): ComposeNode {
        val parent = composeNode.parent ?: return composeNode
        val updatedChild = composeNode.copy(
            parent = parent,
            text = if (composeNode.type == ComposeType.Text) "New Text" else null
        )
        val updatedParent = parent.copy(
            children = parent.children?.plus(updatedChild) ?: listOf(updatedChild)
        )
        return updatedParent
    }

    private fun updateNode(composeNode: ComposeNode): ComposeNode {
        val parent = composeNode.parent ?: return composeNode
        val updatedParent = parent.copy(
            children = parent.children?.map {
                if (it.id == composeNode.id) {
                    composeNode
                } else {
                    it
                }
            } ?: parent.children
        )
        return updateNode(updatedParent)
    }
}

data class ComposeEditorState(
    val composeNodeRoot: ComposeNode = ComposeNode(
        ComposeType.Column,
    ),
    val isAddNewNodeMenuDisplayed: Boolean = false,
)

sealed class ComposeEditorSideEffect {
    data object Idle : ComposeEditorSideEffect()
    data class DisplayEditNodeDialog(val composeNode: ComposeNode) : ComposeEditorSideEffect()
}