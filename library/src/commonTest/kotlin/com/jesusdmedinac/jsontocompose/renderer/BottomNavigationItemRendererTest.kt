package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class BottomNavigationItemRendererTest {

    private fun bottomBarWith(vararg items: ComposeNode) = ComposeNode(
        type = ComposeType.BottomBar,
        properties = NodeProperties.BottomBarProps(
            children = items.toList(),
        )
    )

    private fun navItem(
        label: String? = null,
        icon: String? = null,
        selected: Boolean? = null,
        onClickEventName: String? = null,
    ) = ComposeNode(
        type = ComposeType.BottomNavigationItem,
        properties = NodeProperties.BottomNavigationItemProps(
            selected = selected,
            onClickEventName = onClickEventName,
            label = label?.let {
                ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = it),
                )
            },
            icon = icon?.let {
                ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = it),
                )
            },
        )
    )

    // --- Scenario 1: Render a BottomNavigationItem with label and icon ---

    @Test
    fun bottomNavigationItemRendersWithLabelAndIcon() = runComposeUiTest {
        val node = bottomBarWith(
            navItem(label = "Home", icon = "H"),
            navItem(label = "Search", icon = "S"),
            navItem(label = "Profile", icon = "P"),
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Home").assertIsDisplayed()
        onNodeWithText("Search").assertIsDisplayed()
        onNodeWithText("Profile").assertIsDisplayed()
    }

    // --- Scenario 2: Render a BottomNavigationItem as selected ---

    @Test
    fun bottomNavigationItemRendersAsSelected() = runComposeUiTest {
        val node = bottomBarWith(
            navItem(label = "Home", icon = "H", selected = false),
            navItem(label = "Search", icon = "S", selected = true),
            navItem(label = "Profile", icon = "P", selected = false),
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Home").assertIsDisplayed()
        onNodeWithText("Search").assertIsDisplayed()
        onNodeWithText("Profile").assertIsDisplayed()
    }

    // --- Scenario 3: BottomNavigationItem emits event when clicked ---

    @Test
    fun bottomNavigationItemEmitsEventWhenClicked() = runComposeUiTest {
        var invoked = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                invoked = true
            }
        }

        val node = bottomBarWith(
            navItem(label = "Home", icon = "H", onClickEventName = "home_click"),
            navItem(label = "Profile", icon = "P", onClickEventName = "profile_click"),
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

    // --- Scenario 4: BottomNavigationItem integrated with BottomBar ---

    @Test
    fun bottomNavigationItemIntegratedWithBottomBar() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Scaffold,
            properties = NodeProperties.ScaffoldProps(
                bottomBar = bottomBarWith(
                    navItem(label = "Home", icon = "H", selected = true),
                    navItem(label = "Settings", icon = "S", selected = false),
                ),
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Main Content"),
                ),
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Home").assertIsDisplayed()
        onNodeWithText("Settings").assertIsDisplayed()
        onNodeWithText("Main Content").assertIsDisplayed()
    }

    // --- Scenario 5: Serialize and deserialize a BottomNavigationItem from JSON ---

    @Test
    fun bottomNavigationItemSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.BottomNavigationItem,
            properties = NodeProperties.BottomNavigationItemProps(
                selected = true,
                onClickEventName = "nav_home",
                label = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Home"),
                ),
                icon = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "H"),
                ),
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.BottomNavigationItem, decoded.type)
        val props = decoded.properties as? NodeProperties.BottomNavigationItemProps
        assertNotNull(props)
        assertEquals(true, props.selected)
        assertEquals("nav_home", props.onClickEventName)
        assertNotNull(props.label)
        assertEquals(ComposeType.Text, props.label?.type)
        assertNotNull(props.icon)
        assertEquals(ComposeType.Text, props.icon?.type)
    }
}
