package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class VerticalPagerRendererTest {

    @Test
    fun rendersVerticalPagerWithPages() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.VerticalPager,
            properties = NodeProperties.PagerProps(
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
            node.ToVerticalPager()
        }

        onNodeWithTag("VerticalPager").assertExists()
        onNodeWithText("Page 1").assertExists()
    }

    @Test
    fun verticalPagerCurrentPageControlledByState() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.VerticalPager,
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
                node.ToVerticalPager()
            }
        }

        onNodeWithText("Page 2").assertExists()
    }
}
