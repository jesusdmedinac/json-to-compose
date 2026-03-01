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
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class SwitchRendererTest {

    private fun createEnabledStateHost(enabled: Boolean = true): StateHost<Boolean> {
        var value by mutableStateOf(enabled)
        return object : StateHost<Boolean> {
            override val state: Boolean get() = value
            override fun onStateChange(state: Boolean) { value = state }
        }
    }

    // --- Scenario 1: Render a Switch in off state ---

    @Test
    fun switchRendersInOffState() = runComposeUiTest {
        var checked by mutableStateOf(false)
        val checkedStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = checked
            override fun onStateChange(state: Boolean) { checked = state }
        }
        val enabledStateHost = createEnabledStateHost()

        val node = ComposeNode(
            type = ComposeType.Switch,
            properties = NodeProperties.SwitchProps(
                checkedStateHostName = "switch_state",
                enabledStateHostName = "switch_enabled",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "switch_state" to checkedStateHost,
                    "switch_enabled" to enabledStateHost,
                )
            ) {
                node.ToCompose()
            }
        }

        onNodeWithTag("Switch").assertIsDisplayed()
        onNodeWithTag("Switch").assertIsToggleable()
        onNodeWithTag("Switch").assertIsOff()
    }

    // --- Scenario 2: Render a Switch in on state ---

    @Test
    fun switchRendersInOnState() = runComposeUiTest {
        var checked by mutableStateOf(true)
        val checkedStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = checked
            override fun onStateChange(state: Boolean) { checked = state }
        }
        val enabledStateHost = createEnabledStateHost()

        val node = ComposeNode(
            type = ComposeType.Switch,
            properties = NodeProperties.SwitchProps(
                checkedStateHostName = "switch_state",
                enabledStateHostName = "switch_enabled",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "switch_state" to checkedStateHost,
                    "switch_enabled" to enabledStateHost,
                )
            ) {
                node.ToCompose()
            }
        }

        onNodeWithTag("Switch").assertIsDisplayed()
        onNodeWithTag("Switch").assertIsOn()
    }

    // --- Scenario 3: Switch emits event when state changes ---

    @Test
    fun switchEmitsEventWhenStateChanges() = runComposeUiTest {
        var checked by mutableStateOf(false)
        var eventInvoked = false
        val checkedStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = checked
            override fun onStateChange(state: Boolean) { checked = state }
        }
        val enabledStateHost = createEnabledStateHost()
        val mockBehavior = object : Behavior {
            override fun invoke() { eventInvoked = true }
        }

        val node = ComposeNode(
            type = ComposeType.Switch,
            properties = NodeProperties.SwitchProps(
                checkedStateHostName = "switch_state",
                enabledStateHostName = "switch_enabled",
                onCheckedChangeEventName = "switch_toggled",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "switch_state" to checkedStateHost,
                    "switch_enabled" to enabledStateHost,
                ),
                LocalBehavior provides mapOf("switch_toggled" to mockBehavior),
            ) {
                node.ToCompose()
            }
        }

        onNodeWithTag("Switch").performClick()
        assertTrue(checked, "Switch state should be true after click")
        assertTrue(eventInvoked, "Behavior for switch_toggled should have been invoked")
    }

    // --- Scenario 4: Switch reflects state from StateHost ---

    @Test
    fun switchReflectsStateFromStateHost() = runComposeUiTest {
        var checked by mutableStateOf(false)
        val checkedStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = checked
            override fun onStateChange(state: Boolean) { checked = state }
        }
        val enabledStateHost = createEnabledStateHost()

        val node = ComposeNode(
            type = ComposeType.Switch,
            properties = NodeProperties.SwitchProps(
                checkedStateHostName = "switch_state",
                enabledStateHostName = "switch_enabled",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "switch_state" to checkedStateHost,
                    "switch_enabled" to enabledStateHost,
                )
            ) {
                node.ToCompose()
            }
        }

        onNodeWithTag("Switch").assertIsOff()

        // Toggle via click
        onNodeWithTag("Switch").performClick()
        assertEquals(true, checked)
        onNodeWithTag("Switch").assertIsOn()
    }

    // --- Scenario 5: Serialize and deserialize a Switch from JSON ---

    @Test
    fun switchSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.Switch,
            properties = NodeProperties.SwitchProps(
                checkedStateHostName = "my_switch",
                onCheckedChangeEventName = "switch_changed",
                enabledStateHostName = "my_switch_enabled",
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.Switch, decoded.type)
        val props = decoded.properties as? NodeProperties.SwitchProps
        assertNotNull(props)
        assertEquals("my_switch", props.checkedStateHostName)
        assertEquals("switch_changed", props.onCheckedChangeEventName)
        assertEquals("my_switch_enabled", props.enabledStateHostName)
    }
}
