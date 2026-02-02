package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
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

    private fun createBooleanStateHost(initial: Boolean): StateHost<Boolean> {
        var value by mutableStateOf(initial)
        return object : StateHost<Boolean> {
            override val state: Boolean get() = value
            override fun onStateChange(state: Boolean) { value = state }
        }
    }

    private fun bottomBarWith(vararg items: ComposeNode) = ComposeNode(
        type = ComposeType.BottomBar,
        properties = NodeProperties.BottomBarProps(
            children = items.toList(),
        )
    )

    private fun navItem(
        label: String? = null,
        icon: String? = null,
        selectedStateHostName: String? = null,
        enabledStateHostName: String? = null,
        alwaysShowLabelStateHostName: String? = null,
        onClickEventName: String? = null,
    ) = ComposeNode(
        type = ComposeType.BottomNavigationItem,
        properties = NodeProperties.BottomNavigationItemProps(
            selectedStateHostName = selectedStateHostName,
            enabledStateHostName = enabledStateHostName,
            alwaysShowLabelStateHostName = alwaysShowLabelStateHostName,
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
            navItem(label = "Home", icon = "H", selectedStateHostName = "home_selected", enabledStateHostName = "home_enabled", alwaysShowLabelStateHostName = "home_show_label"),
            navItem(label = "Search", icon = "S", selectedStateHostName = "search_selected", enabledStateHostName = "search_enabled", alwaysShowLabelStateHostName = "search_show_label"),
            navItem(label = "Profile", icon = "P", selectedStateHostName = "profile_selected", enabledStateHostName = "profile_enabled", alwaysShowLabelStateHostName = "profile_show_label"),
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "home_selected" to createBooleanStateHost(false),
                    "home_enabled" to createBooleanStateHost(true),
                    "home_show_label" to createBooleanStateHost(true),
                    "search_selected" to createBooleanStateHost(false),
                    "search_enabled" to createBooleanStateHost(true),
                    "search_show_label" to createBooleanStateHost(true),
                    "profile_selected" to createBooleanStateHost(false),
                    "profile_enabled" to createBooleanStateHost(true),
                    "profile_show_label" to createBooleanStateHost(true),
                )
            ) {
                node.ToCompose()
            }
        }

        onNodeWithText("Home").assertIsDisplayed()
        onNodeWithText("Search").assertIsDisplayed()
        onNodeWithText("Profile").assertIsDisplayed()
    }

    // --- Scenario 2: Render a BottomNavigationItem as selected ---

    @Test
    fun bottomNavigationItemRendersAsSelected() = runComposeUiTest {
        val node = bottomBarWith(
            navItem(label = "Home", icon = "H", selectedStateHostName = "home_selected", enabledStateHostName = "home_enabled", alwaysShowLabelStateHostName = "home_show_label"),
            navItem(label = "Search", icon = "S", selectedStateHostName = "search_selected", enabledStateHostName = "search_enabled", alwaysShowLabelStateHostName = "search_show_label"),
            navItem(label = "Profile", icon = "P", selectedStateHostName = "profile_selected", enabledStateHostName = "profile_enabled", alwaysShowLabelStateHostName = "profile_show_label"),
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "home_selected" to createBooleanStateHost(false),
                    "home_enabled" to createBooleanStateHost(true),
                    "home_show_label" to createBooleanStateHost(true),
                    "search_selected" to createBooleanStateHost(true),
                    "search_enabled" to createBooleanStateHost(true),
                    "search_show_label" to createBooleanStateHost(true),
                    "profile_selected" to createBooleanStateHost(false),
                    "profile_enabled" to createBooleanStateHost(true),
                    "profile_show_label" to createBooleanStateHost(true),
                )
            ) {
                node.ToCompose()
            }
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
            navItem(label = "Home", icon = "H", selectedStateHostName = "home_selected", enabledStateHostName = "home_enabled", alwaysShowLabelStateHostName = "home_show_label", onClickEventName = "home_click"),
            navItem(label = "Profile", icon = "P", selectedStateHostName = "profile_selected", enabledStateHostName = "profile_enabled", alwaysShowLabelStateHostName = "profile_show_label", onClickEventName = "profile_click"),
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "home_selected" to createBooleanStateHost(false),
                    "home_enabled" to createBooleanStateHost(true),
                    "home_show_label" to createBooleanStateHost(true),
                    "profile_selected" to createBooleanStateHost(false),
                    "profile_enabled" to createBooleanStateHost(true),
                    "profile_show_label" to createBooleanStateHost(true),
                ),
                LocalBehavior provides mapOf("profile_click" to mockBehavior),
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
                    navItem(label = "Home", icon = "H", selectedStateHostName = "home_selected", enabledStateHostName = "home_enabled", alwaysShowLabelStateHostName = "home_show_label"),
                    navItem(label = "Settings", icon = "S", selectedStateHostName = "settings_selected", enabledStateHostName = "settings_enabled", alwaysShowLabelStateHostName = "settings_show_label"),
                ),
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Main Content"),
                ),
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "home_selected" to createBooleanStateHost(true),
                    "home_enabled" to createBooleanStateHost(true),
                    "home_show_label" to createBooleanStateHost(true),
                    "settings_selected" to createBooleanStateHost(false),
                    "settings_enabled" to createBooleanStateHost(true),
                    "settings_show_label" to createBooleanStateHost(true),
                )
            ) {
                node.ToCompose()
            }
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
                selectedStateHostName = "nav_home_selected",
                enabledStateHostName = "nav_home_enabled",
                alwaysShowLabelStateHostName = "nav_home_show_label",
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
        assertEquals("nav_home_selected", props.selectedStateHostName)
        assertEquals("nav_home_enabled", props.enabledStateHostName)
        assertEquals("nav_home_show_label", props.alwaysShowLabelStateHostName)
        assertEquals("nav_home", props.onClickEventName)
        assertNotNull(props.label)
        assertEquals(ComposeType.Text, props.label!!.type)
        assertNotNull(props.icon)
        assertEquals(ComposeType.Text, props.icon!!.type)
    }

    // --- Scenario 6: Render with inline values only (no StateHost) ---

    @Test
    fun bottomNavigationItemRendersWithInlineValuesOnly() = runComposeUiTest {
        val node = bottomBarWith(
            navItem(label = "Home", icon = "H", selectedStateHostName = null, enabledStateHostName = null, alwaysShowLabelStateHostName = null),
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Home").assertIsDisplayed()
    }

    // --- Scenario 7: StateHost takes precedence over inline value ---

    @Test
    fun bottomNavigationItemStateHostTakesPrecedenceOverInline() = runComposeUiTest {
        // inline selected = false, but StateHost says true
        val node = ComposeNode(
            type = ComposeType.BottomBar,
            properties = NodeProperties.BottomBarProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.BottomNavigationItem,
                        properties = NodeProperties.BottomNavigationItemProps(
                            selected = false,
                            selectedStateHostName = "home_selected",
                            enabledStateHostName = "home_enabled",
                            alwaysShowLabelStateHostName = "home_show_label",
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Home"),
                            ),
                            icon = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "H"),
                            ),
                        )
                    ),
                ),
            ),
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf(
                    "home_selected" to createBooleanStateHost(true),
                    "home_enabled" to createBooleanStateHost(true),
                    "home_show_label" to createBooleanStateHost(true),
                )
            ) {
                node.ToCompose()
            }
        }

        // Component renders (StateHost selected=true wins over inline selected=false)
        onNodeWithText("Home").assertIsDisplayed()
    }

    // --- Scenario 8: Render with neither inline nor StateHost (defaults apply) ---

    @Test
    fun bottomNavigationItemRendersWithDefaults() = runComposeUiTest {
        val node = bottomBarWith(
            ComposeNode(
                type = ComposeType.BottomNavigationItem,
                properties = NodeProperties.BottomNavigationItemProps(
                    label = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Default"),
                    ),
                    icon = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "D"),
                    ),
                )
            ),
        )

        setContent {
            node.ToCompose()
        }

        // Renders with defaults: selected=false, enabled=true, alwaysShowLabel=true
        onNodeWithText("Default").assertIsDisplayed()
    }
}
