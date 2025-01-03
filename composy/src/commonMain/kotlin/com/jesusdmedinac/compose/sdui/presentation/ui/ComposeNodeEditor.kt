package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeBehavior
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenState
import com.jesusdmedinac.jsontocompose.ComposeModifier
import com.jesusdmedinac.jsontocompose.ComposeType
import com.jesusdmedinac.jsontocompose.ModifierOperation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComposeNodeEditor(
    editNodeState: EditNodeScreenState,
    editNodeBehavior: EditNodeBehavior,
) {
    val selectedComposeNode = editNodeState.selectedComposeNode
    val editingComposeNode = editNodeState.editingComposeNode

    if (selectedComposeNode == null) {
        NoComposeNodeSelected()
        return
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        editNodeBehavior.onComposeNodeSelected(null)
                    },
                    modifier = Modifier
                        .pointerHoverIcon(
                            icon = PointerIcon.Hand
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                TextButton(
                    onClick = {
                        editNodeBehavior.onComposeNodeSelected(null)
                        editingComposeNode?.let { editNodeBehavior.onSaveNodeClick(it) }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContentColor = Color.Gray,
                    ),
                    enabled = selectedComposeNode != editingComposeNode,
                    modifier = Modifier
                        .pointerHoverIcon(
                            icon = PointerIcon.Hand
                        )
                ) {
                    Text(
                        "Save",
                    )
                }
            }
        }
        composeTypeDropdownMenu(
            editNodeState = editNodeState,
            editNodeBehavior = editNodeBehavior
        )
        composeTextTextField(
            editNodeState = editNodeState,
            editNodeBehavior = editNodeBehavior
        )
        composeModifierFields(
            editNodeState = editNodeState,
            editNodeBehavior = editNodeBehavior
        )
    }
}

@Composable
fun NoComposeNodeSelected() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No compose node selected",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.composeTypeDropdownMenu(
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
                onExpandedChange = { editNodeBehavior.onComposeTypeMenuExpandedChange(true) },
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
                            Icons.Filled.ArrowDropDown,
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
fun LazyListScope.composeTextTextField(
    editNodeState: EditNodeScreenState,
    editNodeBehavior: EditNodeBehavior,
) {
    item {
        val editingComposeNode = editNodeState.editingComposeNode
        if (editingComposeNode?.text != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = editingComposeNode.text ?: "",
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
fun LazyListScope.composeModifierFields(
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
                            imageVector = Icons.Filled.Add,
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
fun ComposeModifier.Operation.ToComposeModifierOperation(
    operationIndex: Int,
    modifier: Modifier = Modifier,
    editNodeBehavior: EditNodeBehavior,
) {
    val value = when (this) {
        is ComposeModifier.Operation.BackgroundColor -> this.color
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
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete modifier",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}