package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NodePropertiesSerializationTest {

    private val json = Json { encodeDefaults = true }

    // --- Scenario 1: TextProps serialization with all fields ---

    @Test
    fun textPropsSerialization() {
        val original = NodeProperties.TextProps(text = "Hello")
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.TextProps>(decoded)
        assertEquals("Hello", decoded.text)
        assertTrue(encoded.contains("\"type\":\"TextProps\""))
    }

    // --- Scenario 2: TextProps default values ---

    @Test
    fun textPropsDefaultValues() {
        val jsonString = """{"type":"TextProps"}"""
        val decoded = json.decodeFromString<NodeProperties>(jsonString)

        assertIs<NodeProperties.TextProps>(decoded)
        assertNull(decoded.text)
    }

    // --- Scenario 3: ButtonProps serialization with child ---

    @Test
    fun buttonPropsSerializationWithChild() {
        val original = NodeProperties.ButtonProps(
            onClickEventName = "click_me",
            child = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Click")
            )
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ButtonProps>(decoded)
        assertEquals("click_me", decoded.onClickEventName)
        assertNotNull(decoded.child)
        assertEquals(ComposeType.Text, decoded.child!!.type)
        val childProps = decoded.child!!.properties as? NodeProperties.TextProps
        assertEquals("Click", childProps?.text)
    }

    // --- Scenario 4: ColumnProps serialization with children and layout options ---

    @Test
    fun columnPropsSerializationWithChildrenAndLayout() {
        val original = NodeProperties.ColumnProps(
            children = listOf(
                ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "A")),
                ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "B")),
            ),
            verticalArrangement = "SpaceBetween",
            horizontalAlignment = "CenterHorizontally",
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ColumnProps>(decoded)
        assertEquals(2, decoded.children?.size)
        assertEquals("SpaceBetween", decoded.verticalArrangement)
        assertEquals("CenterHorizontally", decoded.horizontalAlignment)
        val firstChild = decoded.children?.get(0)?.properties as? NodeProperties.TextProps
        assertEquals("A", firstChild?.text)
    }

    // --- Scenario 5: RowProps serialization with children and layout options ---

    @Test
    fun rowPropsSerializationWithChildrenAndLayout() {
        val original = NodeProperties.RowProps(
            children = listOf(
                ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "X")),
            ),
            verticalAlignment = "CenterVertically",
            horizontalArrangement = "SpaceAround",
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.RowProps>(decoded)
        assertEquals(1, decoded.children?.size)
        assertEquals("CenterVertically", decoded.verticalAlignment)
        assertEquals("SpaceAround", decoded.horizontalArrangement)
    }

    // --- Scenario 6: BoxProps serialization with all fields ---

    @Test
    fun boxPropsSerializationWithAllFields() {
        val original = NodeProperties.BoxProps(
            children = listOf(
                ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Inside")),
            ),
            contentAlignment = "Center",
            propagateMinConstraints = true,
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.BoxProps>(decoded)
        assertEquals(1, decoded.children?.size)
        assertEquals("Center", decoded.contentAlignment)
        assertEquals(true, decoded.propagateMinConstraints)
    }

    @Test
    fun boxPropsPropagateMinConstraintsDefaultsToNull() {
        val jsonString = """{"type":"BoxProps"}"""
        val decoded = json.decodeFromString<NodeProperties>(jsonString)

        assertIs<NodeProperties.BoxProps>(decoded)
        assertEquals(null, decoded.propagateMinConstraints)
    }

    // --- Scenario 7: ImageProps serialization with URL ---

    @Test
    fun imagePropsSerializationWithUrl() {
        val original = NodeProperties.ImageProps(
            url = "https://example.com/img.png",
            contentDescription = "A photo",
            contentScale = "Crop",
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ImageProps>(decoded)
        assertEquals("https://example.com/img.png", decoded.url)
        assertEquals("A photo", decoded.contentDescription)
        assertEquals("Crop", decoded.contentScale)
    }

    @Test
    fun imagePropsContentScaleDefaultsToNull() {
        val jsonString = """{"type":"ImageProps"}"""
        val decoded = json.decodeFromString<NodeProperties>(jsonString)

        assertIs<NodeProperties.ImageProps>(decoded)
        assertEquals(null, decoded.contentScale)
    }

    // --- Scenario 8: TextFieldProps serialization with valueStateHostName ---

    @Test
    fun textFieldPropsSerializationWithValueStateHostName() {
        val original = NodeProperties.TextFieldProps(valueStateHostName = "my_field")
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.TextFieldProps>(decoded)
        assertEquals("my_field", decoded.valueStateHostName)
        assertTrue(encoded.contains("\"valueStateHostName\""))
    }

    // --- Scenario 9: ScaffoldProps serialization with child ---

    @Test
    fun scaffoldPropsSerializationWithChild() {
        val original = NodeProperties.ScaffoldProps(
            child = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Body")
            )
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.ScaffoldProps>(decoded)
        assertNotNull(decoded.child)
        assertEquals(ComposeType.Text, decoded.child!!.type)
    }

    // --- Scenario 10: CardProps serialization with all fields ---

    @Test
    fun cardPropsSerializationWithAllFields() {
        val original = NodeProperties.CardProps(
            child = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Card content")
            ),
            elevation = 8,
            cornerRadius = 16,
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.CardProps>(decoded)
        assertNotNull(decoded.child)
        assertEquals(8, decoded.elevation)
        assertEquals(16, decoded.cornerRadius)
    }

    // --- Scenario 11: DialogProps serialization with all fields ---

    @Test
    fun dialogPropsSerializationWithAllFields() {
        val original = NodeProperties.AlertDialogProps(
            title = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Delete")
            ),
            text = ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Are you sure?")
            ),
            confirmButton = ComposeNode(
                type = ComposeType.Button,
                properties = NodeProperties.ButtonProps(
                    onClickEventName = "evt_confirm",
                    child = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Yes")
                    )
                )
            ),
            dismissButton = ComposeNode(
                type = ComposeType.Button,
                properties = NodeProperties.ButtonProps(
                    onClickEventName = "evt_dismiss",
                    child = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "No")
                    )
                )
            ),
            visibilityStateHostName = "dlg_visible",
            onDismissRequestEventName = "evt_dismiss_request",
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.AlertDialogProps>(decoded)
        assertNotNull(decoded.title)
        assertNotNull(decoded.text)
        assertNotNull(decoded.confirmButton)
        assertNotNull(decoded.dismissButton)
        assertEquals("dlg_visible", decoded.visibilityStateHostName)
        assertEquals("evt_dismiss_request", decoded.onDismissRequestEventName)
    }

    // --- Scenario 12: CustomProps serialization with customData ---

    @Test
    fun customPropsSerializationWithCustomData() {
        val original = NodeProperties.CustomProps(
            customType = "MyWidget",
            customData = buildJsonObject {
                put("key", JsonPrimitive("value"))
                put("count", JsonPrimitive(42))
            }
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.CustomProps>(decoded)
        assertEquals("MyWidget", decoded.customType)
        assertNotNull(decoded.customData)
        assertEquals("value", (decoded.customData!!["key"] as JsonPrimitive).content)
        assertEquals("42", (decoded.customData!!["count"] as JsonPrimitive).content)
    }

    // --- Scenario 13: NodeProperties polymorphic deserialization ---

    @Test
    fun nodePropertiesPolymorphicDeserialization() {
        val jsonString = """{"type":"TextProps","text":"Poly"}"""
        val decoded = json.decodeFromString<NodeProperties>(jsonString)

        assertIs<NodeProperties.TextProps>(decoded)
        assertEquals("Poly", decoded.text)
    }
}
