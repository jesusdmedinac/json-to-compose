package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalMaterial3Api::class)
class TimePickerRendererTest {

    // --- Scenario: Render a TimePicker ---
    @Test
    fun timePickerRenders() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TimePicker,
            properties = NodeProperties.TimePickerProps(
                hour = 10,
                minute = 30,
                is24Hour = true
            )
        )

        setContent { node.ToTimePicker() }

        onNodeWithTag(ComposeType.TimePicker.name).assertExists()
    }
}
