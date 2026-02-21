package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.DialogProperties
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

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

    val backgroundColor = props.backgroundColor?.let { Color(it) }
        ?: MaterialTheme.colors.surface
    val contentColor = props.contentColor?.let { Color(it) }
        ?: contentColorFor(backgroundColor)

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            visibilityStateHost?.onStateChange(false)
            behavior?.invoke()
        },
        title = {
            props.title?.ToCompose()
        },
        text = {
            props.text?.ToCompose()
        },
        confirmButton = {
            props.confirmButton?.ToCompose()
        },
        dismissButton = {
            props.dismissButton?.ToCompose()
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        // TODO: Support DialogProperties
        properties = DialogProperties(),
        // TODO: Support Shape
        shape = MaterialTheme.shapes.medium,
    )
}
