package com.jesusdmedinac.compose.sdui.presentation.screenmodel

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

class EditNodeScreenModel : ScreenModel, EditNodeBehavior {
    private val _state = MutableStateFlow(EditNodeScreenState())
    val state: StateFlow<EditNodeScreenState> = _state.asStateFlow()

    private val _sideEffect = MutableStateFlow<EditNodeSideEffect>(EditNodeSideEffect.Idle)
    val sideEffect: StateFlow<EditNodeSideEffect> = _sideEffect.asStateFlow()

    override fun onComposeTypeMenuExpandedChange(expanded: Boolean) {
        _state.update { state ->
            state.copy(
                isComposeTypeMenuExpanded = expanded
            )
        }
    }

    override fun onSaveNodeClick(composeNode: ComposeNode) {
        screenModelScope.launch {
            _sideEffect.update {
                EditNodeSideEffect.SaveNode(composeNode)
            }
            delay(100)
            _sideEffect.update {
                EditNodeSideEffect.Idle
            }
        }
    }

    override fun onComposeTypeSelected(type: ComposeType) {
        _state.update { state ->
            val editingComposeNode = state.editingComposeNode?.copy(
                type = type
            )
            state.copy(
                isComposeTypeMenuExpanded = false,
                editingComposeNode = editingComposeNode
            )
        }
        println(type)
    }

    override fun onComposeNodeSelected(composeNode: ComposeNode?) {
        _state.update { state ->
            state.copy(
                selectedComposeNode = composeNode,
                editingComposeNode = composeNode
            )
        }
    }
}

data class EditNodeScreenState(
    val isComposeTypeMenuExpanded: Boolean = false,
    val selectedComposeNode: ComposeNode? = null,
    val editingComposeNode: ComposeNode? = null,
)

sealed class EditNodeSideEffect {
    data object Idle : EditNodeSideEffect()
    data class SaveNode(val composeNode: ComposeNode) : EditNodeSideEffect()
}

interface EditNodeBehavior {
    fun onComposeTypeMenuExpandedChange(expanded: Boolean)
    fun onSaveNodeClick(composeNode: ComposeNode)
    fun onComposeTypeSelected(type: ComposeType)
    fun onComposeNodeSelected(composeNode: ComposeNode?)
}