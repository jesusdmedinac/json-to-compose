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

    private val _sideEffect =
        MutableStateFlow<ComposeEditorSideEffect>(ComposeEditorSideEffect.Idle)
    val sideEffect: StateFlow<ComposeEditorSideEffect> = _sideEffect.asStateFlow()

    override fun onAddNewNode(composeNode: ComposeNode) {
        _state.update { state ->
            val updatedParent = addNode(composeNode)
            val updatedRoot = updateNode(updatedParent)
            state.copy(
                composeNodeRoot = updatedRoot,
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
        val updatedNode = composeNode.copy(
            text = if (composeNode.type == ComposeType.Text) "New Text Node" else null
        )
        val parentNode = updatedNode.parent ?: return updatedNode
        val updatedChildren = parentNode.children?.plus(updatedNode) ?: listOf(updatedNode)
        return parentNode.copy(children = updatedChildren)
    }

    private fun updateNode(updatedNode: ComposeNode): ComposeNode {
        return updateNodeRecursive(_state.value.composeNodeRoot, updatedNode)
    }

    private fun updateNodeRecursive(currentNode: ComposeNode, updatedNode: ComposeNode): ComposeNode {
        if (currentNode.id == updatedNode.id) {
            return updatedNode
        }
        val updatedChildren = currentNode.children?.map { child ->
            updateNodeRecursive(child, updatedNode)
        } ?: emptyList()
        return currentNode.copy(children = updatedChildren)
    }
}

data class ComposeEditorState(
    val composeNodeRoot: ComposeNode = ComposeNode(
        ComposeType.Column,
    ),
)

sealed class ComposeEditorSideEffect {
    data object Idle : ComposeEditorSideEffect()
    data class DisplayEditNodeDialog(val composeNode: ComposeNode) : ComposeEditorSideEffect()
}