package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FlowLayoutRendererTest {

    @Test
    fun renderAFlowRowWithChildren() = runComposeUiTest {
        // Given a JSON with a FlowRow containing 2 children
        val node = ComposeNode(
            type = ComposeType.FlowRow,
            properties = NodeProperties.FlowRowProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Child 1")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Child 2")
                    )
                )
            )
        )

        // When the node is processed by the renderer
        setContent {
            node.ToFlowRow()
        }

        // Then children are rendered
        onNodeWithTag("FlowRow").assertExists()
        onNodeWithText("Child 1").assertExists()
        onNodeWithText("Child 2").assertExists()
    }

    @Test
    fun renderAFlowColumnWithChildren() = runComposeUiTest {
        // Given a JSON with a FlowColumn containing 2 children
        val node = ComposeNode(
            type = ComposeType.FlowColumn,
            properties = NodeProperties.FlowColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Child A")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Child B")
                    )
                )
            )
        )

        // When the node is processed by the renderer
        setContent {
            node.ToFlowColumn()
        }

        // Then children are rendered
        onNodeWithTag("FlowColumn").assertExists()
        onNodeWithText("Child A").assertExists()
        onNodeWithText("Child B").assertExists()
    }
}
