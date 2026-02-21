package com.jesusdmedinac.jsontocompose.pipeline

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ButtonVariantsPipelineTest {

    @Test
    fun fullPipelineOutlinedButton() = runComposeUiTest {
        val json = """
        {
          "type": "OutlinedButton",
          "properties": {
            "type": "ButtonProps",
            "child": {"type": "Text", "properties": {"type": "TextProps", "text": "Outlined"}}
          }
        }
        """.trimIndent()

        setContent {
            json.ToCompose()
        }

        onNodeWithTag("OutlinedButton").assertExists()
        onNodeWithText("Outlined").assertExists()
    }

    @Test
    fun fullPipelineIconButton() = runComposeUiTest {
        val json = """
        {
          "type": "IconButton",
          "properties": {
            "type": "ButtonProps",
            "child": {"type": "Icon", "properties": {"type": "IconProps", "iconName": "search"}}
          }
        }
        """.trimIndent()

        setContent {
            json.ToCompose()
        }

        onNodeWithTag("IconButton").assertExists()
        onNodeWithText("search").assertExists()
    }

    @Test
    fun fullPipelineExtendedFAB() = runComposeUiTest {
        val json = """
        {
          "type": "ExtendedFloatingActionButton",
          "properties": {
            "type": "ExtendedFabProps",
            "icon": {"type": "Icon", "properties": {"type": "IconProps", "iconName": "add"}},
            "text": {"type": "Text", "properties": {"type": "TextProps", "text": "Create"}}
          }
        }
        """.trimIndent()

        setContent {
            androidx.compose.material3.MaterialTheme {
                json.ToCompose()
            }
        }

        onNodeWithTag("ExtendedFloatingActionButton").assertExists()
        onNodeWithText("add").assertExists()
        onNodeWithText("Create").assertExists()
    }

    @Test
    fun roundtripSerializationButtonVariants() {
        val types = listOf(
            ComposeType.OutlinedButton,
            ComposeType.TextButton,
            ComposeType.ElevatedButton,
            ComposeType.FilledTonalButton
        )

        types.forEach { type ->
            val original = ComposeNode(
                type = type,
                properties = NodeProperties.ButtonProps(
                    enabled = false,
                    child = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Label")
                    )
                )
            )

            val json = Json.encodeToString(original)
            val decoded = Json.decodeFromString<ComposeNode>(json)

            assertEquals(original.type, decoded.type)
            val originalProps = original.properties as NodeProperties.ButtonProps
            val decodedProps = decoded.properties as NodeProperties.ButtonProps
            assertEquals(originalProps.enabled, decodedProps.enabled)
            assertEquals(
                (originalProps.child?.properties as NodeProperties.TextProps).text,
                (decodedProps.child?.properties as NodeProperties.TextProps).text
            )
        }
    }

    @Test
    fun roundtripSerializationFAB() {
        val original = ComposeNode(
            type = ComposeType.FloatingActionButton,
            properties = NodeProperties.FabProps(
                containerColor = 0xFFFF0000.toInt(),
                icon = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "add")
                )
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(original.type, decoded.type)
        val originalProps = original.properties as NodeProperties.FabProps
        val decodedProps = decoded.properties as NodeProperties.FabProps
        assertEquals(originalProps.containerColor, decodedProps.containerColor)
        assertEquals(
            (originalProps.icon?.properties as NodeProperties.IconProps).iconName,
            (decodedProps.icon?.properties as NodeProperties.IconProps).iconName
        )
    }

    @Test
    fun roundtripSerializationExtendedFAB() {
        val original = ComposeNode(
            type = ComposeType.ExtendedFloatingActionButton,
            properties = NodeProperties.ExtendedFabProps(
                containerColor = 0xFF00FF00.toInt(),
                icon = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "add")
                ),
                text = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Label")
                )
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(original.type, decoded.type)
        val originalProps = original.properties as NodeProperties.ExtendedFabProps
        val decodedProps = decoded.properties as NodeProperties.ExtendedFabProps
        assertEquals(originalProps.containerColor, decodedProps.containerColor)
        assertEquals(
            (originalProps.icon?.properties as NodeProperties.IconProps).iconName,
            (decodedProps.icon?.properties as NodeProperties.IconProps).iconName
        )
        assertEquals(
            (originalProps.text?.properties as NodeProperties.TextProps).text,
            (decodedProps.text?.properties as NodeProperties.TextProps).text
        )
    }
}
