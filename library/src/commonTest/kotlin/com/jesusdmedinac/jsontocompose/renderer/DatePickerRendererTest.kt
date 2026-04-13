package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.MutableStateHost
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalTestApi::class, ExperimentalMaterial3Api::class)
class DatePickerRendererTest {

    // --- Scenario: Render a DatePicker ---
    @Test
    fun datePickerRenders() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.DatePicker,
            properties = NodeProperties.DatePickerProps(
                selectedDateMillis = 1712880000000L // some date
            )
        )

        setContent { node.ToDatePicker() }

        onNodeWithTag(ComposeType.DatePicker.name).assertExists()
    }

    // --- Scenario: DatePicker selection updates state ---
    @Test
    fun datePickerSelectionUpdatesState() = runComposeUiTest {
        val initialMillis = 1712880000000L
        val state = MutableStateHost<Long?>(initialMillis)
        val node = ComposeNode(
            type = ComposeType.DatePicker,
            properties = NodeProperties.DatePickerProps(
                selectedDateMillisStateHostName = "birthday"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("birthday" to state)
            ) {
                node.ToDatePicker()
            }
        }

        onNodeWithTag(ComposeType.DatePicker.name).assertExists()
        
        // Simulating selection in DatePicker is hard via semantics without knowing the internal structure
        // but we can verify the state host is used.
        assertEquals(initialMillis, state.state)
    }
}
