package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

private data class ResolvedProgressProps(
    val progress: Float?,
    val color: Color,
    val trackColor: Color
)

@Composable
private fun resolveProgressProps(
    props: NodeProperties.ProgressIndicatorProps,
    defaultColor: Color,
    defaultTrackColor: Color
): ResolvedProgressProps {
    val (progress, _) = resolveStateHostValue(
        stateHostName = props.progressStateHostName,
        inlineValue = props.progress,
        defaultValue = null,
    )
    val color = props.color.toColor(defaultColor)
    val trackColor = props.trackColor.toColor(defaultTrackColor)
    return ResolvedProgressProps(progress, color, trackColor)
}

@Composable
fun ComposeNode.ToCircularProgressIndicator() {
    val props = properties as? NodeProperties.ProgressIndicatorProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val (progress, color, trackColor) = resolveProgressProps(
        props = props,
        defaultColor = ProgressIndicatorDefaults.circularColor,
        defaultTrackColor = ProgressIndicatorDefaults.circularTrackColor
    )

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
    val (progress, color, trackColor) = resolveProgressProps(
        props = props,
        defaultColor = ProgressIndicatorDefaults.linearColor,
        defaultTrackColor = ProgressIndicatorDefaults.linearTrackColor
    )

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
