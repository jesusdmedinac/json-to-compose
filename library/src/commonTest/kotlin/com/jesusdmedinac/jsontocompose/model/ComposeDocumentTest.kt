package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ComposeDocumentTest {

    private val json = Json { encodeDefaults = true }

    // --- Scenario 1: ComposeDocument contains an initialState map ---

    @Test
    fun initialStateHoldsMultipleEntries() {
        val doc = ComposeDocument(
            initialState = mapOf(
                "switch_state" to JsonPrimitive(false),
                "text_value" to JsonPrimitive(""),
            ),
            root = ComposeNode(type = ComposeType.Text),
        )

        assertEquals(2, doc.initialState.size)
        assertEquals(JsonPrimitive(false), doc.initialState["switch_state"])
        assertEquals(JsonPrimitive(""), doc.initialState["text_value"])
    }

    @Test
    fun initialStateSupportsMixedTypes() {
        val doc = ComposeDocument(
            initialState = mapOf(
                "flag" to JsonPrimitive(true),
                "name" to JsonPrimitive("hello"),
                "count" to JsonPrimitive(42),
                "opacity" to JsonPrimitive(0.75f),
            ),
            root = ComposeNode(type = ComposeType.Text),
        )

        assertEquals(4, doc.initialState.size)
        assertEquals(JsonPrimitive(true), doc.initialState["flag"])
        assertEquals(JsonPrimitive("hello"), doc.initialState["name"])
        assertEquals(JsonPrimitive(42), doc.initialState["count"])
        assertEquals(JsonPrimitive(0.75f), doc.initialState["opacity"])
    }

    // --- Scenario 2: ComposeDocument contains an actions map of name to action list ---

    @Test
    fun actionsMapHoldsNamedActionLists() {
        val doc = ComposeDocument(
            actions = mapOf(
                "toggle_switch" to listOf(
                    ComposeAction.ToggleState(stateKey = "switch_state"),
                ),
                "dismiss_dialog" to listOf(
                    ComposeAction.SetState(stateKey = "dialog_visible", value = JsonPrimitive(false)),
                    ComposeAction.Log(message = "Dialog dismissed"),
                ),
            ),
            root = ComposeNode(type = ComposeType.Text),
        )

        assertEquals(2, doc.actions.size)
        assertEquals(1, doc.actions["toggle_switch"]?.size)
        assertEquals(2, doc.actions["dismiss_dialog"]?.size)
        assertIs<ComposeAction.ToggleState>(doc.actions["toggle_switch"]!![0])
        assertIs<ComposeAction.SetState>(doc.actions["dismiss_dialog"]!![0])
        assertIs<ComposeAction.Log>(doc.actions["dismiss_dialog"]!![1])
    }

    // --- Scenario 3: ComposeDocument contains a root ComposeNode ---

    @Test
    fun rootHoldsComposeNodeTree() {
        val root = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Hello"),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "World"),
                    ),
                )
            ),
        )
        val doc = ComposeDocument(root = root)

        assertEquals(ComposeType.Column, doc.root.type)
        val props = doc.root.properties as NodeProperties.ColumnProps
        assertEquals(2, props.children?.size)
    }

    // --- Scenario 4: ComposeDocument is serializable to and from JSON ---

    @Test
    fun serializesToJsonWithAllKeys() {
        val doc = ComposeDocument(
            initialState = mapOf("visible" to JsonPrimitive(true)),
            actions = mapOf(
                "hide" to listOf(
                    ComposeAction.SetState(stateKey = "visible", value = JsonPrimitive(false)),
                ),
            ),
            root = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Hello"),
            ),
        )

        val encoded = json.encodeToString(doc)

        assertTrue(encoded.contains("\"initialState\""), "JSON should contain initialState key")
        assertTrue(encoded.contains("\"actions\""), "JSON should contain actions key")
        assertTrue(encoded.contains("\"root\""), "JSON should contain root key")
    }

    @Test
    fun roundTripSerializationPreservesData() {
        val original = ComposeDocument(
            initialState = mapOf(
                "switch" to JsonPrimitive(false),
                "name" to JsonPrimitive("test"),
            ),
            actions = mapOf(
                "toggle" to listOf(
                    ComposeAction.ToggleState(stateKey = "switch"),
                ),
                "reset" to listOf(
                    ComposeAction.SetState(stateKey = "name", value = JsonPrimitive("")),
                    ComposeAction.Log(message = "reset"),
                ),
            ),
            root = ComposeNode(
                type = ComposeType.Column,
                properties = NodeProperties.ColumnProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Hello"),
                        ),
                    ),
                ),
            ),
        )

        val encoded = json.encodeToString(original)
        val decoded = json.decodeFromString<ComposeDocument>(encoded)

        // initialState preserved
        assertEquals(2, decoded.initialState.size)
        assertEquals(JsonPrimitive(false), decoded.initialState["switch"])
        assertEquals(JsonPrimitive("test"), decoded.initialState["name"])

        // actions preserved
        assertEquals(2, decoded.actions.size)
        assertEquals(1, decoded.actions["toggle"]?.size)
        assertIs<ComposeAction.ToggleState>(decoded.actions["toggle"]!![0])
        assertEquals(2, decoded.actions["reset"]?.size)

        // root preserved
        assertEquals(ComposeType.Column, decoded.root.type)
        val props = decoded.root.properties as NodeProperties.ColumnProps
        assertEquals(1, props.children?.size)
        assertEquals(ComposeType.Text, props.children?.get(0)?.type)
    }

    // --- Scenario 5: ComposeDocument with empty state and empty actions is valid ---

    @Test
    fun emptyStateAndActionsIsValid() {
        val doc = ComposeDocument(
            root = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Hello"),
            ),
        )

        assertEquals(emptyMap(), doc.initialState)
        assertEquals(emptyMap(), doc.actions)
        assertEquals(ComposeType.Text, doc.root.type)
    }

    @Test
    fun emptyDocumentRoundTripSerialization() {
        val original = ComposeDocument(
            root = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Static"),
            ),
        )

        val encoded = json.encodeToString(original)
        val decoded = json.decodeFromString<ComposeDocument>(encoded)

        assertEquals(emptyMap(), decoded.initialState)
        assertEquals(emptyMap(), decoded.actions)
        assertEquals("Static", (decoded.root.properties as NodeProperties.TextProps).text)
    }
}
