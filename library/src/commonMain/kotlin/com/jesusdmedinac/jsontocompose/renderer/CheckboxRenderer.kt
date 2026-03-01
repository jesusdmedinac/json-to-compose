package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToCheckbox() {
    val props = properties as? NodeProperties.CheckboxProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val behaviors = LocalBehavior.current

    val (checked, checkedStateHost) = resolveStateHostValue(
        stateHostName = props.checkedStateHostName,
        inlineValue = props.checked,
        defaultValue = false,
    )
    val (enabled, _) = resolveStateHostValue(
        stateHostName = props.enabledStateHostName,
        inlineValue = props.enabled,
        defaultValue = true,
    )

    Checkbox(
        checked = checked,
        onCheckedChange = { newValue ->
            checkedStateHost?.onStateChange(newValue)
            props.onCheckedChangeEventName?.let { behaviors[it]?.invoke() }
        },
        modifier = modifier,
        enabled = enabled,
    )
}
