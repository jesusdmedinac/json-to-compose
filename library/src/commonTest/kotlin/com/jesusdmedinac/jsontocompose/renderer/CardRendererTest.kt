package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
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
import kotlin.test.assertNotNull

@OptIn(ExperimentalTestApi::class)
class CardRendererTest {

    // --- Scenario 1: Render a basic Card with Text child ---

    @Test
    fun cardRendersWithTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Card,
            properties = NodeProperties.CardProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Card Content")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Card").assertExists()
        onNodeWithText("Card Content").assertIsDisplayed()
    }

    // --- Scenario 2: Render a Card with custom elevation ---

    @Test
    fun cardRendersWithCustomElevation() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Card,
            properties = NodeProperties.CardProps(
                elevation = 8,
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Elevated Card")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Card").assertExists()
        onNodeWithTag("Card").assertIsDisplayed()
        onNodeWithText("Elevated Card").assertIsDisplayed()
    }

    // --- Scenario 3: Render a Card with rounded shape ---

    @Test
    fun cardRendersWithRoundedCorners() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Card,
            properties = NodeProperties.CardProps(
                cornerRadius = 16,
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Rounded Card")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Card").assertExists()
        onNodeWithTag("Card").assertIsDisplayed()
        onNodeWithText("Rounded Card").assertIsDisplayed()
    }

    // --- Scenario 4: Render a Card with multiple children in Column ---

    @Test
    fun cardRendersWithColumnChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Card,
            properties = NodeProperties.CardProps(
                child = ComposeNode(
                    type = ComposeType.Column,
                    properties = NodeProperties.ColumnProps(
                        children = listOf(
                            ComposeNode(
                                type = ComposeType.Image,
                                properties = NodeProperties.ImageProps(
                                    url = "https://example.com/image.png",
                                    contentDescription = "Card image"
                                )
                            ),
                            ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Card Title")
                            ),
                        )
                    )
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Card").assertExists()
        onNodeWithTag("Column").assertExists()
        onNodeWithText("Card Title").assertIsDisplayed()
    }

    // --- Scenario 5: Serialize and deserialize a Card from JSON ---

    @Test
    fun cardSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.Card,
            properties = NodeProperties.CardProps(
                elevation = 4,
                cornerRadius = 12,
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Serialized")
                )
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.Card, decoded.type)
        val decodedProps = decoded.properties as? NodeProperties.CardProps
        assertNotNull(decodedProps)
        assertEquals(4, decodedProps.elevation)
        assertEquals(12, decodedProps.cornerRadius)

        val childProps = decodedProps.child?.properties as? NodeProperties.TextProps
        assertNotNull(childProps)
        assertEquals("Serialized", childProps.text)
    }
}
