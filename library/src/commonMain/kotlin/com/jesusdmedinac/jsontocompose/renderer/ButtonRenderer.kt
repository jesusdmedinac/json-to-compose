package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.modifier.toShape
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
        if (behavior != null) {
            behavior.invoke()
        } else if (onClickEventName != null) {
            println("Warning: Behavior for event \"$onClickEventName\" not found in LocalBehavior.")
        }
    }
    val buttonContent: @Composable RowScope.() -> Unit = {
        child?.ToCompose()
    }

    val shape = props.shape?.toShape()
    val containerColor = props.containerColor?.toColor()
    val contentColor = props.contentColor?.toColor()

    when (type) {
        ComposeType.Button -> {
            val colors = if (containerColor != null || contentColor != null) {
                ButtonDefaults.buttonColors(
                    containerColor = containerColor ?: androidx.compose.ui.graphics.Color.Unspecified,
                    contentColor = contentColor ?: androidx.compose.ui.graphics.Color.Unspecified
                )
            } else ButtonDefaults.buttonColors()

            Button(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape ?: ButtonDefaults.shape,
                colors = colors,
                content = buttonContent,
            )
        }

        ComposeType.OutlinedButton -> {
            val colors = if (containerColor != null || contentColor != null) {
                ButtonDefaults.outlinedButtonColors(
                    containerColor = containerColor ?: androidx.compose.ui.graphics.Color.Transparent,
                    contentColor = contentColor ?: androidx.compose.ui.graphics.Color.Unspecified
                )
            } else ButtonDefaults.outlinedButtonColors()

            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape ?: ButtonDefaults.outlinedShape,
                colors = colors,
                content = buttonContent,
            )
        }

        ComposeType.TextButton -> {
            val colors = if (containerColor != null || contentColor != null) {
                ButtonDefaults.textButtonColors(
                    containerColor = containerColor ?: androidx.compose.ui.graphics.Color.Transparent,
                    contentColor = contentColor ?: androidx.compose.ui.graphics.Color.Unspecified
                )
            } else ButtonDefaults.textButtonColors()

            TextButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape ?: ButtonDefaults.textShape,
                colors = colors,
                content = buttonContent,
            )
        }

        ComposeType.ElevatedButton -> {
            val colors = if (containerColor != null || contentColor != null) {
                ButtonDefaults.elevatedButtonColors(
                    containerColor = containerColor ?: androidx.compose.ui.graphics.Color.Unspecified,
                    contentColor = contentColor ?: androidx.compose.ui.graphics.Color.Unspecified
                )
            } else ButtonDefaults.elevatedButtonColors()

            ElevatedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape ?: ButtonDefaults.elevatedShape,
                colors = colors,
                content = buttonContent,
            )
        }

        ComposeType.FilledTonalButton -> {
            val colors = if (containerColor != null || contentColor != null) {
                ButtonDefaults.filledTonalButtonColors(
                    containerColor = containerColor ?: androidx.compose.ui.graphics.Color.Unspecified,
                    contentColor = contentColor ?: androidx.compose.ui.graphics.Color.Unspecified
                )
            } else ButtonDefaults.filledTonalButtonColors()

            FilledTonalButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape ?: ButtonDefaults.filledTonalShape,
                colors = colors,
                content = buttonContent,
            )
        }

        else -> {}
    }
}
