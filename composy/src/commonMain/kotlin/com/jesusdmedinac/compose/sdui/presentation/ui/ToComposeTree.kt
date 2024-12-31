package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeState
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ComposeType

@Composable
fun ComposeNode.ToComposeTree(
    modifier: Modifier = Modifier,
    state: ComposeTreeState = ComposeTreeState(),
    behavior: ComposeTreeBehavior = ComposeTreeBehavior.Default,
) {
    val horizontalScrollState = rememberScrollState()
    LazyColumn(
        modifier
            .fillMaxSize()
            .horizontalScroll(horizontalScrollState),
    ) {
        items(this@ToComposeTree.asList()) {
            it.ComposeTreeItem(
                state = state,
                behavior = behavior,
                modifier = Modifier.height(40.dp)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComposeNode.ComposeTreeItem(
    state: ComposeTreeState = ComposeTreeState(),
    behavior: ComposeTreeBehavior = ComposeTreeBehavior.Default,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isHovered by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                behavior.onComposeNodeSelected(this@ComposeTreeItem)
            }
            .hoverable(
                interactionSource = interactionSource,
                enabled = true,
            )
            .pointerHoverIcon(
                icon = PointerIcon.Hand
            )
            .onPointerEvent(
                PointerEventType.Enter,
                onEvent = {
                    isHovered = true
                }
            )
            .onPointerEvent(
                PointerEventType.Exit,
                onEvent = {
                    isHovered = false
                }
            )
            .then(
                when {
                    isHovered -> {
                        Modifier
                            .background(MaterialTheme.colorScheme.onBackground)
                    }
                    state.selectedComposeNode == this@ComposeTreeItem -> {
                        Modifier
                            .background(MaterialTheme.colorScheme.primary)
                    }
                    else -> {
                        Modifier
                    }
                }
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (parent != null) {
            Divider(
                modifier = Modifier
                    //.padding(horizontal = (16 * countLevels()).dp)
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color.White)
            )
        }
        Text(
            text = type.name,
            color = when {
                isHovered -> MaterialTheme.colorScheme.background
                state.selectedComposeNode == this@ComposeTreeItem -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onBackground
            },
        )
        Spacer(modifier = Modifier.width(64.dp))
        // TODO Display when hide children implemented
        if (false) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .pointerHoverIcon(
                        icon = PointerIcon.Hand
                    )
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
}

interface ComposeTreeBehavior {
    fun onAddNewNodeToChildren(composeNode: ComposeNode)
    fun onAddNewNodeAsChild(composeNode: ComposeNode)
    fun onComposeNodeSelected(composeNode: ComposeNode?)
    fun saveNode(composeNode: ComposeNode)

    companion object {
        val Default = object : ComposeTreeBehavior {
            override fun onAddNewNodeToChildren(composeNode: ComposeNode) {
                TODO("onAddNewNodeToChildren is not implemented")
            }

            override fun onAddNewNodeAsChild(composeNode: ComposeNode) {
                TODO("onAddNewNodeAsChild is not implemented")
            }

            override fun onComposeNodeSelected(composeNode: ComposeNode?) {
                TODO("onComposeNodeSelected is not implemented")
            }

            override fun saveNode(composeNode: ComposeNode) {
                TODO("Not yet implemented")
            }
        }
    }
}