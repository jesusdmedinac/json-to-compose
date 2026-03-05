package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class NavigationRendererTest {

    @Test
    fun navigationBarRendersWithItems() = runComposeUiTest {
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
                    )
                )
            )
        )

        setContent {
            node.ToNavigationBar()
        }

        onNodeWithText("Home").assertExists()
        onNodeWithText("Search").assertExists()
    }

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

        setContent {
            node.ToTabRow()
        }

        onNodeWithText("Tab 1").assertExists()
        onNodeWithText("Tab 2").assertExists()
    }

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

        setContent {
            node.ToNavigationRail()
        }

        onNodeWithText("Rail 1").assertExists()
    }

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

        setContent {
            node.ToModalNavigationDrawer()
        }

        onNodeWithText("Drawer Item").assertExists()
        onNodeWithText("Main Content").assertExists()
    }
}
