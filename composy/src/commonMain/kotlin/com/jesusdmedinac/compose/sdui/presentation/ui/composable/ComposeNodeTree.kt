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
import com.composables.icons.lucide.Lucide
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeBehavior
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeState
import com.jesusdmedinac.jsontocompose.ComposeNode


@Composable
fun ComposeNodeTree(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    val composeTreeScreenModel: ComposeTreeScreenModel = navigator.koinNavigatorScreenModel()
    val state by composeTreeScreenModel.state.collectAsState()
    val composeNodeRoot = state.composeNodeRoot
    composeNodeRoot.ToComposeTree(
        modifier = modifier,
        state = state,
        behavior = composeTreeScreenModel,
    )
}

@Composable
private fun ComposeNode.ToComposeTree(
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
private fun ComposeNode.ComposeTreeItem(
    state: ComposeTreeState = ComposeTreeState(),
    behavior: ComposeTreeBehavior = ComposeTreeBehavior.Default,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isHovered by remember { mutableStateOf(false) }
    val isParentExpanded = state.isParentExpanded(this)
    AnimatedVisibility(
        isParentExpanded,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
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

                        state.isSelected(this@ComposeTreeItem) -> {
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
                    state.isSelected(this@ComposeTreeItem) -> MaterialTheme.colorScheme.onPrimary
                    else -> MaterialTheme.colorScheme.onBackground
                },
            )
            Spacer(modifier = Modifier.width(64.dp))
            if (type.isLayout() || type.hasChild()) {
                IconButton(
                    onClick = {
                        behavior.onNodeExpanded(this@ComposeTreeItem)
                    },
                    modifier = Modifier
                        .pointerHoverIcon(
                            icon = PointerIcon.Hand
                        )
                ) {
                    val expanded = state.collapsedNodes.contains(this@ComposeTreeItem)
                    Icon(
                        Lucide.ArrowDown,
                        contentDescription = "Trailing icon for exposed dropdown menu",
                        modifier = Modifier.rotate(if (expanded) 180f else 360f),
                        tint = when {
                            isHovered -> MaterialTheme.colorScheme.background
                            state.isSelected(this@ComposeTreeItem) -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onBackground
                        }
                    )
                }
            }
        }
    }
}