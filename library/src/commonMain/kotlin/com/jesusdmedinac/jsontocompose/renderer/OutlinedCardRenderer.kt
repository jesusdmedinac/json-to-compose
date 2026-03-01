package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

fun String.toColorOrNull(): Color? = runCatching {
    if (!startsWith("#")) {
        null
    } else {
        var colorString = substring(1)
        if (colorString.length == 6) {
            colorString = "FF$colorString"
        }
        Color(colorString.toLong(16))
    }
}
    .getOrNull()

@Composable
fun ComposeNode.ToOutlinedCard() {
    val props = properties as? NodeProperties.OutlinedCardProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (borderColorString, _) = resolveStateHostValue(
        stateHostName = props.borderColorStateHostName,
        inlineValue = props.borderColor,
        defaultValue = null,
    )
    
    val (cornerRadius, _) = resolveStateHostValue(
        stateHostName = props.cornerRadiusStateHostName,
        inlineValue = props.cornerRadius,
        defaultValue = 0,
    )

    val borderColor = borderColorString?.toColorOrNull()
    val borderStroke = if (borderColor != null) BorderStroke(1.dp, borderColor)
    else CardDefaults.outlinedCardBorder()

    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius.dp),
        border = borderStroke
    ) {
        props.child?.ToCompose()
    }
}
