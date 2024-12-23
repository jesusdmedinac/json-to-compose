package com.jesusdmedinac.compose.sdui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun WindowWithLeftPanel(
    isLeftPanelDisplayed: Boolean,
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
    var offsetPosition by remember { mutableStateOf(offsetPositionFromDp) }

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
            }
        }
    }

    Box(
        modifier = modifier
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val newValue = offsetPosition + delta
                    offsetPosition = newValue.coerceIn(minPx, maxPx)
                }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(leftSideWidth.dp)
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
                    .background(Color(0xFF1E1E1E))
            ) {
                content()
            }
        }
        DraggableVerticalBar(offsetPosition)
    }
}

@Composable
private fun DraggableVerticalBar(offsetPosition: Float) {
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetPosition.roundToInt(), 0) }
            .fillMaxHeight()
            .width(4.dp)
            .background(Color(0xFF1E1E1E))
    ) { }
}