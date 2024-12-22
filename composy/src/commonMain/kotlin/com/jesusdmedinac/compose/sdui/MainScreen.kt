package com.jesusdmedinac.compose.sdui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.kotlin.fibonacci.ComposeNode
import io.github.kotlin.fibonacci.ToCompose
import json_to_compose.composy.generated.resources.Res
import json_to_compose.composy.generated.resources.ic_menu
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
data object MainScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<MainScreenModel>()
        val composeEditorScreenModel = koinScreenModel<ComposeEditorScreenModel>()
        val editNodeScreenModel = koinScreenModel<EditNodeScreenModel>()
        val state by screenModel.state.collectAsState()
        val isLeftPanelDisplayed = state.isLeftPanelDisplayed
        val composeEditorState by composeEditorScreenModel.state.collectAsState()
        val composeNodeRoot = composeEditorState.composeNodeRoot
        val composeEditorSideEffect by composeEditorScreenModel.sideEffect.collectAsState()
        val editNodeSideEffect by editNodeScreenModel.sideEffect.collectAsState()

        LaunchedEffect(composeEditorSideEffect) {
            when (val sideEffect = composeEditorSideEffect) {
                ComposeEditorSideEffect.Idle -> Unit
                is ComposeEditorSideEffect.DisplayEditNodeDialog -> {
                    navigator.push(EditNodeScreen(sideEffect.composeNode))
                }
            }
        }

        LaunchedEffect(editNodeSideEffect) {
            when (val sideEffect = editNodeSideEffect) {
                EditNodeSideEffect.Idle -> Unit
                is EditNodeSideEffect.SaveNode -> {
                    composeEditorScreenModel.saveNode(sideEffect.composeNode)
                }
            }
        }

        MaterialTheme {
            val leftSideWidth = 384
            val draggableWidth = 4
            val max = (leftSideWidth - draggableWidth).dp
            val min = 0.dp
            val (minPx, maxPx) = with(LocalDensity.current) { min.toPx() to max.toPx() }
            val offsetPositionFromDp = with(LocalDensity.current) { max.toPx() }
            var offsetPosition by remember { mutableStateOf(offsetPositionFromDp) }

            LaunchedEffect(isLeftPanelDisplayed) {
                when {
                    isLeftPanelDisplayed && offsetPosition < maxPx -> {
                        val start = if (offsetPosition == minPx) minPx
                        else offsetPosition
                        for (i in start.roundToInt() .. maxPx.roundToInt()) {
                            offsetPosition = i.toFloat()
                            if (i % 8 == 0) {
                                delay(1)
                            }
                        }
                    }
                    !isLeftPanelDisplayed && offsetPosition > minPx -> {
                        val start = if (offsetPosition == maxPx) maxPx
                        else offsetPosition
                        for (i in start.roundToInt()downTo minPx.roundToInt()) {
                            offsetPosition = i.toFloat()
                            if (i % 8 == 0) {
                                delay(1)
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            val newValue = offsetPosition + delta
                            offsetPosition = newValue.coerceIn(minPx, maxPx)
                        }
                    )
            ) {
                ComposeNodeTree(
                    composeNodeRoot,
                    composeEditorScreenModel,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(leftSideWidth.dp)
                        .background(Color(0xFF2C2C2C))
                )
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val leftSideSpaceWidth =
                        with(LocalDensity.current) { (offsetPosition).toDp() }
                    Spacer(modifier = Modifier.width(leftSideSpaceWidth))
                    ComposePreview(
                        composeNodeRoot,
                        onMenuClick = {
                            screenModel.onDisplayLeftPanelClick()
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(3f)
                            .background(Color(0xFF1E1E1E))
                    )
                }
                Box(
                    modifier = Modifier
                        .offset { IntOffset(offsetPosition.roundToInt(), 0) }
                        .fillMaxHeight()
                        .width(4.dp)
                        .background(Color(0xFF1E1E1E))
                ) { }
            }
        }
    }
}

@Composable
fun ComposePreview(
    composeNodeRoot: ComposeNode,
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        IconButton(onClick = {
            onMenuClick()
        }) {
            Icon(
                painterResource(Res.drawable.ic_menu),
                contentDescription = null,
                tint = Color.White
            )
        }
        composeNodeRoot.ToCompose(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        )
    }
}