package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.MutableStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class NavigationRendererTest {

    // --- Scenario: Render a NavigationBar with three items ---
    @Test
    fun navigationBarRendersWithThreeItems() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.NavigationBar,
            properties = NodeProperties.NavigationBarProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.NavigationBarItem,
                        properties = NodeProperties.NavigationBarItemProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Home")
                            ),
                            selected = true
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.NavigationBarItem,
                        properties = NodeProperties.NavigationBarItemProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Search")
                            ),
                            selected = false
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.NavigationBarItem,
                        properties = NodeProperties.NavigationBarItemProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Profile")
                            ),
                            selected = false
                        )
                    )
                )
            )
        )

        setContent { node.ToNavigationBar() }

        onNodeWithText("Home").assertExists()
        onNodeWithText("Search").assertExists()
        onNodeWithText("Profile").assertExists()
    }

    // --- Scenario: Render a NavigationBarItem with icon and label ---
    @Test
    fun navigationBarItemRendersWithIconAndLabel() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.NavigationBarItem,
            properties = NodeProperties.NavigationBarItemProps(
                label = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Home")
                ),
                icon = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "home_icon")
                ),
                selected = true
            )
        )

        setContent { 
            // Needs LocalRowScope for NavigationBarItem
            androidx.compose.foundation.layout.Row {
                CompositionLocalProvider(com.jesusdmedinac.jsontocompose.LocalRowScope provides this) {
                    node.ToNavigationBarItem() 
                }
            }
        }

        onRoot().printToLog("TAG")

        onNodeWithText("Home", useUnmergedTree = true).assertExists()
        onNodeWithText("home_icon", useUnmergedTree = true).assertExists()
        onNodeWithTag(ComposeType.NavigationBarItem.name).assertIsSelected()
    }

    // --- Scenario: NavigationBarItem selection controlled by state ---
    @Test
    fun navigationBarItemSelectionControlledByState() = runComposeUiTest {
        val selectedState = MutableStateHost(true)
        val node = ComposeNode(
            type = ComposeType.NavigationBarItem,
            properties = NodeProperties.NavigationBarItemProps(
                selectedStateHostName = "tabSelected"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("tabSelected" to selectedState)
            ) {
                androidx.compose.foundation.layout.Row {
                    CompositionLocalProvider(com.jesusdmedinac.jsontocompose.LocalRowScope provides this) {
                        node.ToNavigationBarItem()
                    }
                }
            }
        }

        onNodeWithTag(ComposeType.NavigationBarItem.name).assertIsSelected()

        selectedState.onStateChange(false)
        onNodeWithTag(ComposeType.NavigationBarItem.name).assertIsNotSelected()
    }

    // --- Scenario: NavigationBarItem onClick triggers action ---
    @Test
    fun navigationBarItemOnClickTriggersAction() = runComposeUiTest {
        var clicked = false
        val behavior = object : Behavior {
            override fun invoke() {
                clicked = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.NavigationBarItem,
            properties = NodeProperties.NavigationBarItemProps(
                onClickEventName = "selectTab"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("selectTab" to behavior)
            ) {
                androidx.compose.foundation.layout.Row {
                    CompositionLocalProvider(com.jesusdmedinac.jsontocompose.LocalRowScope provides this) {
                        node.ToNavigationBarItem()
                    }
                }
            }
        }

        onNodeWithTag(ComposeType.NavigationBarItem.name).performClick()
        assertTrue(clicked)
    }

    // --- Scenario: Render a NavigationRail with items ---
    @Test
    fun navigationRailRendersWithItems() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.NavigationRail,
            properties = NodeProperties.NavigationRailProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.NavigationRailItem,
                        properties = NodeProperties.NavigationRailItemProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Rail 1")
                            ),
                            selected = true
                        )
                    )
                )
            )
        )

        setContent { node.ToNavigationRail() }

        onNodeWithText("Rail 1").assertExists()
    }

    // --- Scenario: Render a NavigationRailItem with icon and label ---
    @Test
    fun navigationRailItemRendersWithIconAndLabel() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.NavigationRailItem,
            properties = NodeProperties.NavigationRailItemProps(
                label = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Settings")
                ),
                icon = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "settings_icon")
                ),
                selected = false
            )
        )

        setContent { node.ToNavigationRailItem() }

        onNodeWithText("Settings", useUnmergedTree = true).assertExists()
        onNodeWithText("settings_icon", useUnmergedTree = true).assertExists()
        onNodeWithTag(ComposeType.NavigationRailItem.name).assertIsNotSelected()
    }

    // --- Scenario: Render a NavigationRail with header FAB ---
    @Test
    fun navigationRailRendersWithHeader() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.NavigationRail,
            properties = NodeProperties.NavigationRailProps(
                header = ComposeNode(
                    type = ComposeType.Button, // Using button as FAB replacement for test
                    properties = NodeProperties.ButtonProps(
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "FAB")
                        )
                    )
                )
            )
        )

        setContent { node.ToNavigationRail() }

        onNodeWithText("FAB", useUnmergedTree = true).assertExists()
    }

    // --- Scenario: Render a ModalNavigationDrawer with content ---
    @Test
    fun modalNavigationDrawerRendersWithContent() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ModalNavigationDrawer,
            properties = NodeProperties.NavigationDrawerProps(
                isOpen = true,
                drawerContent = listOf(
                    ComposeNode(
                        type = ComposeType.NavigationDrawerItem,
                        properties = NodeProperties.NavigationDrawerItemProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Drawer Item")
                            ),
                            selected = true
                        )
                    )
                ),
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Main Content")
                )
            )
        )

        setContent { node.ToModalNavigationDrawer() }

        onNodeWithText("Drawer Item").assertExists()
        onNodeWithText("Main Content").assertExists()
    }

    // --- Scenario: ModalNavigationDrawer visibility controlled by state ---
    @Test
    fun modalNavigationDrawerVisibilityControlledByState() = runComposeUiTest {
        val openState = MutableStateHost(false)
        val node = ComposeNode(
            type = ComposeType.ModalNavigationDrawer,
            properties = NodeProperties.NavigationDrawerProps(
                isOpenStateHostName = "drawerOpen",
                drawerContent = listOf(
                    ComposeNode(
                        type = ComposeType.NavigationDrawerItem,
                        properties = NodeProperties.NavigationDrawerItemProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Drawer Item")
                            )
                        )
                    )
                ),
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Main Content")
                )
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("drawerOpen" to openState)
            ) {
                node.ToModalNavigationDrawer()
            }
        }

        // By default it's closed, so drawer item is not visible on desktop without dragging
        // but since we render modal drawer, if it's closed drawer is not shown or hidden off screen
        onNodeWithText("Drawer Item").assertIsNotDisplayed()

        // Open drawer
        openState.onStateChange(true)
        onNodeWithText("Drawer Item").assertIsDisplayed()
    }

    // --- Scenario: Render a TabRow with tabs ---
    @Test
    fun tabRowRendersWithTabs() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TabRow,
            properties = NodeProperties.TabRowProps(
                selectedTabIndex = 1,
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Tab,
                        properties = NodeProperties.TabProps(
                            text = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Tab 1")
                            ),
                            selected = false
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Tab,
                        properties = NodeProperties.TabProps(
                            text = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Tab 2")
                            ),
                            selected = true
                        )
                    )
                )
            )
        )

        setContent { node.ToTabRow() }

        onNodeWithText("Tab 1").assertExists()
        onNodeWithText("Tab 2").assertExists()
    }

    // --- Scenario: Render a Tab with text and icon ---
    @Test
    fun tabRendersWithTextAndIcon() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Tab,
            properties = NodeProperties.TabProps(
                text = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Photos")
                ),
                icon = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "photo_icon")
                ),
                selected = true
            )
        )

        setContent { node.ToTab() }

        onNodeWithText("Photos").assertExists()
        onNodeWithText("photo_icon").assertExists()
        onNodeWithTag(ComposeType.Tab.name).assertIsSelected()
    }

    // --- Scenario: Tab selection controlled by state ---
    @Test
    fun tabSelectionControlledByState() = runComposeUiTest {
        val selectedState = MutableStateHost(true)
        val node = ComposeNode(
            type = ComposeType.Tab,
            properties = NodeProperties.TabProps(
                selectedStateHostName = "activeTab"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("activeTab" to selectedState)
            ) {
                node.ToTab()
            }
        }

        onNodeWithTag(ComposeType.Tab.name).assertIsSelected()
        
        selectedState.onStateChange(false)
        onNodeWithTag(ComposeType.Tab.name).assertIsNotSelected()
    }

    // --- Scenario: Render a ScrollableTabRow with many tabs ---
    @Test
    fun scrollableTabRowRendersWithManyTabs() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ScrollableTabRow,
            properties = NodeProperties.TabRowProps(
                selectedTabIndex = 0,
                children = List(8) {
                    ComposeNode(
                        type = ComposeType.Tab,
                        properties = NodeProperties.TabProps(
                            text = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Tab $it")
                            ),
                            selected = it == 0
                        )
                    )
                }
            )
        )

        setContent { node.ToScrollableTabRow() }

        onNodeWithText("Tab 0").assertExists()
        onNodeWithText("Tab 7").assertExists()
    }

    // --- Scenario: Render NavigationBar inside Scaffold bottomBar ---
    @Test
    fun scaffoldRendersNavigationBar() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Scaffold,
            properties = NodeProperties.ScaffoldProps(
                bottomBar = ComposeNode(
                    type = ComposeType.NavigationBar,
                    properties = NodeProperties.NavigationBarProps(
                        children = listOf(
                            ComposeNode(
                                type = ComposeType.NavigationBarItem,
                                properties = NodeProperties.NavigationBarItemProps(
                                    label = ComposeNode(
                                        type = ComposeType.Text,
                                        properties = NodeProperties.TextProps(text = "BottomItem")
                                    )
                                )
                            )
                        )
                    )
                ),
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Main Content")
                )
            )
        )

        setContent { node.ToCompose() }

        onNodeWithText("BottomItem").assertExists()
    }
}
