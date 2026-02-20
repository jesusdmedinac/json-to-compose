package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.FloatingActionButton
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
fun ComposeNode.ToFloatingActionButton() {
    val props = properties as? NodeProperties.FabProps ?: return
    val icon = props.icon
    val onClickEventName = props.onClickEventName
    val currentBehavior = LocalBehavior.current
    val behavior = currentBehavior[onClickEventName]

    val containerColor = props.containerColor?.let { Color(it) }
        ?: FloatingActionButtonDefaults.containerColor

    val modifier = (Modifier from composeModifier).testTag(type.name)

    FloatingActionButton(
        onClick = {
            behavior?.invoke()
        },
        modifier = modifier,
        containerColor = containerColor,
    ) {
        // In Material 3 FAB, the content is typically an Icon.
        icon?.ToCompose()
    }
}
