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
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToAlertDialog() {
    val props = properties as? NodeProperties.AlertDialogProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val currentStateHost = LocalStateHost.current
    val visibilityStateHost = props.visibilityStateHostName?.let { name ->
        val rawStateHost = currentStateHost[name]
        if (rawStateHost == null) {
            println("Warning: No StateHost registered with name \"$name\". Expected StateHost<Boolean>. Dialog will default to visible.")
            return@let null
        }
        @Suppress("UNCHECKED_CAST")
        val typed = rawStateHost as? StateHost<Boolean>
        if (typed == null) {
            println("Warning: StateHost \"$name\" is not of type StateHost<Boolean>. Check the registered type. Dialog will default to visible.")
            return@let null
        }
        // Verify actual type at runtime (type erasure makes the cast above always succeed)
        try {
            typed.state as? Boolean ?: run {
                println("Warning: StateHost \"$name\" is not of type StateHost<Boolean>. Check the registered type. Dialog will default to visible.")
                return@let null
            }
            typed
        } catch (e: ClassCastException) {
            println("Warning: StateHost \"$name\" is not of type StateHost<Boolean>. Check the registered type. Dialog will default to visible.")
            null
        }
    }

    val isVisible = visibilityStateHost?.state ?: true
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
