package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class HorizontalPagerRendererTest {

    @Test
    fun rendersHorizontalPagerWithPages() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.HorizontalPager,
            properties = NodeProperties.PagerProps(
                pages = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 1")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 2")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 3")
                    )
                )
            )
        )

        setContent {
            node.ToHorizontalPager()
        }

        onNodeWithTag("HorizontalPager").assertExists()
        onNodeWithText("Page 1").assertExists()
    }

    @Test
    fun horizontalPagerCurrentPageControlledByState() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.HorizontalPager,
            properties = NodeProperties.PagerProps(
                currentPageStateHostName = "currentPage",
                pages = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 1")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 2")
                    )
                )
            )
        )

        val pageStateHost = object : StateHost<Int> {
            override val state: Int = 1 // Start at page 2
            override fun onStateChange(state: Int) {}
        }

        setContent {
            CompositionLocalProvider(LocalStateHost provides mapOf("currentPage" to pageStateHost)) {
                node.ToHorizontalPager()
            }
        }

        onNodeWithText("Page 2").assertExists()
    }

    @Test
    fun horizontalPagerPageChangeUpdatesState() = runComposeUiTest {
        var lastReportedPage = 0
        val pageStateHost = object : StateHost<Int> {
            override val state: Int = 0
            override fun onStateChange(state: Int) {
                lastReportedPage = state
            }
        }

        val node = ComposeNode(
            type = ComposeType.HorizontalPager,
            properties = NodeProperties.PagerProps(
                currentPageStateHostName = "currentPage",
                pages = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 1")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 2")
                    )
                )
            )
        )

        setContent {
            CompositionLocalProvider(LocalStateHost provides mapOf("currentPage" to pageStateHost)) {
                node.ToHorizontalPager()
            }
        }

        onNodeWithTag("HorizontalPager").performTouchInput { swipeLeft() }
        
        waitForIdle()
        assertEquals(1, lastReportedPage)
    }

    @Test
    fun horizontalPagerWithBeyondViewportPageCount() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.HorizontalPager,
            properties = NodeProperties.PagerProps(
                beyondViewportPageCount = 1,
                pages = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 1")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 2")
                    )
                )
            )
        )

        setContent {
            node.ToHorizontalPager()
        }

        // Both pages should exist in the composition because of beyondViewportPageCount = 1
        onNodeWithText("Page 1").assertExists()
        onNodeWithText("Page 2").assertExists()
    }

    @Test
    fun horizontalPagerWithUserScrollEnabledFalse() = runComposeUiTest {
        var lastReportedPage = 0
        val pageStateHost = object : StateHost<Int> {
            override val state: Int = 0
            override fun onStateChange(state: Int) {
                lastReportedPage = state
            }
        }

        val node = ComposeNode(
            type = ComposeType.HorizontalPager,
            properties = NodeProperties.PagerProps(
                currentPageStateHostName = "currentPage",
                userScrollEnabled = false,
                pages = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 1")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 2")
                    )
                )
            )
        )

        setContent {
            CompositionLocalProvider(LocalStateHost provides mapOf("currentPage" to pageStateHost)) {
                node.ToHorizontalPager()
            }
        }

        onNodeWithTag("HorizontalPager").performTouchInput { swipeLeft() }
        waitForIdle()
        
        // State should not change because scrolling is disabled
        assertEquals(0, lastReportedPage)
        onNodeWithText("Page 1").assertExists()
    }
}
