package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.performClick
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class BottomBarRendererTest {

    // --- Scenario 1: Render a BottomBar with navigation items ---

    @Test
    fun bottomBarRendersWithNavigationItems() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.BottomBar,
            properties = NodeProperties.BottomBarProps(
                items = listOf(
                    NodeProperties.BottomBarItem(label = "Home"),
                    NodeProperties.BottomBarItem(label = "Search"),
                    NodeProperties.BottomBarItem(label = "Profile"),
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

    // --- Scenario 2: Render a BottomBar with selected item ---

    @Test
    fun bottomBarRendersWithSelectedItem() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.BottomBar,
            properties = NodeProperties.BottomBarProps(
                items = listOf(
                    NodeProperties.BottomBarItem(label = "Home"),
                    NodeProperties.BottomBarItem(label = "Search"),
                    NodeProperties.BottomBarItem(label = "Profile"),
                ),
                selectedIndex = 1,
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Home").assertIsDisplayed()
        onNodeWithText("Search").assertIsDisplayed()
        onNodeWithText("Profile").assertIsDisplayed()
    }

    // --- Scenario 3: BottomBar emits event when item is selected ---

    @Test
    fun bottomBarEmitsEventWhenItemSelected() = runComposeUiTest {
        var invoked = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                invoked = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.BottomBar,
            properties = NodeProperties.BottomBarProps(
                items = listOf(
                    NodeProperties.BottomBarItem(label = "Home", eventName = "home_click"),
                    NodeProperties.BottomBarItem(label = "Search", eventName = "search_click"),
                    NodeProperties.BottomBarItem(label = "Profile", eventName = "profile_click"),
                ),
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("profile_click" to mockBehavior)
            ) {
                node.ToCompose()
            }
        }

        onNodeWithText("Profile").performClick()
        assertTrue(invoked, "Behavior for profile_click should have been invoked")
    }

    // --- Scenario 4: BottomBar integrated with Scaffold ---

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
                        items = listOf(
                            NodeProperties.BottomBarItem(label = "Home"),
                            NodeProperties.BottomBarItem(label = "Settings"),
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

    // --- Scenario 5: Serialize and deserialize a BottomBar from JSON ---

    @Test
    fun bottomBarSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.BottomBar,
            properties = NodeProperties.BottomBarProps(
                items = listOf(
                    NodeProperties.BottomBarItem(
                        label = "Home",
                        iconName = "home",
                        eventName = "home_click",
                    ),
                    NodeProperties.BottomBarItem(
                        label = "Profile",
                        iconName = "person",
                        eventName = "profile_click",
                    ),
                ),
                selectedIndex = 0,
                backgroundColor = 0xFF6200EE.toInt(),
                contentColor = 0xFFFFFFFF.toInt(),
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.BottomBar, decoded.type)
        val props = decoded.properties as? NodeProperties.BottomBarProps
        assertNotNull(props)
        assertNotNull(props.items)
        assertEquals(2, props.items?.size)
        assertEquals("Home", props.items?.get(0)?.label)
        assertEquals("home", props.items?.get(0)?.iconName)
        assertEquals("home_click", props.items?.get(0)?.eventName)
        assertEquals("Profile", props.items?.get(1)?.label)
        assertEquals(0, props.selectedIndex)
        assertEquals(0xFF6200EE.toInt(), props.backgroundColor)
        assertEquals(0xFFFFFFFF.toInt(), props.contentColor)
    }
}
