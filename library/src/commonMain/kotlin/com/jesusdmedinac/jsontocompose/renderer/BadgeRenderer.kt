package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToBadge() {
    val props = properties as? NodeProperties.BadgeProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val (text, _) = resolveStateHostValue(
        stateHostName = props.textStateHostName,
        inlineValue = props.text,
        defaultValue = null,
    )
    val containerColor = props.containerColor.toColor(BadgeDefaults.containerColor)
    val contentColor = props.contentColor.toColor(Color.Unspecified)

    Badge(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        if (!text.isNullOrEmpty()) {
            Text(text = text)
        }
    }
}
