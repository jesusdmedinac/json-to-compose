package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.jesusdmedinac.compose.sdui.presentation.ui.ComposeEditorBehavior
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ComposeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ComposeTreeScreenModel : ScreenModel, ComposeEditorBehavior {
    private val _state = MutableStateFlow(ComposeTreeState())
    val state: StateFlow<ComposeTreeState> = _state.asStateFlow()

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
        _state.update { state ->
            state.copy(
                selectedComposeNode = composeNode
            )
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

    private fun updateNodeRecursive(
        currentNode: ComposeNode,
        updatedNode: ComposeNode
    ): ComposeNode {
        if (currentNode.id == updatedNode.id) {
            return updatedNode
        }
        val updatedChildren = currentNode.children?.map { child ->
            updateNodeRecursive(child, updatedNode)
        } ?: emptyList()
        return currentNode.copy(children = updatedChildren)
    }
}

data class ComposeTreeState(
    val composeNodeRoot: ComposeNode = ComposeNode(
        ComposeType.Column,
    ),
    val selectedComposeNode: ComposeNode? = null,
)