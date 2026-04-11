package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

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
}
