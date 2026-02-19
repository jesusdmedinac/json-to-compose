package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class LazyColumnRendererTest {

    @Test
    fun lazyColumnRendersTenItems() = runComposeUiTest {
        val children = (1..10).map { index ->
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Item $index")
            )
        }

        val node = ComposeNode(
            type = ComposeType.LazyColumn,
            properties = NodeProperties.ColumnProps(
                children = children
            )
        )

        setContent {
            node.ToLazyColumn()
        }

        // LazyColumn only renders visible items, so we verify at least the first ones exist
        onNodeWithText("Item 1").assertExists()
        onNodeWithText("Item 2").assertExists()
        onNodeWithText("Item 3").assertExists()
    }
}
