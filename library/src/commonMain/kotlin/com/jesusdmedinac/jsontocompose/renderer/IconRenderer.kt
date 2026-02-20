package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalDrawableResources
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.resolveStateHostValue
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import org.jetbrains.compose.resources.painterResource

@Composable
fun ComposeNode.ToIcon() {
    val props = properties as? NodeProperties.IconProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val drawableResources = LocalDrawableResources.current

    val (iconName, _) = resolveStateHostValue(
        stateHostName = props.iconNameStateHostName,
        inlineValue = props.iconName,
        defaultValue = null,
    )
    val (tint, _) = resolveStateHostValue(
        stateHostName = props.tintStateHostName,
        inlineValue = props.tint,
        defaultValue = null,
    )
    val contentDescription = props.contentDescription

    if (iconName != null) {
        val resource = drawableResources[iconName]
        if (resource != null) {
            Icon(
                painter = painterResource(resource),
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint?.let { Color(it) } ?: LocalContentColor.current
            )
        } else {
            Text(
                text = iconName,
                modifier = modifier
            )
        }
    }
}
