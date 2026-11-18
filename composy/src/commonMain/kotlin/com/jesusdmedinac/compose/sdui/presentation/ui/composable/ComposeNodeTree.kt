package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.composables.icons.lucide.ArrowDown
import com.composables.icons.lucide.Delete
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditorIntent
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditorScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditorState
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType

@Composable
fun ComposeNodeTree(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    val editorScreenModel: EditorScreenModel = navigator.koinNavigatorScreenModel()
    val state by editorScreenModel.state.collectAsState()
    val rootNode = state.rootNode
    rootNode.ToComposeTree(
        modifier = modifier,
        state = state,
        onIntent = { intent -> editorScreenModel.onIntent(intent) },
    )
}

@Composable
private fun ComposeNode.ToComposeTree(
    modifier: Modifier = Modifier,
    state: EditorState,
    onIntent: (EditorIntent) -> Unit,
) {
    val horizontalScrollState = rememberScrollState()
    val collapsedNodeIds = remember { mutableStateListOf<String>() }

    LazyColumn(
        modifier
            .fillMaxSize()
            .horizontalScroll(horizontalScrollState),
    ) {
        items(this@ToComposeTree.asList()) { node ->
            node.ComposeTreeItem(
                state = state,
                isCollapsed = collapsedNodeIds.contains(node.id),
                isParentExpanded = node.parents().none { parent -> collapsedNodeIds.contains(parent.id) },
                onToggleCollapse = {
                    if (collapsedNodeIds.contains(node.id)) {
                        collapsedNodeIds.remove(node.id)
                    } else {
                        collapsedNodeIds.add(node.id)
                    }
                },
                onIntent = onIntent,
                modifier = Modifier.height(40.dp)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ComposeNode.ComposeTreeItem(
    state: EditorState,
    isCollapsed: Boolean,
    isParentExpanded: Boolean,
    onToggleCollapse: () -> Unit,
    onIntent: (EditorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isHovered by remember { mutableStateOf(false) }
    val isSelected = state.selectedNodeId == this.id
    val isRoot = state.rootNode.id == this.id

    AnimatedVisibility(
        isParentExpanded,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    onIntent(EditorIntent.SelectNode(this@ComposeTreeItem.id))
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
                    onEvent = { isHovered = true }
                )
                .onPointerEvent(
                    PointerEventType.Exit,
                    onEvent = { isHovered = false }
                )
                .then(
                    when {
                        isHovered -> Modifier.background(MaterialTheme.colorScheme.onBackground)
                        isSelected -> Modifier.background(MaterialTheme.colorScheme.primary)
                        else -> Modifier
                    }
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            if (parent != null) {
                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = (16 * countLevels()).dp)
                        .background(Color.White)
                )
            }
            Text(
                text = type.name,
                color = when {
                    isHovered -> MaterialTheme.colorScheme.background
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    else -> MaterialTheme.colorScheme.onBackground
                },
            )
            Spacer(modifier = Modifier.width(16.dp))

            if (isHovered || isSelected) {
                if (!isRoot) {
                    IconButton(
                        onClick = {
                            onIntent(EditorIntent.ReorderNode(this@ComposeTreeItem.id, EditorIntent.ReorderNode.Direction.UP))
                        },
                        modifier = Modifier.size(24.dp).pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(
                            Lucide.ArrowDown,
                            contentDescription = "Reorder Up",
                            modifier = Modifier.rotate(180f).size(16.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.background
                        )
                    }
                    IconButton(
                        onClick = {
                            onIntent(EditorIntent.ReorderNode(this@ComposeTreeItem.id, EditorIntent.ReorderNode.Direction.DOWN))
                        },
                        modifier = Modifier.size(24.dp).pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(
                            Lucide.ArrowDown,
                            contentDescription = "Reorder Down",
                            modifier = Modifier.size(16.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.background
                        )
                    }
                }

                if (type.isLayout() || type.hasChild()) {
                    IconButton(
                        onClick = {
                            onIntent(EditorIntent.AddNode(parentId = this@ComposeTreeItem.id, type = ComposeType.Text))
                        },
                        modifier = Modifier.size(24.dp).pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(
                            Lucide.Plus,
                            contentDescription = "Add Child Node",
                            modifier = Modifier.size(16.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.background
                        )
                    }
                }

                if (!isRoot) {
                    IconButton(
                        onClick = {
                            onIntent(EditorIntent.DeleteNode(this@ComposeTreeItem.id))
                        },
                        modifier = Modifier.size(24.dp).pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(
                            Lucide.Delete,
                            contentDescription = "Delete Node",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))

            if (type.isLayout() || type.hasChild()) {
                IconButton(
                    onClick = onToggleCollapse,
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Icon(
                        Lucide.ArrowDown,
                        contentDescription = "Toggle Expand/Collapse",
                        modifier = Modifier.rotate(if (isCollapsed) 180f else 360f),
                        tint = when {
                            isHovered -> MaterialTheme.colorScheme.background
                            isSelected -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onBackground
                        }
                    )
                }
            }
        }
    }
}
