package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeShape
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.modifier.toShape
import kotlinx.serialization.json.Json

@Composable
fun ComposeNode.ToSurface() {
    val props = properties as? NodeProperties.SurfaceProps ?: return
    val child = props.child
    val tonalElevation = props.tonalElevation?.dp ?: 0.dp

    val shape = try {
        props.shape?.let { Json.decodeFromString<ComposeShape>(it).toShape() } ?: RectangleShape
    } catch (e: Exception) {
        RectangleShape
    }

    val color = props.color?.toColor()
    val modifier = (Modifier from composeModifier).testTag(type.name)

    if (color != null) {
        Surface(
            modifier = modifier,
            tonalElevation = tonalElevation,
            shape = shape,
            color = color,
        ) {
            child?.ToCompose()
        }
    } else {
        Surface(
            modifier = modifier,
            tonalElevation = tonalElevation,
            shape = shape,
        ) {
            child?.ToCompose()
        }
    }
}
