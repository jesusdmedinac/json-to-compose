package com.jesusdmedinac.jsontocompose.runtime

import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.MutableStateHost
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeAction
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActionDispatcherTest {

    // --- Scenario 1: Dispatcher executes SetState and updates the corresponding StateHost ---

    @Test
    fun setStateUpdatesIntStateHost() {
        val counterHost = MutableStateHost(0)
        val stateHosts = mapOf<String, StateHost<*>>("counter" to counterHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(
            ComposeAction.SetState(stateKey = "counter", value = JsonPrimitive(5))
        )

        assertEquals(5, counterHost.state)
    }

    @Test
    fun setStateUpdatesStringStateHost() {
        val nameHost = MutableStateHost("")
        val stateHosts = mapOf<String, StateHost<*>>("name" to nameHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(
            ComposeAction.SetState(stateKey = "name", value = JsonPrimitive("hello"))
        )

        assertEquals("hello", nameHost.state)
    }

    @Test
    fun setStateUpdatesBooleanStateHost() {
        val visibleHost = MutableStateHost(false)
        val stateHosts = mapOf<String, StateHost<*>>("visible" to visibleHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(
            ComposeAction.SetState(stateKey = "visible", value = JsonPrimitive(true))
        )

        assertEquals(true, visibleHost.state)
    }

    // --- Scenario 2: Dispatcher executes ToggleState and flips a boolean StateHost ---

    @Test
    fun toggleStateFlipsFalseToTrue() {
        val switchHost = MutableStateHost(false)
        val stateHosts = mapOf<String, StateHost<*>>("switch_state" to switchHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(ComposeAction.ToggleState(stateKey = "switch_state"))

        assertTrue(switchHost.state)
    }

    @Test
    fun toggleStateTwiceReturnsToOriginal() {
        val switchHost = MutableStateHost(false)
        val stateHosts = mapOf<String, StateHost<*>>("switch_state" to switchHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(ComposeAction.ToggleState(stateKey = "switch_state"))
        assertTrue(switchHost.state)

        dispatcher.dispatch(ComposeAction.ToggleState(stateKey = "switch_state"))
        assertFalse(switchHost.state)
    }

    // --- Scenario 3: Dispatcher executes Log and outputs the message ---

    @Test
    fun logOutputsMessage() {
        val logs = mutableListOf<String>()
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            logger = { logs.add(it) },
        )

        dispatcher.dispatch(ComposeAction.Log(message = "User clicked submit"))

        assertEquals(1, logs.size)
        assertEquals("User clicked submit", logs[0])
    }

    @Test
    fun logDoesNotModifyState() {
        val counterHost = MutableStateHost(42)
        val stateHosts = mapOf<String, StateHost<*>>("counter" to counterHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(ComposeAction.Log(message = "test"))

        assertEquals(42, counterHost.state)
    }

    // --- Scenario 4: Dispatcher executes Sequence and runs all child actions in order ---

    @Test
    fun sequenceExecutesAllChildActionsInOrder() {
        val loadingHost = MutableStateHost(false)
        val countHost = MutableStateHost(0)
        val stateHosts = mapOf<String, StateHost<*>>(
            "loading" to loadingHost,
            "count" to countHost,
        )
        val executionOrder = mutableListOf<String>()
        val dispatcher = ActionDispatcher(
            stateHosts = stateHosts,
            logger = { executionOrder.add(it) },
        )

        dispatcher.dispatch(
            ComposeAction.Sequence(
                actions = listOf(
                    ComposeAction.SetState(stateKey = "loading", value = JsonPrimitive(true)),
                    ComposeAction.Log(message = "step1"),
                    ComposeAction.SetState(stateKey = "count", value = JsonPrimitive(1)),
                    ComposeAction.Log(message = "step2"),
                )
            )
        )

        assertEquals(true, loadingHost.state)
        assertEquals(1, countHost.state)
        assertEquals(listOf("step1", "step2"), executionOrder)
    }

    // --- Scenario 5: Dispatcher executes Custom action via registered handler ---

    @Test
    fun customActionDelegatesToRegisteredHandler() {
        var receivedAction: ComposeAction.Custom? = null
        val handlers = mapOf<String, (ComposeAction.Custom) -> Unit>(
            "navigate" to { action -> receivedAction = action },
        )
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            customActionHandlers = handlers,
        )

        val params = buildJsonObject { put("route", JsonPrimitive("home")) }
        dispatcher.dispatch(ComposeAction.Custom(customType = "navigate", params = params))

        assertEquals("navigate", receivedAction?.customType)
        assertEquals("home", receivedAction?.params?.get("route")?.let {
            (it as JsonPrimitive).content
        })
    }

    // --- Scenario 6: Dispatcher warns on SetState for non-existent state key ---

    @Test
    fun setStateWarnsOnNonExistentKey() {
        val counterHost = MutableStateHost(0)
        val stateHosts = mapOf<String, StateHost<*>>("counter" to counterHost)
        val warnings = mutableListOf<String>()
        val dispatcher = ActionDispatcher(
            stateHosts = stateHosts,
            logger = { warnings.add(it) },
        )

        dispatcher.dispatch(
            ComposeAction.SetState(stateKey = "nonexistent", value = JsonPrimitive(5))
        )

        assertEquals(0, counterHost.state) // not modified
        assertEquals(1, warnings.size)
        assertTrue(warnings[0].contains("nonexistent"))
    }

    // --- Scenario 7: Dispatcher warns on ToggleState for non-boolean state key ---

    @Test
    fun toggleStateWarnsOnNonBooleanKey() {
        val textHost = MutableStateHost("hello")
        val stateHosts = mapOf<String, StateHost<*>>("text_value" to textHost)
        val warnings = mutableListOf<String>()
        val dispatcher = ActionDispatcher(
            stateHosts = stateHosts,
            logger = { warnings.add(it) },
        )

        dispatcher.dispatch(ComposeAction.ToggleState(stateKey = "text_value"))

        assertEquals("hello", textHost.state) // not modified
        assertEquals(1, warnings.size)
        assertTrue(warnings[0].contains("text_value"))
        assertTrue(warnings[0].contains("Boolean"))
    }

    @Test
    fun customActionWarnsOnUnregisteredType() {
        val warnings = mutableListOf<String>()
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            customActionHandlers = emptyMap(),
            logger = { warnings.add(it) },
        )

        dispatcher.dispatch(ComposeAction.Custom(customType = "unknown"))

        assertEquals(1, warnings.size)
        assertTrue(warnings[0].contains("unknown"))
    }
}
