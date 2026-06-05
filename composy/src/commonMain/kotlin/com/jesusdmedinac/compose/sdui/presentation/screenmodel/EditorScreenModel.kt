package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel

import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditorScreenModel : ScreenModel {
    private val _state = MutableStateFlow(EditorState())
    val state: StateFlow<EditorState> = _state.asStateFlow()

    fun onIntent(intent: EditorIntent) {
        when (intent) {
            is EditorIntent.SelectNode -> {
                // To be implemented in next features
            }
            is EditorIntent.AddNode -> {
                // To be implemented in next features
            }
            is EditorIntent.DeleteNode -> {
                // To be implemented in next features
            }
            is EditorIntent.UpdateNodeType -> {
                // To be implemented in next features
            }
            is EditorIntent.UpdateNodeText -> {
                // To be implemented in next features
            }
            is EditorIntent.AddModifier -> {
                // To be implemented in next features
            }
            is EditorIntent.UpdateModifier -> {
                // To be implemented in next features
            }
            is EditorIntent.DeleteModifier -> {
                // To be implemented in next features
            }
            is EditorIntent.SetLeftPanelDisplayed -> {
                // To be implemented in next features
            }
            is EditorIntent.SetRightPanelDisplayed -> {
                // To be implemented in next features
            }
            is EditorIntent.SetDeviceType -> {
                // To be implemented in next features
            }
            is EditorIntent.SetDeviceOrientation -> {
                // To be implemented in next features
            }
            is EditorIntent.SetSearchKeyword -> {
                // To be implemented in next features
            }
        }
    }
}
