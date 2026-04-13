package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import com.jesusdmedinac.jsontocompose.*
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToSegmentedButton() {
    val props = properties as? NodeProperties.SegmentedButtonProps ?: return
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

    // Fallback implementation using Button for now as SegmentedButtonRowScope is missing in this version
    Button(
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
        modifier = modifier.semantics {
            this.selected = selected
        },
        enabled = enabled,
    ) {
        props.icon?.ToCompose()
        props.label?.ToCompose()
    }
}
