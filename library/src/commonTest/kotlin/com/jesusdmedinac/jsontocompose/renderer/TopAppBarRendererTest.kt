package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
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
class TopAppBarRendererTest {

    // --- Scenario 1: Renders with title text ---

    @Test
    fun topAppBarRendersWithTitle() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TopAppBar,
            properties = NodeProperties.TopAppBarProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "My App")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("My App").assertIsDisplayed()
    }

    // --- Scenario 2: Renders with navigation icon ---

    @Test
    fun topAppBarRendersWithNavigationIcon() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TopAppBar,
            properties = NodeProperties.TopAppBarProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Title")
                ),
                navigationIcon = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Back")
                        )
                    )
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Title").assertIsDisplayed()
        onNodeWithText("Back").assertIsDisplayed()
    }

    // --- Scenario 3: Renders with action buttons ---

    @Test
    fun topAppBarRendersWithActions() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TopAppBar,
            properties = NodeProperties.TopAppBarProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Home")
                ),
                actions = listOf(
                    ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Search")
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Settings")
                            )
                        )
                    )
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Home").assertIsDisplayed()
        onNodeWithText("Search").assertIsDisplayed()
        onNodeWithText("Settings").assertIsDisplayed()
    }

    // --- Scenario 4: Serialization round-trip ---

    @Test
    fun topAppBarSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.TopAppBar,
            properties = NodeProperties.TopAppBarProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "My App")
                ),
                navigationIcon = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Back")
                        )
                    )
                ),
                actions = listOf(
                    ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Search")
                            )
                        )
                    )
                ),
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.TopAppBar, decoded.type)
        val props = decoded.properties as? NodeProperties.TopAppBarProps
        assertNotNull(props)
        assertNotNull(props.title)
        assertEquals(ComposeType.Text, props.title?.type)
        assertNotNull(props.navigationIcon)
        assertEquals(ComposeType.Button, props.navigationIcon?.type)
        assertNotNull(props.actions)
        assertEquals(1, props.actions?.size)
        assertEquals(ComposeType.Button, props.actions?.first()?.type)
    }
}
