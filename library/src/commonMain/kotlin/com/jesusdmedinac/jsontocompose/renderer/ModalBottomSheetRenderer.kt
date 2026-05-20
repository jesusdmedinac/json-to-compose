package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.modifier.toShape
import com.jesusdmedinac.jsontocompose.renderer.toColor
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToModalBottomSheet() {
    val props = properties as? NodeProperties.ModalBottomSheetProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (isVisible, visibilityStateHost) = resolveStateHostValue(
        stateHostName = props.visibleStateHostName,
        inlineValue = props.visible,
        defaultValue = false,
    )

    if (!isVisible) return

    val onDismissRequestEventName = props.onDismissRequestEventName
    val currentBehavior = LocalBehavior.current
    val behavior = currentBehavior[onDismissRequestEventName]

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    // Sync sheet visibility with state
    LaunchedEffect(isVisible) {
        if (isVisible && sheetState.currentValue == SheetValue.Hidden) {
            sheetState.show()
        } else if (!isVisible && sheetState.currentValue != SheetValue.Hidden) {
            sheetState.hide()
        }
    }

    // Update state when sheet is dismissed
    LaunchedEffect(sheetState.currentValue) {
        if (sheetState.currentValue == SheetValue.Hidden && isVisible) {
            visibilityStateHost?.onStateChange(false)
        }
    }

    val scrimColor = props.scrimColor.toColor(BottomSheetDefaults.ScrimColor)
    val shape: Shape = props.shape?.toShape() ?: RectangleShape

    ModalBottomSheet(
        onDismissRequest = {
            visibilityStateHost?.onStateChange(false)
            behavior?.invoke()
            if (behavior == null && onDismissRequestEventName != null) {
                println("Warning: Behavior for event \"$onDismissRequestEventName\" not found in LocalBehavior.")
            }
        },
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        scrimColor = scrimColor,
        dragHandle = if (props.showDragHandle == true) {
            { /* Default drag handle will be shown by Material 3 */ }
        } else {
            props.dragHandle?.let { dragHandleNode ->
                { dragHandleNode.ToCompose() }
            }
        },
    ) {
        props.child?.ToCompose()
    }
}