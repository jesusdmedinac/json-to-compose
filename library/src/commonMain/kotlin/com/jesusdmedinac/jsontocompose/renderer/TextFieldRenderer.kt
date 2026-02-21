package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToTextField() {
    val props = properties as? NodeProperties.TextFieldProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (value, valueStateHost) = resolveStateHostValue(
        stateHostName = props.valueStateHostName,
        inlineValue = props.value,
        defaultValue = "",
    )

    TextField(
        value = value,
        onValueChange = { newValue ->
            valueStateHost?.onStateChange(newValue)
        },
        modifier = modifier,
    )
}
