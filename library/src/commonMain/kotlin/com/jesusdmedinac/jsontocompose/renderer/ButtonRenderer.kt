package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.RowScope
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
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

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

    val onClick: () -> Unit = {
        behavior?.invoke()
    }
    val buttonContent: @Composable RowScope.() -> Unit = {
        child?.ToCompose()
    }
    when (type) {
        ComposeType.Button -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            content = buttonContent,
        )

        ComposeType.OutlinedButton -> OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            content = buttonContent,
        )

        ComposeType.TextButton -> TextButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            content = buttonContent,
        )

        ComposeType.ElevatedButton -> ElevatedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            content = buttonContent,
        )

        ComposeType.FilledTonalButton -> FilledTonalButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            content = buttonContent,
        )

        else -> {}
    }
}
