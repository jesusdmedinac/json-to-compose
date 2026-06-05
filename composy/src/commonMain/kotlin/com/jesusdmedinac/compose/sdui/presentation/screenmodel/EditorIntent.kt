package com.jesusdmedinac.compose.sdui.presentation.screenmodel


import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation

sealed interface EditorIntent {
    // Tree Actions
    data class SelectNode(val id: String?) : EditorIntent
    data class AddNode(val parentId: String, val type: ComposeType) : EditorIntent
    data class DeleteNode(val id: String) : EditorIntent
    
    // Editor Actions
    data class UpdateNodeType(val id: String, val type: ComposeType) : EditorIntent
    data class UpdateNodeText(val id: String, val text: String) : EditorIntent
    data class AddModifier(val id: String, val operation: ModifierOperation) : EditorIntent
    data class UpdateModifier(val id: String, val index: Int, val operation: ComposeModifier.Operation) : EditorIntent
    data class DeleteModifier(val id: String, val index: Int) : EditorIntent

    // UI Actions
    data class SetLeftPanelDisplayed(val displayed: Boolean) : EditorIntent
    data class SetRightPanelDisplayed(val displayed: Boolean) : EditorIntent
    data class SetDeviceType(val deviceType: DeviceType) : EditorIntent
    data class SetDeviceOrientation(val deviceOrientation: DeviceOrientation) : EditorIntent
    data class SetSearchKeyword(val keyword: String) : EditorIntent
}
