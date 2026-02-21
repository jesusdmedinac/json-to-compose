package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToExtendedFloatingActionButton() {
    val props = properties as? NodeProperties.ExtendedFabProps ?: return
    val icon = props.icon
    val text = props.text
    val onClickEventName = props.onClickEventName
    val currentBehavior = LocalBehavior.current
    val behavior = currentBehavior[onClickEventName]

    val containerColor = props.containerColor?.let { Color(it) }
        ?: FloatingActionButtonDefaults.containerColor

    val modifier = (Modifier from composeModifier).testTag(type.name)

    ExtendedFloatingActionButton(
        onClick = {
            behavior?.invoke()
        },
        modifier = modifier,
        containerColor = containerColor,
    ) {
        icon?.ToCompose()
        text?.ToCompose()
    }
}
