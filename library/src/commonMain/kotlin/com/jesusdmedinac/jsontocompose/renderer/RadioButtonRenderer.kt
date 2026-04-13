package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToRadioButton() {
    val props = properties as? NodeProperties.RadioButtonProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val behaviors = LocalBehavior.current

    val (selected, selectedStateHost) = resolveStateHostValue(
        stateHostName = props.selectedStateHostName,
        inlineValue = props.selected,
        defaultValue = false,
    )
    
    val (enabled, _) = resolveStateHostValue(
        stateHostName = props.enabledStateHostName,
        inlineValue = props.enabled,
        defaultValue = true,
    )

    RadioButton(
        selected = selected,
        onClick = {
            selectedStateHost?.onStateChange(!selected)
            val eventName = props.onClickEventName
            if (eventName != null) {
                val behavior = behaviors[eventName]
                if (behavior != null) {
                    behavior.invoke()
                } else {
                    println("Warning: Behavior for event \"$eventName\" not found in LocalBehavior.")
                }
            }
        },
        modifier = modifier,
        enabled = enabled,
    )
}
