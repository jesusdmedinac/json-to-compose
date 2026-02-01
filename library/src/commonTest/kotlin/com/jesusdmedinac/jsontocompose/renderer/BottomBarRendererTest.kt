package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalTestApi::class)
class BottomBarRendererTest {

    // --- Scenario 1: Render a BottomBar with children ---

    @Test
    fun bottomBarRendersWithChildren() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.BottomBar,
            properties = NodeProperties.BottomBarProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Home"),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Search"),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Profile"),
                    ),
                ),
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("BottomBar").assertIsDisplayed()
        onNodeWithText("Home").assertIsDisplayed()
        onNodeWithText("Search").assertIsDisplayed()
        onNodeWithText("Profile").assertIsDisplayed()
    }

    // --- Scenario 2: Render a BottomBar with selected item ---

    @Test
    fun bottomBarRendersWithSelectedItem() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.BottomBar,
            properties = NodeProperties.BottomBarProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Home"),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Search"),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Profile"),
                    ),
                ),
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Home").assertIsDisplayed()
        onNodeWithText("Search").assertIsDisplayed()
        onNodeWithText("Profile").assertIsDisplayed()
    }

    // --- Scenario 3: BottomBar integrated with Scaffold ---

    @Test
    fun bottomBarIntegratedWithScaffold() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Scaffold,
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.FillMaxWidth,
                    ComposeModifier.Operation.Height(200),
                )
            ),
            properties = NodeProperties.ScaffoldProps(
                bottomBar = ComposeNode(
                    type = ComposeType.BottomBar,
                    properties = NodeProperties.BottomBarProps(
                        children = listOf(
                            ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Home"),
                            ),
                            ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Settings"),
                            ),
                        ),
                    )
                ),
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Body Content"),
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Home").assertIsDisplayed()
        onNodeWithText("Body Content").assertIsDisplayed()
    }

    // --- Scenario 4: Serialize and deserialize a BottomBar from JSON ---

    @Test
    fun bottomBarSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.BottomBar,
            properties = NodeProperties.BottomBarProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Home"),
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Profile"),
                    ),
                ),
                backgroundColor = 0xFF6200EE.toInt(),
                contentColor = 0xFFFFFFFF.toInt(),
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.BottomBar, decoded.type)
        val props = decoded.properties as? NodeProperties.BottomBarProps
        assertNotNull(props)
        assertNotNull(props.children)
        assertEquals(2, props.children?.size)
        assertEquals(ComposeType.Text, props.children?.get(0)?.type)
        assertEquals(ComposeType.Text, props.children?.get(1)?.type)
        assertEquals(0xFF6200EE.toInt(), props.backgroundColor)
        assertEquals(0xFFFFFFFF.toInt(), props.contentColor)
    }
}
