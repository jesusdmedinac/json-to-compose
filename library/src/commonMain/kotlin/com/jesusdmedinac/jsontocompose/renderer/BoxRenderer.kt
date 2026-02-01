package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toAlignment
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToBox() {
    val props = properties as? NodeProperties.BoxProps ?: return
    val children = props.children
    val contentAlignment = props.contentAlignment
        ?.toAlignment()
        ?: Alignment.TopStart
    val propagateMinConstraints = props.propagateMinConstraints ?: false
    val modifier = (Modifier from composeModifier).testTag(type.name)
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
    ) {
        children?.forEach {
            it.ToCompose()
        }
    }
}
