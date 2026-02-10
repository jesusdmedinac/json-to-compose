package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ComposeActionTest {

    private val json = Json { encodeDefaults = true }

    // --- Scenario 1: SetState action holds a stateKey and a value ---

    @Test
    fun setStateHoldsStateKeyAndIntValue() {
        val action = ComposeAction.SetState(
            stateKey = "counter",
            value = JsonPrimitive(5),
        )

        assertEquals("counter", action.stateKey)
        assertEquals(5, action.value.jsonPrimitive.int)
    }

    @Test
    fun setStateHoldsStringValue() {
        val action = ComposeAction.SetState(
            stateKey = "name",
            value = JsonPrimitive("hello"),
        )

        assertEquals("hello", action.value.jsonPrimitive.content)
    }

    @Test
    fun setStateHoldsBooleanValue() {
        val action = ComposeAction.SetState(
            stateKey = "visible",
            value = JsonPrimitive(true),
        )

        assertTrue(action.value.jsonPrimitive.content.toBoolean())
    }

    @Test
    fun setStateHoldsFloatValue() {
        val action = ComposeAction.SetState(
            stateKey = "opacity",
            value = JsonPrimitive(0.5f),
        )

        assertEquals(0.5f, action.value.jsonPrimitive.content.toFloat())
    }

    // --- Scenario 2: ToggleState action holds a stateKey ---

    @Test
    fun toggleStateHoldsStateKey() {
        val action = ComposeAction.ToggleState(stateKey = "switch_state")

        assertEquals("switch_state", action.stateKey)
    }

    // --- Scenario 3: Log action holds a message string ---

    @Test
    fun logHoldsMessage() {
        val action = ComposeAction.Log(message = "Button was clicked")

        assertEquals("Button was clicked", action.message)
    }

    // --- Scenario 4: Sequence action holds a list of child actions ---

    @Test
    fun sequenceHoldsChildActions() {
        val action = ComposeAction.Sequence(
            actions = listOf(
                ComposeAction.SetState(stateKey = "loading", value = JsonPrimitive(true)),
                ComposeAction.Log(message = "started"),
            )
        )

        assertEquals(2, action.actions.size)
        assertIs<ComposeAction.SetState>(action.actions[0])
        assertIs<ComposeAction.Log>(action.actions[1])
    }

    // --- Scenario 5: Custom action holds a type and a params map ---

    @Test
    fun customHoldsTypeAndParams() {
        val params = buildJsonObject {
            put("route", JsonPrimitive("home"))
        }
        val action = ComposeAction.Custom(customType ="navigate", params = params)

        assertEquals("navigate", action.customType)
        assertEquals("home", action.params["route"]?.jsonPrimitive?.content)
    }

    @Test
    fun customDefaultsToEmptyParams() {
        val action = ComposeAction.Custom(customType ="analytics")

        assertEquals("analytics", action.customType)
        assertEquals(JsonObject(emptyMap()), action.params)
    }

    // --- Scenario 6: ComposeAction is serializable to and from JSON ---

    @Test
    fun setStateSerializesToJson() {
        val action: ComposeAction = ComposeAction.SetState(
            stateKey = "visible",
            value = JsonPrimitive(true),
        )
        val encoded = json.encodeToString(action)

        assertTrue(encoded.contains("\"setState\"") || encoded.contains("setState"), "JSON should contain action type")
        assertTrue(encoded.contains("\"visible\""), "JSON should contain stateKey")
        assertTrue(encoded.contains("true"), "JSON should contain value")
    }

    @Test
    fun setStateRoundTripSerialization() {
        val original: ComposeAction = ComposeAction.SetState(
            stateKey = "visible",
            value = JsonPrimitive(true),
        )
        val encoded = json.encodeToString(original)
        val decoded = json.decodeFromString<ComposeAction>(encoded)

        assertIs<ComposeAction.SetState>(decoded)
        assertEquals("visible", decoded.stateKey)
        assertEquals(JsonPrimitive(true), decoded.value)
    }

    @Test
    fun toggleStateRoundTripSerialization() {
        val original: ComposeAction = ComposeAction.ToggleState(stateKey = "switch_state")
        val encoded = json.encodeToString(original)
        val decoded = json.decodeFromString<ComposeAction>(encoded)

        assertIs<ComposeAction.ToggleState>(decoded)
        assertEquals("switch_state", decoded.stateKey)
    }

    @Test
    fun logRoundTripSerialization() {
        val original: ComposeAction = ComposeAction.Log(message = "clicked")
        val encoded = json.encodeToString(original)
        val decoded = json.decodeFromString<ComposeAction>(encoded)

        assertIs<ComposeAction.Log>(decoded)
        assertEquals("clicked", decoded.message)
    }

    @Test
    fun sequenceRoundTripSerialization() {
        val original: ComposeAction = ComposeAction.Sequence(
            actions = listOf(
                ComposeAction.SetState(stateKey = "a", value = JsonPrimitive(1)),
                ComposeAction.ToggleState(stateKey = "b"),
                ComposeAction.Log(message = "done"),
            )
        )
        val encoded = json.encodeToString(original)
        val decoded = json.decodeFromString<ComposeAction>(encoded)

        assertIs<ComposeAction.Sequence>(decoded)
        assertEquals(3, decoded.actions.size)
        assertIs<ComposeAction.SetState>(decoded.actions[0])
        assertIs<ComposeAction.ToggleState>(decoded.actions[1])
        assertIs<ComposeAction.Log>(decoded.actions[2])
    }

    @Test
    fun customRoundTripSerialization() {
        val original: ComposeAction = ComposeAction.Custom(
            customType = "navigate",
            params = buildJsonObject {
                put("route", JsonPrimitive("home"))
            },
        )
        val encoded = json.encodeToString(original)
        val decoded = json.decodeFromString<ComposeAction>(encoded)

        assertIs<ComposeAction.Custom>(decoded)
        assertEquals("navigate", decoded.customType)
        assertEquals("home", decoded.params["route"]?.jsonPrimitive?.content)
    }
}
