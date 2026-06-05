package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
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
        val updatedNode = composeNode // Removed applyDefaultTextIfComposeTypeIsText
        val parentNode = updatedNode.parent ?: return updatedNode
        
        val visitor = AppendChildVisitor(updatedNode)
        val updatedProperties = parentNode.properties.accept(visitor)
        
        return parentNode.copy(properties = updatedProperties)
    }

    private fun updateNode(updatedNode: ComposeNode): ComposeNode =
        updateNodeRecursive(_state.value.composeNodeRoot, updatedNode)

    private fun updateNodeRecursive(currentNode: ComposeNode, updatedNode: ComposeNode): ComposeNode {
        if (currentNode.id == updatedNode.id) return updatedNode
        
        val visitor = UpdateChildrenVisitor(updatedNode, ::updateNodeRecursive)
        val updatedProps = currentNode.properties.accept(visitor)
        
        return currentNode.copy(properties = updatedProps)
    }
}

data class ComposeTreeState(
    val composeNodeRoot: ComposeNode = ComposeNode(
        type = ComposeType.Box,
        properties = ComposeType.Box.createDefaultProperties()
    ),
    val selectedComposeNode: ComposeNode? = null,
    val collapsedNodes: List<ComposeNode> = emptyList(),
) {
    fun isSelected(composeNode: ComposeNode): Boolean {
        return selectedComposeNode?.id == composeNode.id
    }
    fun isParentExpanded(composeNode: ComposeNode): Boolean {
        val parents = composeNode.parents()
        return parents.none { parent -> collapsedNodes.any { it.id == parent.id } }
    }
}

interface ComposeTreeBehavior {
    fun onAddNewNodeToChildren(composeNode: ComposeNode)
    fun onComposeNodeSelected(composeNode: ComposeNode?)
    fun saveNode(composeNode: ComposeNode)
    fun onNodeExpanded(composeNode: ComposeNode)

    companion object {
        val Default = object : ComposeTreeBehavior {
            override fun onAddNewNodeToChildren(composeNode: ComposeNode) {
                TODO("onAddNewNodeToChildren is not implemented")
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