package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToAlertDialog() {
    val props = properties as? NodeProperties.AlertDialogProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (isVisible, visibilityStateHost) = resolveStateHostValue(
        stateHostName = props.visibilityStateHostName,
        inlineValue = null,
        defaultValue = true,
    )
    if (!isVisible) return

    val onClickEventName = props.onDismissRequestEventName
    val currentBehavior = LocalBehavior.current
    val behavior = currentBehavior[onClickEventName]

    val backgroundColor = props.backgroundColor.toColor(AlertDialogDefaults.containerColor)
    val contentColor = props.contentColor.toColor(AlertDialogDefaults.textContentColor)

    val (tonalElevationVal, _) = resolveStateHostValue(
        stateHostName = props.tonalElevationStateHostName,
        inlineValue = props.tonalElevation,
        defaultValue = 6, // default M3 dialog elevation is 6dp
    )

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            visibilityStateHost?.onStateChange(false)
            if (behavior != null) {
                behavior.invoke()
            } else if (onClickEventName != null) {
                println("Warning: Behavior for event \"$onClickEventName\" not found in LocalBehavior.")
            }
        },
        title = props.title?.let { t -> { t.ToCompose() } },
        text = props.text?.let { t -> { t.ToCompose() } },
        icon = props.icon?.let { i -> { i.ToCompose() } },
        confirmButton = {
            props.confirmButton?.ToCompose()
        },
        dismissButton = props.dismissButton?.let { d -> { d.ToCompose() } },
        containerColor = backgroundColor,
        textContentColor = contentColor,
        tonalElevation = tonalElevationVal.dp,
        properties = DialogProperties()
    )
}
