package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToButton() {
    val props = properties as? NodeProperties.ButtonProps ?: return
    val child = props.child
    val onClickEventName = props.onClickEventName
    val currentBehavior = LocalBehavior.current
    val behavior = currentBehavior[onClickEventName]

    val (enabled, _) = resolveStateHostValue(
        stateHostName = props.enabledStateHostName,
        inlineValue = props.enabled,
        defaultValue = true,
    )

    val modifier = (Modifier from composeModifier).testTag(type.name)

    when (type) {
        ComposeType.Button -> Button(
            onClick = {
                behavior?.invoke()
            },
            modifier = modifier,
            enabled = enabled,
        ) {
            child?.ToCompose()
        }

        ComposeType.OutlinedButton -> OutlinedButton(
            onClick = {
                behavior?.invoke()
            },
            modifier = modifier,
            enabled = enabled,
        ) {
            child?.ToCompose()
        }

        ComposeType.TextButton -> TextButton(
            onClick = {
                behavior?.invoke()
            },
            modifier = modifier,
            enabled = enabled,
        ) {
            child?.ToCompose()
        }

        ComposeType.ElevatedButton -> ElevatedButton(
            onClick = {
                behavior?.invoke()
            },
            modifier = modifier,
            enabled = enabled,
        ) {
            child?.ToCompose()
        }

        ComposeType.FilledTonalButton -> FilledTonalButton(
            onClick = {
                behavior?.invoke()
            },
            modifier = modifier,
            enabled = enabled,
        ) {
            child?.ToCompose()
        }

        else -> {}
    }
}
