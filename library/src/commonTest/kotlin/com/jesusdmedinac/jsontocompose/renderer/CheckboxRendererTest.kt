package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class CheckboxRendererTest {

    private fun createBooleanStateHost(initial: Boolean): StateHost<Boolean> {
        var value by mutableStateOf(initial)
        return object : StateHost<Boolean> {
            override val state: Boolean get() = value
            override fun onStateChange(state: Boolean) { value = state }
        }
    }

    private fun stateHosts(
        checked: Boolean = false,
        enabled: Boolean = true,
    ): Map<String, StateHost<*>> = mapOf(
        "cb_checked" to createBooleanStateHost(checked),
        "cb_enabled" to createBooleanStateHost(enabled),
    )

    // --- Scenario 1: Render an unchecked Checkbox ---

    @Test
    fun checkboxRendersUnchecked() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Checkbox,
            properties = NodeProperties.CheckboxProps(
                checkedStateHostName = "cb_checked",
                enabledStateHostName = "cb_enabled",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides stateHosts(checked = false)
            ) {
                node.ToCompose()
            }
        }

        onNodeWithTag("Checkbox").assertIsDisplayed()
        onNode(isToggleable()).assertIsOff()
    }

    // --- Scenario 2: Render a checked Checkbox ---

    @Test
    fun checkboxRendersChecked() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Checkbox,
            properties = NodeProperties.CheckboxProps(
                checkedStateHostName = "cb_checked",
                enabledStateHostName = "cb_enabled",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides stateHosts(checked = true)
            ) {
                node.ToCompose()
            }
        }

        onNodeWithTag("Checkbox").assertIsDisplayed()
        onNode(isToggleable()).assertIsOn()
    }

    // --- Scenario 3: Checkbox emits event when state changes ---

    @Test
    fun checkboxEmitsEventWhenStateChanges() = runComposeUiTest {
        var checked by mutableStateOf(false)
        var eventInvoked = false
        val checkedStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = checked
            override fun onStateChange(state: Boolean) { checked = state }
        }
        val enabledStateHost = createBooleanStateHost(true)
        val mockBehavior = object : Behavior {
            override fun invoke() { eventInvoked = true }
        }

        val node = ComposeNode(
            type = ComposeType.Checkbox,
            properties = NodeProperties.CheckboxProps(
                checkedStateHostName = "cb_checked",
                enabledStateHostName = "cb_enabled",
                onCheckedChangeEventName = "cb_toggled",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "cb_checked" to checkedStateHost,
                    "cb_enabled" to enabledStateHost,
                ),
                LocalBehavior provides mapOf("cb_toggled" to mockBehavior),
            ) {
                node.ToCompose()
            }
        }

        onNode(isToggleable()).performClick()
        assertTrue(checked, "Checkbox state should be true after click")
        assertTrue(eventInvoked, "Behavior for cb_toggled should have been invoked")
    }

    // --- Scenario 4: Checkbox with label composed via Row ---

    @Test
    fun checkboxRendersWithLabelViaRow() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Row,
            properties = NodeProperties.RowProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Checkbox,
                        properties = NodeProperties.CheckboxProps(
                            checkedStateHostName = "cb_checked",
                            enabledStateHostName = "cb_enabled",
                        ),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Accept terms"),
                    ),
                ),
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides stateHosts(checked = false)
            ) {
                node.ToCompose()
            }
        }

        onNode(isToggleable()).assertIsDisplayed()
        onNodeWithText("Accept terms").assertIsDisplayed()
    }

    // --- Scenario 5: Serialize and deserialize a Checkbox from JSON ---

    @Test
    fun checkboxSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.Checkbox,
            properties = NodeProperties.CheckboxProps(
                checkedStateHostName = "my_checkbox",
                onCheckedChangeEventName = "checkbox_changed",
                enabledStateHostName = "my_checkbox_enabled",
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.Checkbox, decoded.type)
        val props = decoded.properties as? NodeProperties.CheckboxProps
        assertNotNull(props)
        assertEquals("my_checkbox", props.checkedStateHostName)
        assertEquals("checkbox_changed", props.onCheckedChangeEventName)
        assertEquals("my_checkbox_enabled", props.enabledStateHostName)
    }
}
