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
        val updatedRoot = _state.value.rootNode.addNodeRecursive(intent.parentId, newNode)
        reduce { it.copy(rootNode = updatedRoot) }
    }

    override fun visit(intent: EditorIntent.DeleteNode) {
        if (intent.id == "root") return // Cannot delete root
        
        val updatedRoot = _state.value.rootNode.deleteNodeRecursive(intent.id) ?: return
        
        reduce { state ->
            val newSelectedId = if (state.selectedNodeId == intent.id) null else state.selectedNodeId
            state.copy(
                rootNode = updatedRoot,
                selectedNodeId = newSelectedId
            )
        }
    }

    override fun visit(intent: EditorIntent.ReorderNode) {
        val updatedRoot = _state.value.rootNode.reorderNodeRecursive(intent.id, intent.direction)
        reduce { it.copy(rootNode = updatedRoot) }
    }

    override fun visit(intent: EditorIntent.UpdateNodeType) {
        // To be implemented in next features
    }

    override fun visit(intent: EditorIntent.UpdateNodeText) {
        // To be implemented in next features
    }

    override fun visit(intent: EditorIntent.AddModifier) {
        // To be implemented in next features
    }

    override fun visit(intent: EditorIntent.UpdateModifier) {
        // To be implemented in next features
    }

    override fun visit(intent: EditorIntent.DeleteModifier) {
        // To be implemented in next features
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
