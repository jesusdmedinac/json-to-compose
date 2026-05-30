package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    val (containerColorHex, _) = resolveStateHostValue(
        stateHostName = props.containerColorStateHostName,
        inlineValue = props.containerColor,
        defaultValue = null,
    )
    val (borderColorHex, _) = resolveStateHostValue(
        stateHostName = props.borderColorStateHostName,
        inlineValue = props.borderColor,
        defaultValue = null,
    )
    val (borderWidthVal, _) = resolveStateHostValue(
        stateHostName = props.borderWidthStateHostName,
        inlineValue = props.borderWidth,
        defaultValue = null,
    )

    val containerColor = containerColorHex?.toColor()
    val cardColors = if (containerColor != null) CardDefaults.cardColors(containerColor = containerColor)
                     else CardDefaults.cardColors()

    val borderColor = borderColorHex?.toColor()
    val borderStroke = if (borderColor != null) {
        BorderStroke((borderWidthVal ?: 1).dp, borderColor)
    } else null

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
        shape = RoundedCornerShape(cornerRadius.dp),
        colors = cardColors,
        border = borderStroke,
    ) {
        props.child?.ToCompose()
    }
}
