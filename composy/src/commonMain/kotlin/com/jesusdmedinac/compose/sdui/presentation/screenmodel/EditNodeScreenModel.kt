package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.jesusdmedinac.jsontocompose.ComposeModifier
import com.jesusdmedinac.jsontocompose.ComposeNode
import com.jesusdmedinac.jsontocompose.ComposeType
import com.jesusdmedinac.jsontocompose.ModifierOperation
import com.jesusdmedinac.jsontocompose.NodeProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditNodeScreenModel : ScreenModel, EditNodeBehavior {
    private val _state = MutableStateFlow(EditNodeScreenState())
    val state: StateFlow<EditNodeScreenState> = _state.asStateFlow()

    override fun onComposeTypeMenuExpandedChange(expanded: Boolean) {
        _state.update { state ->
            state.copy(
                isComposeTypeMenuExpanded = expanded
            )
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
    }

    override fun onComposeNodeSelected(composeNode: ComposeNode?) {
        _state.update { state ->
            state.copy(
                selectedComposeNode = composeNode,
                editingComposeNode = composeNode
            )
        }
    }

    override fun onComposeNodeTextChange(text: String) {
        _state.update { state ->
            val editingComposeNode = state.editingComposeNode
            val props = editingComposeNode?.properties as? NodeProperties.TextProps
                ?: return
            val updatedProps = props.copy(text = text)
            val updatedEditingComposeNode = state.editingComposeNode
                .copy(properties = updatedProps)
            state.copy(editingComposeNode = updatedEditingComposeNode)
        }
    }

    override fun onModifierMenuExpandedChange(expanded: Boolean) {
        _state.update { state ->
            state.copy(
                isModifierMenuExpanded = expanded
            )
        }
    }

    override fun onModifierOperationSelected(modifierOperation: ModifierOperation) {
        _state.update { state ->
            val operation = when (modifierOperation) {
                ModifierOperation.Padding -> ComposeModifier.Operation.Padding(0)
                ModifierOperation.FillMaxSize -> ComposeModifier.Operation.FillMaxSize
                ModifierOperation.FillMaxWidth -> ComposeModifier.Operation.FillMaxWidth
                ModifierOperation.FillMaxHeight -> ComposeModifier.Operation.FillMaxHeight
                ModifierOperation.Width -> ComposeModifier.Operation.Width(0)
                ModifierOperation.Height -> ComposeModifier.Operation.Height(0)
                ModifierOperation.BackgroundColor -> ComposeModifier.Operation.BackgroundColor("#FFFFFFFF")
            }
            val editingComposeNode = state.editingComposeNode?.copy(
                composeModifier = state.editingComposeNode.composeModifier.copy(
                    operations = state.editingComposeNode.composeModifier.operations.plus(operation)
                )
            )
            state.copy(
                isModifierMenuExpanded = false,
                editingComposeNode = editingComposeNode,
            )
        }
    }

    override fun onModifierOperationNameChange(
        operation: ComposeModifier.Operation,
        operationValue: String,
        operationIndex: Int,
    ) {
        _state.update { state ->
            val operations = state
                .editingComposeNode
                ?.composeModifier
                ?.operations
                ?.toMutableList()
                ?.mapIndexed { index, operation ->
                    if (index == operationIndex) {
                        when (operation) {
                            is ComposeModifier.Operation.Width -> ComposeModifier.Operation.Width(
                                operationValue.toIntOrNull() ?: 0
                            )

                            is ComposeModifier.Operation.Height -> ComposeModifier.Operation.Height(
                                operationValue.toIntOrNull() ?: 0
                            )

                            is ComposeModifier.Operation.Padding -> ComposeModifier.Operation.Padding(
                                operationValue.toIntOrNull() ?: 0
                            )

                            is ComposeModifier.Operation.BackgroundColor -> ComposeModifier.Operation.BackgroundColor(
                                operationValue
                            )

                            else -> operation
                        }
                    } else {
                        operation
                    }
                }
                ?: emptyList()
            val composeModifier = state
                .editingComposeNode
                ?.composeModifier
                ?.copy(
                    operations = operations
                )
                ?: ComposeModifier()
            val editingComposeNode = state.editingComposeNode?.copy(
                composeModifier = composeModifier
            )
            state.copy(
                editingComposeNode = editingComposeNode
            )
        }
    }
}

data class EditNodeScreenState(
    val isComposeTypeMenuExpanded: Boolean = false,
    val selectedComposeNode: ComposeNode? = null,
    val editingComposeNode: ComposeNode? = null,
    val isModifierMenuExpanded: Boolean = false,
)

interface EditNodeBehavior {
    fun onComposeTypeMenuExpandedChange(expanded: Boolean)
    fun onComposeTypeSelected(type: ComposeType)
    fun onComposeNodeSelected(composeNode: ComposeNode?)
    fun onComposeNodeTextChange(text: String)
    fun onModifierMenuExpandedChange(expanded: Boolean)
    fun onModifierOperationSelected(modifierOperation: ModifierOperation)
    fun onModifierOperationNameChange(
        operation: ComposeModifier.Operation,
        operationValue: String,
        operationIndex: Int,
    )
}