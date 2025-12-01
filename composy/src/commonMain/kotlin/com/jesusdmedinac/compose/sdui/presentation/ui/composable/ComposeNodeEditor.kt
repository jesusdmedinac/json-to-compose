package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.composables.icons.lucide.ArrowDown
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Delete
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeBehavior
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenState
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation

@Composable
fun ComposeNodeEditor(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val editNodeScreenModel: EditNodeScreenModel = navigator.koinNavigatorScreenModel()
    val editNodeState by editNodeScreenModel.state.collectAsState()

    val selectedComposeNode = editNodeState.selectedComposeNode
    val editingComposeNode = editNodeState.editingComposeNode

    if (selectedComposeNode == null) {
        NoComposeNodeSelected(
            modifier = modifier
        )
        return
    }
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        editNodeScreenModel.onComposeNodeSelected(null)
                    },
                    modifier = Modifier
                        .pointerHoverIcon(
                            icon = PointerIcon.Hand
                        )
                ) {
                    Icon(
                        imageVector = Lucide.ArrowLeft,
                        contentDescription = null,
                    )
                }
            }
        }
        composeTypeDropdownMenu(
            editNodeState = editNodeState,
            editNodeBehavior = editNodeScreenModel
        )
        composeTextTextField(
            editNodeState = editNodeState,
            editNodeBehavior = editNodeScreenModel
        )
        composeModifierFields(
            editNodeState = editNodeState,
            editNodeBehavior = editNodeScreenModel
        )
    }
}

@Composable
private fun NoComposeNodeSelected(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No compose node selected",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun LazyListScope.composeTypeDropdownMenu(
    editNodeState: EditNodeScreenState,
    editNodeBehavior: EditNodeBehavior,
) {
    item {
        val isComposeTypeMenuExpanded = editNodeState.isComposeTypeMenuExpanded
        val editingComposeNode = editNodeState.editingComposeNode
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = isComposeTypeMenuExpanded,
                onExpandedChange = { isExpanded ->
                    editNodeBehavior.onComposeTypeMenuExpandedChange(isExpanded)
                },
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = editingComposeNode?.type?.name ?: "",
                    onValueChange = {},
                    label = {
                        Text(
                            "Compose type",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Lucide.ArrowDown,
                            "Trailing icon for exposed dropdown menu",
                            Modifier.rotate(if (isComposeTypeMenuExpanded) 180f else 360f),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isComposeTypeMenuExpanded,
                    onDismissRequest = {
                        editNodeBehavior
                            .onComposeTypeMenuExpandedChange(false)
                    }
                ) {
                    ComposeType.entries.forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                editNodeBehavior.onComposeTypeSelected(type)
                            }
                        ) {
                            Text(text = type.name)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun LazyListScope.composeTextTextField(
    editNodeState: EditNodeScreenState,
    editNodeBehavior: EditNodeBehavior,
) {
    item {
        val editingComposeNode = editNodeState.editingComposeNode
        val props = editingComposeNode?.properties as? NodeProperties.TextProps
            ?: return@item
        if (props.text != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = props.text ?: "",
                    onValueChange = {
                        editNodeBehavior.onComposeNodeTextChange(it)
                    },
                    label = {
                        Text(
                            text = "Text",
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun LazyListScope.composeModifierFields(
    editNodeState: EditNodeScreenState,
    editNodeBehavior: EditNodeBehavior,
) {
    item {
        Text(
            text = "Modifier",
            modifier = Modifier.padding(16.dp),
        )
    }

    itemsIndexed(
        editNodeState.editingComposeNode?.composeModifier?.operations ?: emptyList()
    ) { index, operation ->
        operation.ToComposeModifierOperation(
            editNodeBehavior = editNodeBehavior,
            operationIndex = index,
        )
    }

    item {
        val isModifierMenuExpanded = editNodeState.isModifierMenuExpanded

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = isModifierMenuExpanded,
                onExpandedChange = {
                    editNodeBehavior.onModifierMenuExpandedChange(true)
                },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedIconButton(
                        onClick = {
                            editNodeBehavior.onModifierMenuExpandedChange(true)
                        }
                    ) {
                        Icon(
                            imageVector = Lucide.Plus,
                            contentDescription = "Add modifier",
                            Modifier.rotate(if (isModifierMenuExpanded) 45f else 360f),
                        )
                    }
                }
                ExposedDropdownMenu(
                    expanded = isModifierMenuExpanded,
                    onDismissRequest = {
                        editNodeBehavior.onModifierMenuExpandedChange(false)
                    }
                ) {
                    ModifierOperation.entries.forEach { operation ->
                        DropdownMenuItem(
                            onClick = {
                                editNodeBehavior.onModifierOperationSelected(operation)
                            }
                        ) {
                            Text(text = operation.name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComposeModifier.Operation.ToComposeModifierOperation(
    operationIndex: Int,
    modifier: Modifier = Modifier,
    editNodeBehavior: EditNodeBehavior,
) {
    val value = when (this) {
        is ComposeModifier.Operation.BackgroundColor -> this.hexColor
        is ComposeModifier.Operation.Height -> this.value
        is ComposeModifier.Operation.Padding -> this.value
        is ComposeModifier.Operation.Width -> this.value
        else -> ""
    }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value.toString())) }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            editNodeBehavior.onModifierOperationNameChange(
                this,
                operationValue = it.text,
                operationIndex,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        label = {
            Text(
                text = modifierOperation?.name ?: ""
            )
        },
        trailingIcon = {
            OutlinedIconButton(
                onClick = {

                },
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    imageVector = Lucide.Delete,
                    contentDescription = "Delete modifier",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}