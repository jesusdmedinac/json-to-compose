package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToCircularProgressIndicator() {
    val props = properties as? NodeProperties.ProgressIndicatorProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val (progress, _) = resolveStateHostValue(
        stateHostName = props.progressStateHostName,
        inlineValue = props.progress,
        defaultValue = null,
    )
    val color = props.color.toColor(ProgressIndicatorDefaults.circularColor)
    val trackColor = props.trackColor.toColor(ProgressIndicatorDefaults.circularTrackColor)

    if (progress != null) {
        CircularProgressIndicator(
            progress = progress,
            modifier = modifier,
            color = color,
            trackColor = trackColor
        )
    } else {
        CircularProgressIndicator(
            modifier = modifier,
            color = color,
            trackColor = trackColor
        )
    }
}

@Composable
fun ComposeNode.ToLinearProgressIndicator() {
    val props = properties as? NodeProperties.ProgressIndicatorProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val (progress, _) = resolveStateHostValue(
        stateHostName = props.progressStateHostName,
        inlineValue = props.progress,
        defaultValue = null,
    )
    val color = props.color.toColor(ProgressIndicatorDefaults.linearColor)
    val trackColor = props.trackColor.toColor(ProgressIndicatorDefaults.linearTrackColor)

    if (progress != null) {
        LinearProgressIndicator(
            progress = progress,
            modifier = modifier,
            color = color,
            trackColor = trackColor
        )
    } else {
        LinearProgressIndicator(
            modifier = modifier,
            color = color,
            trackColor = trackColor
        )
    }
}
