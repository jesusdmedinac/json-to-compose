package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToTimePicker() {
    val props = properties as? NodeProperties.TimePickerProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (hour, hourStateHost) = resolveStateHostValue(
        stateHostName = props.hourStateHostName,
        inlineValue = props.hour,
        defaultValue = 0,
    )

    val (minute, minuteStateHost) = resolveStateHostValue(
        stateHostName = props.minuteStateHostName,
        inlineValue = props.minute,
        defaultValue = 0,
    )

    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = props.is24Hour ?: false
    )

    // Sync from internal state to StateHost
    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        if (timePickerState.hour != hour) {
            hourStateHost?.onStateChange(timePickerState.hour)
        }
        if (timePickerState.minute != minute) {
            minuteStateHost?.onStateChange(timePickerState.minute)
        }
    }

    TimePicker(
        state = timePickerState,
        modifier = modifier
    )
}
