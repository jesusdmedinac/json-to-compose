package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToHorizontalDivider() {
    val props = properties as? NodeProperties.HorizontalDividerProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val thickness = props.thickness?.dp ?: 1.dp
    val color = props.color?.toColor()

    if (color != null) {
        HorizontalDivider(modifier = modifier, thickness = thickness, color = color)
    } else {
        HorizontalDivider(modifier = modifier, thickness = thickness)
    }
}
