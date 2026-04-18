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
    LaunchedEffect(datePickerState.selectedDateMillis) {
        if (datePickerState.selectedDateMillis != selectedDateMillis) {
            stateHost?.onStateChange(datePickerState.selectedDateMillis)
        }
    }
    
    // Sync from StateHost to internal state
    LaunchedEffect(selectedDateMillis) {
        if (selectedDateMillis != datePickerState.selectedDateMillis) {
            datePickerState.selectedDateMillis = selectedDateMillis
        }
    }
    
    DatePicker(
        state = datePickerState,
        modifier = modifier
    )
}
