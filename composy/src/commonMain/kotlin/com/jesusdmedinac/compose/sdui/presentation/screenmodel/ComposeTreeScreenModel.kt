package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.jesusdmedinac.jsontocompose.ComposeNode
import com.jesusdmedinac.jsontocompose.ComposeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ComposeTreeScreenModel : ScreenModel, ComposeTreeBehavior {
    private val _state = MutableStateFlow(ComposeTreeState())
    val state: StateFlow<ComposeTreeState> = _state.asStateFlow()

    override fun onAddNewNodeToChildren(composeNode: ComposeNode) {
        _state.update { state ->
            val updatedParent = addNodeToChildren(composeNode)
            val updatedRoot = updateNode(updatedParent)
            state.copy(
                composeNodeRoot = updatedRoot,
            )
        }
    }

    override fun onAddNewNodeAsChild(composeNode: ComposeNode) {
        _state.update { state ->
            val updatedParent = addNodeToChild(composeNode)
            val updatedRoot = updateNode(updatedParent)
            state.copy(
                composeNodeRoot = updatedRoot,
            )
        }
    }

    override fun onComposeNodeSelected(composeNode: ComposeNode?) {
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

    override fun onNodeExpanded(composeNode: ComposeNode) {
        _state.update { state ->
            val updatedCollapsedNodes = if (state.collapsedNodes.contains(composeNode)) {
                state.collapsedNodes.filter { it.id != composeNode.id }
            } else {
                state.collapsedNodes.plus(composeNode)
            }
            state.copy(
                collapsedNodes = updatedCollapsedNodes
            )
        }
    }

    private fun addNodeToChildren(composeNode: ComposeNode): ComposeNode {
        val updatedNode = composeNode.applyDefaultTextIfComposeTypeIsText()
        val parentNode = updatedNode.parent ?: return updatedNode
        val updatedChildren = parentNode.children?.plus(updatedNode) ?: listOf(updatedNode)
        return parentNode.copy(children = updatedChildren)
    }

    private fun addNodeToChild(composeNode: ComposeNode): ComposeNode {
        val updatedNode = composeNode.applyDefaultTextIfComposeTypeIsText()
        val parentNode = updatedNode.parent ?: return updatedNode
        return parentNode.copy(child = updatedNode)
    }

    private fun ComposeNode.applyDefaultTextIfComposeTypeIsText(): ComposeNode {
        val updatedNode = copy(
            text = when (type) {
                ComposeType.Text -> "New Text Node"
                else -> null
            },
        )
        return updatedNode
    }

    private fun updateNode(updatedNode: ComposeNode): ComposeNode =
        updateNodeRecursive(_state.value.composeNodeRoot, updatedNode)

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
    val collapsedNodes: List<ComposeNode> = emptyList(),
) {
    fun isParentExpanded(composeNode: ComposeNode): Boolean {
        val parents = composeNode.parents()
        return parents.none { parent -> collapsedNodes.any { it.id == parent.id } }
    }
}

interface ComposeTreeBehavior {
    fun onAddNewNodeToChildren(composeNode: ComposeNode)
    fun onAddNewNodeAsChild(composeNode: ComposeNode)
    fun onComposeNodeSelected(composeNode: ComposeNode?)
    fun saveNode(composeNode: ComposeNode)
    fun onNodeExpanded(composeNode: ComposeNode)

    companion object {
        val Default = object : ComposeTreeBehavior {
            override fun onAddNewNodeToChildren(composeNode: ComposeNode) {
                TODO("onAddNewNodeToChildren is not implemented")
            }

            override fun onAddNewNodeAsChild(composeNode: ComposeNode) {
                TODO("onAddNewNodeAsChild is not implemented")
            }

            override fun onComposeNodeSelected(composeNode: ComposeNode?) {
                TODO("onComposeNodeSelected is not implemented")
            }

            override fun saveNode(composeNode: ComposeNode) {
                TODO("saveNode is not yet implemented")
            }

            override fun onNodeExpanded(composeNode: ComposeNode) {
                TODO("onNodeExpanded is not implemented")
            }
        }
    }
}