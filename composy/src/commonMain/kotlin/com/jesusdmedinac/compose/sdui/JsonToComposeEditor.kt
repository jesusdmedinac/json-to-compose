package com.jesusdmedinac.compose.sdui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ComposeType

@Composable
fun ComposeNode.ToComposeEditor(
    modifier: Modifier = Modifier,
    state: ComposeEditorState = ComposeEditorState(),
    behavior: ComposeEditorBehavior = ComposeEditorBehavior.Default,
) {
    when (type) {
        ComposeType.Column, ComposeType.Row, ComposeType.Box -> ToLayoutEditor(
            modifier,
            state,
            behavior,
        )

        ComposeType.Text -> ToTextEditor(
            modifier,
            behavior,
        )

        ComposeType.Button -> ToButtonEditor(modifier)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComposeNode.ToComposeEditorListItem(
    modifier: Modifier = Modifier,
    behavior: ComposeEditorBehavior = ComposeEditorBehavior.Default,
) {
    ListItem(
        modifier = modifier,
        icon = {
            IconButton(onClick = {
                behavior.onEditNodeClick(this)
            }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        trailing = {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        text = {
            Text(
                text = type.name,
                color = Color.White
            )
        },
    )
}

@Composable
fun ComposeNode.ToLayoutEditor(
    modifier: Modifier = Modifier,
    state: ComposeEditorState = ComposeEditorState(),
    behavior: ComposeEditorBehavior = ComposeEditorBehavior.Default,
) {
    if (type != ComposeType.Column && type != ComposeType.Row && type != ComposeType.Box)
        throw IllegalArgumentException("Type is not a layout")
    LazyColumn(
        modifier = modifier
    ) {
        item {
            ToComposeEditorListItem(
                behavior = behavior
            )
        }
        items(children ?: emptyList()) {
            it.ToComposeEditor(
                behavior = behavior
            )
        }
        item {
            Box {
                TextButton(
                    onClick = {
                        behavior.onAddNewNodeClick()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                    )
                    Text("Add node into ${type.name}")
                }
                DropdownMenu(
                    expanded = state.isAddNewNodeMenuDisplayed,
                    onDismissRequest = {
                        behavior.onAddNewNodeMenuDismiss()
                    }) {
                    ComposeType.entries.forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                behavior.onAddNewNode(
                                    ComposeNode(
                                        type,
                                        text = "New ${type.name}",
                                        child = null,
                                        children = emptyList(),
                                        onClickEventName = "onClick_${type.name}",
                                    )
                                )
                            }
                        ) {
                            Text(type.name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComposeNode.ToColumnEditor(
    modifier: Modifier = Modifier,
) {
    TODO("Not yet implemented")
}

@Composable
fun ComposeNode.ToRowEditor(
    modifier: Modifier = Modifier,
) {
    TODO("Not yet implemented")
}

@Composable
fun ComposeNode.ToBoxEditor(
    modifier: Modifier = Modifier,
) {
    TODO("Not yet implemented")
}

@Composable
fun ComposeNode.ToTextEditor(
    modifier: Modifier = Modifier,
    behavior: ComposeEditorBehavior = ComposeEditorBehavior.Default,
) {
    ToComposeEditorListItem(
        modifier = modifier,
        behavior = behavior
    )
}

@Composable
fun ComposeNode.ToButtonEditor(
    modifier: Modifier = Modifier,
) {
    TODO("Not yet implemented")
}

interface ComposeEditorBehavior {
    fun onAddNewNodeClick()
    fun onAddNewNodeMenuDismiss()
    fun onAddNewNode(composeNode: ComposeNode)
    fun onEditNodeClick(composeNode: ComposeNode)
    fun saveNode(composeNode: ComposeNode)

    companion object {
        val Default = object : ComposeEditorBehavior {
            override fun onAddNewNodeClick() {
                TODO("onAddNewNodeClick is not implemented")
            }

            override fun onAddNewNodeMenuDismiss() {
                TODO("onAddNewNodeMenuDismiss is not implemented")
            }

            override fun onAddNewNode(composeNode: ComposeNode) {
                TODO("onAddNewNode is not implemented")
            }

            override fun onEditNodeClick(composeNode: ComposeNode) {
                TODO("onEditNodeClick is not implemented")
            }

            override fun saveNode(composeNode: ComposeNode) {
                TODO("Not yet implemented")
            }
        }
    }
}