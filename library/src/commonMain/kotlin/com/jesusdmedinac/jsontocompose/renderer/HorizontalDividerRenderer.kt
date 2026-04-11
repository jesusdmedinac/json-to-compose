package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

val HorizontalDividerThicknessKey = SemanticsPropertyKey<Dp>("HorizontalDividerThickness")
var SemanticsPropertyReceiver.horizontalDividerThickness by HorizontalDividerThicknessKey

val HorizontalDividerColorKey = SemanticsPropertyKey<Color>("HorizontalDividerColor")
var SemanticsPropertyReceiver.horizontalDividerColor by HorizontalDividerColorKey

@Composable
fun ComposeNode.ToHorizontalDivider() {
    val props = properties as? NodeProperties.DividerProps
    
    val thickness = props?.thickness?.dp ?: DividerDefaults.Thickness
    val color = props?.color?.toColor() ?: DividerDefaults.color

    val modifier = (Modifier from composeModifier)
        .testTag(type.name)
        .semantics {
            this.horizontalDividerThickness = thickness
            this.horizontalDividerColor = color
        }

    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}
