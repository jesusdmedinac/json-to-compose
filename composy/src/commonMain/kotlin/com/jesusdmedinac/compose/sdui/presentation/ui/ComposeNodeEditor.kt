package com.jesusdmedinac.compose.sdui.presentation.ui

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeBehavior
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenState
import io.github.kotlin.fibonacci.ComposeType

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
                        editNodeBehavior.onSaveNodeClick(editingComposeNode!!)
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
        item {
            ComposeTypeDropdownMenu(
                editNodeState = editNodeState,
                editNodeBehavior = editNodeBehavior
            )
        }
        item {
            ComposeTextTextField(
                editNodeState = editNodeState,
                editNodeBehavior = editNodeBehavior
            )
        }
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
@Composable
fun ComposeTypeDropdownMenu(
    editNodeState: EditNodeScreenState,
    editNodeBehavior: EditNodeBehavior,
) {
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
                label = { Text("Compose type", color = MaterialTheme.colorScheme.onBackground) },
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComposeTextTextField(
    editNodeState: EditNodeScreenState,
    editNodeBehavior: EditNodeBehavior,
) {
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
                label = { Text("Text", color = MaterialTheme.colorScheme.onBackground) },
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