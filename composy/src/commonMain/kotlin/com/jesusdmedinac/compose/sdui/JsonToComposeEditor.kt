package com.jesusdmedinac.compose.sdui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ComposeType
import kotlin.math.roundToInt

@Composable
fun ComposeNode.ToComposeEditor(
    modifier: Modifier = Modifier,
    state: ComposeEditorState = ComposeEditorState(),
    behavior: ComposeEditorBehavior = ComposeEditorBehavior.Default,
) {
    val horizontalScrollState = rememberScrollState()
    LazyColumn(
        modifier
            .horizontalScroll(horizontalScrollState)
    ) {
        items(this@ToComposeEditor.asList()) {
            it.ComposeEditorItem(
                state = state,
                behavior = behavior,
            )
        }
    }
}

@Composable
fun ComposeNode.ComposeEditorItem(
    state: ComposeEditorState = ComposeEditorState(),
    behavior: ComposeEditorBehavior = ComposeEditorBehavior.Default,
) {
    Row(
        modifier = Modifier
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (parent != null) {
            Divider(
                modifier = Modifier
                    .padding(horizontal = (16 * countLevels()).dp)
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color.White)
            )
        }
        Text(
            text = type.name,
            color = Color.White,
        )
        Text(
            text = id,
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (type == ComposeType.Column || type == ComposeType.Row || type == ComposeType.Box) {
            var expanded by remember { mutableStateOf(false) }
            Box {
                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add child",
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                ) {
                    ComposeType.entries.forEach {
                        DropdownMenuItem(
                            onClick = {
                                behavior.onAddNewNode(
                                    ComposeNode(
                                        type = it,
                                        parent = this@ComposeEditorItem,
                                    )
                                )
                                expanded = false
                            }
                        ) {
                            Text(text = it.name)
                        }
                    }
                }
            }
        }
        IconButton(
            onClick = {}
        ) {
            val expanded = true
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = "Trailing icon for exposed dropdown menu",
                modifier = Modifier.rotate(if (expanded) 180f else 360f),
                tint = Color.White
            )
        }
    }
}

interface ComposeEditorBehavior {
    fun onAddNewNode(composeNode: ComposeNode)
    fun onEditNodeClick(composeNode: ComposeNode)
    fun saveNode(composeNode: ComposeNode)

    companion object {
        val Default = object : ComposeEditorBehavior {
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