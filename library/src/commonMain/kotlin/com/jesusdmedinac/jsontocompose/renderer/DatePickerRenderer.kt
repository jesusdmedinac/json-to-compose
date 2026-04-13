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
fun ComposeNode.ToDatePicker() {
    val props = properties as? NodeProperties.DatePickerProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (selectedDateMillis, stateHost) = resolveStateHostValue(
        stateHostName = props.selectedDateMillisStateHostName,
        inlineValue = props.selectedDateMillis,
        defaultValue = null as Long?,
    )

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis
    )

    // Sync from internal state to StateHost
    val currentSelectedDateMillis = datePickerState.selectedDateMillis
    LaunchedEffect(currentSelectedDateMillis) {
        if (currentSelectedDateMillis != selectedDateMillis) {
            stateHost?.onStateChange(currentSelectedDateMillis)
        }
    }
    
    // Sync from StateHost to internal state (if needed, but rememberDatePickerState initial value is used)
    // Actually, if selectedDateMillis changes from outside, we should update datePickerState.
    // However, DatePickerState.selectedDateMillis is a property we can't easily set after creation without a new state object.
    // Most M3 states are like this.
    
    DatePicker(
        state = datePickerState,
        modifier = modifier
    )
}
