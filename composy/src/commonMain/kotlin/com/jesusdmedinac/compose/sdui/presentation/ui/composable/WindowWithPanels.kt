package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun WindowWithPanels(
    isLeftPanelDisplayed: Boolean,
    onLeftPanelClosed: () -> Unit,
    isRightPanelDisplayed: Boolean,
    onRightPanelClosed: () -> Unit,
    leftPanelContent: @Composable () -> Unit,
    rightPanelContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    RightPanel(
        isRightPanelDisplayed = isRightPanelDisplayed,
        onRightPanelClosed = onRightPanelClosed,
        rightPanelContent = rightPanelContent,
        modifier = modifier
    ) {
        LeftPanel(
            isLeftPanelDisplayed = isLeftPanelDisplayed,
            onLeftPanelClosed = onLeftPanelClosed,
            leftPanelContent = leftPanelContent,
            modifier = modifier,
            content = content
        )
    }
}

@Composable
private fun RightPanel(
    isRightPanelDisplayed: Boolean,
    onRightPanelClosed: () -> Unit,
    rightPanelContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val rightSideWidth = 384
    val draggableWidth = 4
    val max = (rightSideWidth - draggableWidth).dp
    val min = 0.dp
    val (minPx, maxPx) = with(LocalDensity.current) { min.toPx() to max.toPx() }
    val offsetPositionFromDp = with(LocalDensity.current) { max.toPx() }
    var offsetPosition by remember {
        mutableStateOf(
            if (isRightPanelDisplayed) offsetPositionFromDp
            else minPx
        )
    }

    LaunchedEffect(isRightPanelDisplayed) {
        when {
            isRightPanelDisplayed && offsetPosition < maxPx -> {
                val start = if (offsetPosition == minPx) minPx
                else offsetPosition
                for (i in start.roundToInt()..maxPx.roundToInt()) {
                    offsetPosition = i.toFloat()
                    if (i % 8 == 0) {
                        delay(1)
                    }
                }
            }

            !isRightPanelDisplayed && offsetPosition > minPx -> {
                val start = if (offsetPosition == maxPx) maxPx
                else offsetPosition
                for (i in start.roundToInt() downTo minPx.roundToInt()) {
                    offsetPosition = i.toFloat()
                    if (i % 8 == 0) {
                        delay(1)
                    }
                }
                onRightPanelClosed()
            }
        }
    }

    Box(
        modifier = modifier
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val newValue = if (offsetPosition <= minPx + 64) minPx
                    else offsetPosition - delta
                    offsetPosition = newValue.coerceIn(minPx, maxPx)
                    if (offsetPosition == minPx) {
                        onRightPanelClosed()
                    }
                }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width((rightSideWidth - draggableWidth).dp)
                .align(Alignment.CenterEnd),
        ) {
            rightPanelContent()
        }
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(3f)
            ) {
                content()
            }
            val rightSideSpaceWidth =
                with(LocalDensity.current) { (offsetPosition).toDp() }
            Spacer(modifier = Modifier.width(rightSideSpaceWidth))
        }
    }
}

@Composable
private fun LeftPanel(
    isLeftPanelDisplayed: Boolean,
    onLeftPanelClosed: () -> Unit,
    leftPanelContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val leftSideWidth = 384
    val draggableWidth = 4
    val max = (leftSideWidth - draggableWidth).dp
    val min = 0.dp
    val (minPx, maxPx) = with(LocalDensity.current) { min.toPx() to max.toPx() }
    val offsetPositionFromDp = with(LocalDensity.current) { max.toPx() }
    var offsetPosition by remember {
        mutableStateOf(
            if (isLeftPanelDisplayed) offsetPositionFromDp
            else minPx
        )
    }

    LaunchedEffect(isLeftPanelDisplayed) {
        when {
            isLeftPanelDisplayed && offsetPosition < maxPx -> {
                val start = if (offsetPosition == minPx) minPx
                else offsetPosition
                for (i in start.roundToInt()..maxPx.roundToInt()) {
                    offsetPosition = i.toFloat()
                    if (i % 8 == 0) {
                        delay(1)
                    }
                }
            }

            !isLeftPanelDisplayed && offsetPosition > minPx -> {
                val start = if (offsetPosition == maxPx) maxPx
                else offsetPosition
                for (i in start.roundToInt() downTo minPx.roundToInt()) {
                    offsetPosition = i.toFloat()
                    if (i % 8 == 0) {
                        delay(1)
                    }
                }
                onLeftPanelClosed()
            }
        }
    }

    Box(
        modifier = modifier
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val newValue = if (offsetPosition <= minPx + 64) minPx
                    else offsetPosition + delta
                    offsetPosition = newValue.coerceIn(minPx, maxPx)
                    if (offsetPosition == minPx) {
                        onLeftPanelClosed()
                    }
                }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width((leftSideWidth - draggableWidth).dp)
        ) {
            leftPanelContent()
        }
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            val leftSideSpaceWidth =
                with(LocalDensity.current) { (offsetPosition).toDp() }
            Spacer(modifier = Modifier.width(leftSideSpaceWidth))
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(3f)
            ) {
                content()
            }
        }
    }
}