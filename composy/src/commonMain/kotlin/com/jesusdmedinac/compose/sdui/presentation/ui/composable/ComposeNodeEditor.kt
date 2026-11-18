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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.TextButton
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
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditorIntent
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditorScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.compatibleTypes
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation

@Composable
fun ComposeNodeEditor(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val editorScreenModel: EditorScreenModel = navigator.koinNavigatorScreenModel()
    val state by editorScreenModel.state.collectAsState()

    val selectedNode = state.selectedNode

    if (selectedNode == null) {
        NoComposeNodeSelected(modifier = modifier)
        return
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Node") },
            text = { Text("Are you sure you want to delete this node? All its children will also be deleted.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        editorScreenModel.onIntent(EditorIntent.DeleteNode(selectedNode.id))
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    val isRoot = state.rootNode.id == selectedNode.id

    LazyColumn(
        modifier = modifier
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        editorScreenModel.onIntent(EditorIntent.SelectNode(null))
                    },
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Icon(
                        imageVector = Lucide.ArrowLeft,
                        contentDescription = "Deselect node",
                    )
                }

                if (!isRoot) {
                    IconButton(
                        onClick = {
                            if (selectedNode.children().isNotEmpty()) {
                                showDeleteDialog = true
                            } else {
                                editorScreenModel.onIntent(EditorIntent.DeleteNode(selectedNode.id))
                            }
                        },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(
                            imageVector = Lucide.Delete,
                            contentDescription = "Delete Node",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        composeTypeDropdownMenu(
            selectedNode = selectedNode,
            onTypeSelected = { type ->
                editorScreenModel.onIntent(EditorIntent.UpdateNodeType(selectedNode.id, type))
            }
        )

        composeTextTextField(
            selectedNode = selectedNode,
            onTextChanged = { text ->
                editorScreenModel.onIntent(EditorIntent.UpdateNodeText(selectedNode.id, text))
            }
        )

        composeModifierFields(
            selectedNode = selectedNode,
            onAddModifier = { op ->
                editorScreenModel.onIntent(EditorIntent.AddModifier(selectedNode.id, op))
            },
            onUpdateModifier = { index, op ->
                editorScreenModel.onIntent(EditorIntent.UpdateModifier(selectedNode.id, index, op))
            },
            onDeleteModifier = { index ->
                editorScreenModel.onIntent(EditorIntent.DeleteModifier(selectedNode.id, index))
            }
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
    selectedNode: ComposeNode,
    onTypeSelected: (ComposeType) -> Unit,
) {
    item {
        var isExpanded by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedNode.type.name,
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
                            Modifier.rotate(if (isExpanded) 180f else 360f),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    val compatibleTypes = selectedNode.compatibleTypes()
                    compatibleTypes.forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                onTypeSelected(type)
                                isExpanded = false
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
    selectedNode: ComposeNode,
    onTextChanged: (String) -> Unit,
) {
    item {
        val props = selectedNode.properties as? NodeProperties.TextProps ?: return@item
        if (props.text != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = props.text ?: "",
                    onValueChange = onTextChanged,
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
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun LazyListScope.composeModifierFields(
    selectedNode: ComposeNode,
    onAddModifier: (ModifierOperation) -> Unit,
    onUpdateModifier: (Int, ComposeModifier.Operation) -> Unit,
    onDeleteModifier: (Int) -> Unit,
) {
    item {
        Text(
            text = "Modifier",
            modifier = Modifier.padding(16.dp),
        )
    }

    itemsIndexed(selectedNode.composeModifier.operations) { index, operation ->
        operation.ToComposeModifierOperation(
            operationIndex = index,
            onUpdate = { newOp -> onUpdateModifier(index, newOp) },
            onDelete = { onDeleteModifier(index) }
        )
    }

    item {
        var isModifierMenuExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = isModifierMenuExpanded,
                onExpandedChange = { isModifierMenuExpanded = it },
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedIconButton(
                        onClick = { isModifierMenuExpanded = true }
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
                    onDismissRequest = { isModifierMenuExpanded = false }
                ) {
                    ModifierOperation.entries.forEach { operation ->
                        DropdownMenuItem(
                            onClick = {
                                onAddModifier(operation)
                                isModifierMenuExpanded = false
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
    onUpdate: (ComposeModifier.Operation) -> Unit,
    onDelete: () -> Unit,
) {
    val value = when (this) {
        is ComposeModifier.Operation.BackgroundColor -> this.hexColor
        is ComposeModifier.Operation.Height -> this.value.toString()
        is ComposeModifier.Operation.Padding -> this.value.toString()
        is ComposeModifier.Operation.Width -> this.value.toString()
        else -> ""
    }
    var textFieldValue by remember(this) { mutableStateOf(TextFieldValue(value)) }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            val updated = this.withUpdatedValue(it.text)
            onUpdate(updated)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        label = {
            Text(text = modifierOperation?.name ?: "")
        },
        trailingIcon = {
            OutlinedIconButton(
                onClick = onDelete,
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

private fun ComposeModifier.Operation.withUpdatedValue(valueText: String): ComposeModifier.Operation = when (this) {
    is ComposeModifier.Operation.Width -> ComposeModifier.Operation.Width(valueText.toIntOrNull() ?: 0)
    is ComposeModifier.Operation.Height -> ComposeModifier.Operation.Height(valueText.toIntOrNull() ?: 0)
    is ComposeModifier.Operation.Padding -> ComposeModifier.Operation.Padding(valueText.toIntOrNull() ?: 0)
    is ComposeModifier.Operation.BackgroundColor -> ComposeModifier.Operation.BackgroundColor(valueText)
    else -> this
}
