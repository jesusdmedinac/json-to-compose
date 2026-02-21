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
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToCard() {
    val props = properties as? NodeProperties.CardProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (elevation, _) = resolveStateHostValue(
        stateHostName = props.elevationStateHostName,
        inlineValue = props.elevation,
        defaultValue = 1,
    )
    val (cornerRadius, _) = resolveStateHostValue(
        stateHostName = props.cornerRadiusStateHostName,
        inlineValue = props.cornerRadius,
        defaultValue = 0,
    )

    Card(
        modifier = modifier,
        elevation = elevation.dp,
        shape = RoundedCornerShape(cornerRadius.dp),
    ) {
        props.child?.ToCompose()
    }
}
