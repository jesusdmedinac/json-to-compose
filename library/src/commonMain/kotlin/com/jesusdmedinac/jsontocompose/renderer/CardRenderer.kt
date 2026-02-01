package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToCard() {
    val props = properties as? NodeProperties.CardProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val elevation = (props.elevation ?: 1).dp
    val shape = RoundedCornerShape((props.cornerRadius ?: 0).dp)
    Card(
        modifier = modifier,
        elevation = elevation,
        shape = shape,
    ) {
        props.child?.ToCompose()
    }
}
