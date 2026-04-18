package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToSlider() {
    val props = properties as? NodeProperties.SliderProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val behaviors = LocalBehavior.current

    val (value, valueStateHost) = resolveStateHostValue(
        stateHostName = props.valueStateHostName,
        inlineValue = props.value,
        defaultValue = 0f,
    )
    
    val (enabled, _) = resolveStateHostValue(
        stateHostName = props.enabledStateHostName,
        inlineValue = props.enabled,
        defaultValue = true,
    )

    val valueRange = props.valueRange?.let { it.start..it.endInclusive } ?: 0f..1f
    val steps = props.steps ?: 0

    Slider(
        value = value,
        onValueChange = { newValue ->
            valueStateHost?.onStateChange(newValue)
            val eventName = props.onValueChangeEventName
            if (eventName != null) {
                val behavior = behaviors[eventName]
                if (behavior != null) {
                    behavior.invoke()
                } else {
                    println("Warning: Behavior for event \"$eventName\" not found in LocalBehavior.")
                }
            }
        },
        onValueChangeFinished = {
            val eventName = props.onValueChangeFinishedEventName
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
        valueRange = valueRange,
        steps = steps
    )
}
