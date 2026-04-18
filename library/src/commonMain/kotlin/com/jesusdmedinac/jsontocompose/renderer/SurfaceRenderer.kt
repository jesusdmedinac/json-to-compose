package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.modifier.toShape

val SurfaceTonalElevationKey = SemanticsPropertyKey<Dp>("SurfaceTonalElevation")
var SemanticsPropertyReceiver.surfaceTonalElevation by SurfaceTonalElevationKey

val SurfaceShadowElevationKey = SemanticsPropertyKey<Dp>("SurfaceShadowElevation")
var SemanticsPropertyReceiver.surfaceShadowElevation by SurfaceShadowElevationKey

val SurfaceColorKey = SemanticsPropertyKey<Color>("SurfaceColor")
var SemanticsPropertyReceiver.surfaceColor by SurfaceColorKey

@Composable
fun ComposeNode.ToSurface() {
    val props = properties as? NodeProperties.SurfaceProps ?: return
    
    val tonalElevation = props.tonalElevation?.dp ?: 0.dp
    val shadowElevation = props.shadowElevation?.dp ?: 0.dp
    val color = props.color?.toColor() ?: MaterialTheme.colorScheme.surface
    val shape = props.shape?.toShape() ?: RectangleShape

    val modifier = (Modifier from composeModifier)
        .testTag(type.name)
        .semantics {
            this.surfaceTonalElevation = tonalElevation
            this.surfaceShadowElevation = shadowElevation
            this.surfaceColor = color
        }

    Surface(
        modifier = modifier,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        color = color,
        shape = shape,
    ) {
        props.child?.ToCompose()
    }
}
