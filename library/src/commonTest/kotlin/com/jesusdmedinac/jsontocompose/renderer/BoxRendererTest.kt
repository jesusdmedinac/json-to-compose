package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class BoxRendererTest {

    @Test
    fun boxRendersOneTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Box,
            properties = NodeProperties.BoxProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Inside Box")
                    ),
                )
            )
        )

        setContent {
            node.ToBox()
        }

        onNodeWithText("Inside Box").assertExists()
    }

    @Test
    fun boxRendersWithContentAlignmentCenter() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Box,
            properties = NodeProperties.BoxProps(
                contentAlignment = "Center",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Centered")
                    ),
                )
            )
        )

        setContent {
            node.ToBox()
        }

        onNodeWithText("Centered").assertExists()
    }
}
