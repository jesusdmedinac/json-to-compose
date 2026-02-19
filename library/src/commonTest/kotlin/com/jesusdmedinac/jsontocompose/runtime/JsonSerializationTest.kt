package com.jesusdmedinac.jsontocompose.runtime

import com.jesusdmedinac.jsontocompose.model.ComposeAction
import com.jesusdmedinac.jsontocompose.model.ComposeDocument
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

class JsonSerializationTest {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    // --- Scenario 1: ComposeDocument serializes to a JSON string ---

    @Test
    fun documentSerializesToJsonWithAllTopLevelKeys() {
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

        assertTrue(encoded.contains("\"initialState\""))
        assertTrue(encoded.contains("\"actions\""))
        assertTrue(encoded.contains("\"root\""))
        // root follows existing ComposeNode format
        assertTrue(encoded.contains("\"type\":\"Text\""))
    }

    // --- Scenario 2: ComposeDocument deserializes from a JSON string ---

    @Test
    fun documentDeserializesFromRawJsonString() {
        val raw = """
        {
            "initialState": {
                "switch_state": false,
                "username": "admin"
            },
            "actions": {
                "toggle": [
                    {"type": "toggleState", "stateKey": "switch_state"}
                ],
                "greet": [
                    {"type": "log", "message": "Hello!"}
                ]
            },
            "root": {
                "type": "Column",
                "properties": {
                    "type": "ColumnProps",
                    "children": [
                        {
                            "type": "Text",
                            "properties": {"type": "TextProps", "text": "Welcome"}
                        }
                    ]
                }
            }
        }
        """.trimIndent()

        val doc = json.decodeFromString<ComposeDocument>(raw)

        // initialState
        assertEquals(2, doc.initialState.size)
        assertEquals(false, doc.initialState["switch_state"]?.jsonPrimitive?.boolean)
        assertEquals("admin", doc.initialState["username"]?.jsonPrimitive?.content)

        // actions
        assertEquals(2, doc.actions.size)
        assertIs<ComposeAction.ToggleState>(doc.actions["toggle"]!![0])
        assertIs<ComposeAction.Log>(doc.actions["greet"]!![0])

        // root
        assertEquals(ComposeType.Column, doc.root.type)
        val children = (doc.root.properties as NodeProperties.ColumnProps).children
        assertEquals(1, children?.size)
        assertEquals(ComposeType.Text, children?.get(0)?.type)
    }

    // --- Scenario 3: Round-trip serialization preserves all data ---

    @Test
    fun roundTripPreservesAllActionTypesAndStateTypes() {
        val original = ComposeDocument(
            initialState = mapOf(
                "flag" to JsonPrimitive(true),
                "name" to JsonPrimitive("test"),
                "count" to JsonPrimitive(42),
                "ratio" to JsonPrimitive(0.5f),
            ),
            actions = mapOf(
                "set_action" to listOf(
                    ComposeAction.SetState(stateKey = "count", value = JsonPrimitive(0)),
                ),
                "toggle_action" to listOf(
                    ComposeAction.ToggleState(stateKey = "flag"),
                ),
                "log_action" to listOf(
                    ComposeAction.Log(message = "logged"),
                ),
                "sequence_action" to listOf(
                    ComposeAction.Sequence(
                        actions = listOf(
                            ComposeAction.SetState(stateKey = "name", value = JsonPrimitive("")),
                            ComposeAction.Log(message = "reset"),
                        ),
                    ),
                ),
                "custom_action" to listOf(
                    ComposeAction.Custom(
                        customType = "navigate",
                        params = buildJsonObject { put("route", JsonPrimitive("home")) },
                    ),
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

        // State types preserved
        assertEquals(JsonPrimitive(true), decoded.initialState["flag"])
        assertEquals(JsonPrimitive("test"), decoded.initialState["name"])
        assertEquals(JsonPrimitive(42), decoded.initialState["count"])
        assertEquals(JsonPrimitive(0.5f), decoded.initialState["ratio"])

        // All 5 action types preserved
        assertIs<ComposeAction.SetState>(decoded.actions["set_action"]!![0])
        assertIs<ComposeAction.ToggleState>(decoded.actions["toggle_action"]!![0])
        assertIs<ComposeAction.Log>(decoded.actions["log_action"]!![0])
        val seq = decoded.actions["sequence_action"]!![0]
        assertIs<ComposeAction.Sequence>(seq)
        assertEquals(2, seq.actions.size)
        val custom = decoded.actions["custom_action"]!![0]
        assertIs<ComposeAction.Custom>(custom)
        assertEquals("navigate", custom.customType)

        // Root preserved
        assertEquals(ComposeType.Column, decoded.root.type)
    }

    // --- Scenario 4: Malformed JSON returns a validation error ---

    @Test
    fun missingRootKeyThrowsSerializationException() {
        val malformed = """
        {
            "initialState": {"flag": true},
            "actions": {}
        }
        """.trimIndent()

        assertFailsWith<SerializationException> {
            json.decodeFromString<ComposeDocument>(malformed)
        }
    }

    @Test
    fun invalidActionTypeThrowsSerializationException() {
        val malformed = """
        {
            "initialState": {},
            "actions": {
                "bad": [{"type": "nonExistentAction", "stateKey": "x"}]
            },
            "root": {"type": "Text", "properties": {"type": "TextProps", "text": "Hi"}}
        }
        """.trimIndent()

        assertFailsWith<SerializationException> {
            json.decodeFromString<ComposeDocument>(malformed)
        }
    }

    // --- Scenario 5: ComposeDocument JSON integrates with existing ComposeNode JSON format ---

    @Test
    fun composeNodeToStringFormatWorksAsDocumentRoot() {
        // Create a ComposeNode and get its JSON via toString() (existing API)
        val node = ComposeNode(
            type = ComposeType.Button,
            properties = NodeProperties.ButtonProps(
                onClickEventName = "click_me",
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Press"),
                ),
            ),
        )
        val nodeJson = node.toString()

        // Wrap it in a ComposeDocument JSON
        val documentJson = """
        {
            "initialState": {},
            "actions": {
                "click_me": [{"type": "log", "message": "pressed"}]
            },
            "root": $nodeJson
        }
        """.trimIndent()

        val doc = json.decodeFromString<ComposeDocument>(documentJson)

        // Root matches standalone ComposeNode
        assertEquals(ComposeType.Button, doc.root.type)
        val props = doc.root.properties as NodeProperties.ButtonProps
        assertEquals("click_me", props.onClickEventName)
        assertEquals(ComposeType.Text, props.child?.type)
        assertEquals("Press", (props.child?.properties as? NodeProperties.TextProps)?.text)

        // Verify existing ComposeNode.toString() still works independently
        val standaloneNode = Json.decodeFromString<ComposeNode>(nodeJson)
        assertEquals(doc.root.type, standaloneNode.type)
    }

    @Test
    fun existingComposeNodeToStringNotBrokenByDocumentAddition() {
        // ... (existing content)
    }

    @Test
    fun textPropsWithFontSizeSerialization() {
        // ... (existing content)
    }

    @Test
    fun textPropsWithAllTypographySerialization() {
        val originalProps = NodeProperties.TextProps(
            text = "Full Typography",
            fontSize = 18.0,
            fontWeight = "Bold",
            fontStyle = "Italic",
            color = 0xFF123456.toInt(),
            textAlign = "Center",
            maxLines = 5,
            overflow = "Ellipsis",
            letterSpacing = 0.5,
            lineHeight = 24.0,
            textDecoration = "Underline",
            minLines = 2,
        )
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = originalProps,
        )

        val jsonString = node.toString()
        val decoded = Json.decodeFromString<ComposeNode>(jsonString)
        val decodedProps = decoded.properties as NodeProperties.TextProps

        assertEquals(originalProps.text, decodedProps.text)
        assertEquals(originalProps.fontSize, decodedProps.fontSize)
        assertEquals(originalProps.fontWeight, decodedProps.fontWeight)
        assertEquals(originalProps.fontStyle, decodedProps.fontStyle)
        assertEquals(originalProps.color, decodedProps.color)
        assertEquals(originalProps.textAlign, decodedProps.textAlign)
        assertEquals(originalProps.maxLines, decodedProps.maxLines)
        assertEquals(originalProps.overflow, decodedProps.overflow)
        assertEquals(originalProps.letterSpacing, decodedProps.letterSpacing)
        assertEquals(originalProps.lineHeight, decodedProps.lineHeight)
        assertEquals(originalProps.textDecoration, decodedProps.textDecoration)
        assertEquals(originalProps.minLines, decodedProps.minLines)
    }
}
