package com.jesusdmedinac.jsontocompose.pipeline

import androidx.compose.material.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalCustomRenderers
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class JsonToComposePipelineTest {

    // --- Full pipeline for simple Text ---

    @Test
    fun fullPipelineSimpleText() = runComposeUiTest {
        val json = """{"type":"Text","properties":{"type":"TextProps","text":"Hello"}}"""

        setContent {
            json.ToCompose()
        }

        onNodeWithText("Hello").assertExists()
    }

    // --- Full pipeline for Column with children ---

    @Test
    fun fullPipelineColumnWithChildren() = runComposeUiTest {
        val json = """
        {
          "type": "Column",
          "properties": {
            "type": "ColumnProps",
            "children": [
              {"type": "Text", "properties": {"type": "TextProps", "text": "First"}},
              {"type": "Text", "properties": {"type": "TextProps", "text": "Second"}}
            ]
          }
        }
        """.trimIndent()

        setContent {
            json.ToCompose()
        }

        onNodeWithText("First").assertExists()
        onNodeWithText("Second").assertExists()
    }

    // --- Full pipeline for deep nested tree ---

    @Test
    fun fullPipelineDeepNestedTree() = runComposeUiTest {
        val json = """
        {
          "type": "Column",
          "properties": {
            "type": "ColumnProps",
            "children": [
              {
                "type": "Row",
                "properties": {
                  "type": "RowProps",
                  "children": [
                    {
                      "type": "Box",
                      "properties": {
                        "type": "BoxProps",
                        "children": [
                          {"type": "Text", "properties": {"type": "TextProps", "text": "Deep"}}
                        ]
                      }
                    }
                  ]
                }
              }
            ]
          }
        }
        """.trimIndent()

        setContent {
            json.ToCompose()
        }

        onNodeWithText("Deep").assertExists()
    }

    // --- Full pipeline with modifiers ---

    @Test
    fun fullPipelineWithModifiers() = runComposeUiTest {
        val json = """
        {
          "type": "Text",
          "properties": {"type": "TextProps", "text": "Styled"},
          "composeModifier": {
            "operations": [
              {"type": "Padding", "value": 16},
              {"type": "BackgroundColor", "hexColor": "#FF00FF00"}
            ]
          }
        }
        """.trimIndent()

        setContent {
            json.ToCompose()
        }

        onNodeWithText("Styled").assertExists()
    }

    // --- Full pipeline with Button and Behavior ---

    @Test
    fun fullPipelineButtonWithBehavior() = runComposeUiTest {
        var clicked = false
        val mockBehavior = object : Behavior {
            override fun onClick() {
                clicked = true
            }
        }

        val json = """
        {
          "type": "Button",
          "properties": {
            "type": "ButtonProps",
            "onClickEventName": "click_action",
            "child": {"type": "Text", "properties": {"type": "TextProps", "text": "Tap"}}
          }
        }
        """.trimIndent()

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("click_action" to mockBehavior)
            ) {
                json.ToCompose()
            }
        }

        onNodeWithText("Tap").performClick()
        assertTrue(clicked, "Behavior.onClick should have been called")
    }

    // --- Full pipeline with TextField and StateHost ---

    @Test
    fun fullPipelineTextFieldWithStateHost() = runComposeUiTest {
        var currentValue by mutableStateOf("start")
        val mockStateHost = object : StateHost<String> {
            override val state: String get() = currentValue
            override fun onStateChange(state: String) {
                currentValue = state
            }
        }

        val json = """
        {
          "type": "TextField",
          "properties": {
            "type": "TextFieldProps",
            "valueStateHostName": "input_field"
          }
        }
        """.trimIndent()

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("input_field" to mockStateHost)
            ) {
                json.ToCompose()
            }
        }

        onNodeWithText("start").assertExists()
        onNodeWithText("start").performTextReplacement("typed")
        assertEquals("typed", currentValue)
    }

    // --- Full pipeline with Image from URL ---

    @Test
    fun fullPipelineImageFromUrl() = runComposeUiTest {
        val json = """
        {
          "type": "Image",
          "properties": {
            "type": "ImageProps",
            "url": "https://example.com/photo.jpg",
            "contentDescription": "A photo"
          }
        }
        """.trimIndent()

        setContent {
            json.ToCompose()
        }

        // AsyncImage is rendered without crash.
        // In test environment the image won't load, but the composable is in the tree.
    }

    // --- Full pipeline with Custom component ---

    @Test
    fun fullPipelineCustomComponent() = runComposeUiTest {
        var receivedType: String? = null

        val json = """
        {
          "type": "Custom",
          "properties": {
            "type": "CustomProps",
            "customType": "StarRating",
            "customData": {"rating": 4.5}
          }
        }
        """.trimIndent()

        setContent {
            CompositionLocalProvider(
                LocalCustomRenderers provides mapOf(
                    "StarRating" to { node ->
                        val props = node.properties as? NodeProperties.CustomProps
                        receivedType = props?.customType
                        Text("Rating: ${props?.customData}")
                    }
                )
            ) {
                json.ToCompose()
            }
        }

        assertEquals("StarRating", receivedType)
        onNodeWithText("Rating: {\"rating\":4.5}").assertExists()
    }

    // --- Full pipeline with LazyColumn with many items ---

    @Test
    fun fullPipelineLazyColumnWithManyItems() = runComposeUiTest {
        val childrenJson = (1..50).joinToString(",") { i ->
            """{"type":"Text","properties":{"type":"TextProps","text":"Item $i"}}"""
        }
        val json = """
        {
          "type": "LazyColumn",
          "properties": {
            "type": "ColumnProps",
            "children": [$childrenJson]
          }
        }
        """.trimIndent()

        setContent {
            json.ToCompose()
        }

        // LazyColumn virtualizes, so only visible items are in the tree
        onNodeWithText("Item 1").assertExists()
        onNodeWithText("Item 2").assertExists()
    }

    // --- Roundtrip serialization ---

    @Test
    fun roundtripSerializationPreservesTree() {
        val original = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "A")
                    ),
                    ComposeNode(
                        type = ComposeType.Row,
                        properties = NodeProperties.RowProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "B")
                                ),
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "C")
                                ),
                            )
                        )
                    ),
                )
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(original.type, decoded.type)

        val originalProps = original.properties as NodeProperties.ColumnProps
        val decodedProps = decoded.properties as NodeProperties.ColumnProps
        assertEquals(originalProps.children?.size, decodedProps.children?.size)

        val originalText = originalProps.children?.get(0)?.properties as NodeProperties.TextProps
        val decodedText = decodedProps.children?.get(0)?.properties as NodeProperties.TextProps
        assertEquals(originalText.text, decodedText.text)

        val originalRow = originalProps.children?.get(1)
        val decodedRow = decodedProps.children?.get(1)
        assertEquals(originalRow?.type, decodedRow?.type)

        val originalRowProps = originalRow?.properties as NodeProperties.RowProps
        val decodedRowProps = decodedRow?.properties as NodeProperties.RowProps
        assertEquals(originalRowProps.children?.size, decodedRowProps.children?.size)

        val originalB = originalRowProps.children?.get(0)?.properties as NodeProperties.TextProps
        val decodedB = decodedRowProps.children?.get(0)?.properties as NodeProperties.TextProps
        assertEquals(originalB.text, decodedB.text)

        val originalC = originalRowProps.children?.get(1)?.properties as NodeProperties.TextProps
        val decodedC = decodedRowProps.children?.get(1)?.properties as NodeProperties.TextProps
        assertEquals(originalC.text, decodedC.text)
    }
}
