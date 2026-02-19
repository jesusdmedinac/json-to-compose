package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.resolveStateHostValue
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToText() {
    val props = properties as? NodeProperties.TextProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (text, _) = resolveStateHostValue(
        stateHostName = props.textStateHostName,
        inlineValue = props.text,
        defaultValue = "",
    )

    Text(
        text = text,
        modifier = modifier,
    )
}
