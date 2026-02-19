package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class LazyRowRendererTest {

    @Test
    fun lazyRowRendersFiveItems() = runComposeUiTest {
        val children = (1..5).map { index ->
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Element $index")
            )
        }

        val node = ComposeNode(
            type = ComposeType.LazyRow,
            properties = NodeProperties.RowProps(
                children = children
            )
        )

        setContent {
            node.ToLazyRow()
        }

        // LazyRow only renders visible items, so we verify at least the first ones exist
        onNodeWithText("Element 1").assertExists()
        onNodeWithText("Element 2").assertExists()
        onNodeWithText("Element 3").assertExists()
    }
}
