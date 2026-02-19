package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class RowRendererTest {

    @Test
    fun rowRendersTwoTextChildren() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Row,
            properties = NodeProperties.RowProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Left")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Right")
                    ),
                )
            )
        )

        setContent {
            node.ToRow()
        }

        onNodeWithText("Left").assertExists()
        onNodeWithText("Right").assertExists()
    }

    @Test
    fun rowRendersWithVerticalAlignmentCenterVertically() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Row,
            properties = NodeProperties.RowProps(
                verticalAlignment = "CenterVertically",
                horizontalArrangement = "SpaceEvenly",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "A")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "B")
                    ),
                )
            )
        )

        setContent {
            node.ToRow()
        }

        onNodeWithText("A").assertExists()
        onNodeWithText("B").assertExists()
    }
}
