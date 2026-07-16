package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditorScreenModel : ScreenModel, EditorIntent.Visitor {
    private val _state = MutableStateFlow(EditorState())
    val state: StateFlow<EditorState> = _state.asStateFlow()

    fun onIntent(intent: EditorIntent) {
        intent.accept(this)
    }

    private fun reduce(block: (EditorState) -> EditorState) {
        _state.update(block)
    }

    override fun visit(intent: EditorIntent.SelectNode) {
        reduce { it.copy(selectedNodeId = intent.id) }
    }

    override fun visit(intent: EditorIntent.AddNode) {
        val newNode = ComposeNode(
            type = intent.type,
            properties = intent.type.createDefaultProperties()
        )
        reduce { state ->
            val updatedRoot = state.rootNode.addNodeRecursive(intent.parentId, newNode)
            state.copy(rootNode = updatedRoot)
        }
    }

    override fun visit(intent: EditorIntent.DeleteNode) {
        reduce { state ->
            if (intent.id == state.rootNode.id) return@reduce state // Cannot delete root
            
            val updatedRoot = state.rootNode.deleteNodeRecursive(intent.id) ?: return@reduce state
            
            val newSelectedId = if (state.selectedNodeId != null && !updatedRoot.nodeExists(state.selectedNodeId)) {
                null
            } else {
                state.selectedNodeId
            }
            
            state.copy(
                rootNode = updatedRoot,
                selectedNodeId = newSelectedId
            )
        }
    }

    override fun visit(intent: EditorIntent.ReorderNode) {
        reduce { state ->
            val updatedRoot = state.rootNode.reorderNodeRecursive(intent.id, intent.direction)
            state.copy(rootNode = updatedRoot)
        }
    }

    override fun visit(intent: EditorIntent.UpdateNodeType) {
        reduce { state ->
            val updatedRoot = state.rootNode.updateNodeRecursive(intent.id) { node ->
                if (intent.type !in node.compatibleTypes()) return@updateNodeRecursive node
                node.copy(
                    type = intent.type,
                    properties = intent.type.createDefaultProperties(
                        preservedChildren = node.children().takeIf { it.isNotEmpty() }
                    )
                )
            }
            state.copy(rootNode = updatedRoot)
        }
    }

    override fun visit(intent: EditorIntent.UpdateNodeText) {
        reduce { state ->
            val updatedRoot = state.rootNode.updateNodeRecursive(intent.id) { node ->
                val props = node.properties
                if (props !is com.jesusdmedinac.jsontocompose.model.NodeProperties.TextProps) return@updateNodeRecursive node
                node.copy(properties = props.copy(text = intent.text))
            }
            state.copy(rootNode = updatedRoot)
        }
    }

    override fun visit(intent: EditorIntent.AddModifier) {
        reduce { state ->
            val updatedRoot = state.rootNode.updateNodeRecursive(intent.id) { node ->
                val newOperations = node.composeModifier.operations + intent.operation.createDefaultOperation()
                node.copy(composeModifier = com.jesusdmedinac.jsontocompose.model.ComposeModifier(newOperations))
            }
            state.copy(rootNode = updatedRoot)
        }
    }

    override fun visit(intent: EditorIntent.UpdateModifier) {
        reduce { state ->
            val updatedRoot = state.rootNode.updateNodeRecursive(intent.id) { node ->
                val operations = node.composeModifier.operations
                if (intent.index in operations.indices) {
                    val newOperations = operations.toMutableList().apply {
                        set(intent.index, intent.operation)
                    }
                    node.copy(composeModifier = com.jesusdmedinac.jsontocompose.model.ComposeModifier(newOperations))
                } else {
                    node
                }
            }
            state.copy(rootNode = updatedRoot)
        }
    }

    override fun visit(intent: EditorIntent.DeleteModifier) {
        reduce { state ->
            val updatedRoot = state.rootNode.updateNodeRecursive(intent.id) { node ->
                val operations = node.composeModifier.operations
                if (intent.index in operations.indices) {
                    val newOperations = operations.toMutableList().apply {
                        removeAt(intent.index)
                    }
                    node.copy(composeModifier = com.jesusdmedinac.jsontocompose.model.ComposeModifier(newOperations))
                } else {
                    node
                }
            }
            state.copy(rootNode = updatedRoot)
        }
    }

    override fun visit(intent: EditorIntent.SetLeftPanelDisplayed) {
        reduce { it.copy(isLeftPanelDisplayed = intent.displayed) }
    }

    override fun visit(intent: EditorIntent.SetRightPanelDisplayed) {
        reduce { it.copy(isRightPanelDisplayed = intent.displayed) }
    }

    override fun visit(intent: EditorIntent.SetDeviceType) {
        reduce { it.copy(deviceType = intent.deviceType) }
    }

    override fun visit(intent: EditorIntent.SetDeviceOrientation) {
        reduce { it.copy(deviceOrientation = intent.deviceOrientation) }
    }

    override fun visit(intent: EditorIntent.SetSearchKeyword) {
        reduce { it.copy(searchKeyword = intent.keyword) }
    }
}
