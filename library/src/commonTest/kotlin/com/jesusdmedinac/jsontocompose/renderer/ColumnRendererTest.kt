package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ColumnRendererTest {

    @Test
    fun columnRendersThreeTextChildren() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Child 1")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Child 2")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Child 3")
                    ),
                )
            )
        )

        setContent {
            node.ToColumn()
        }

        onNodeWithText("Child 1").assertExists()
        onNodeWithText("Child 2").assertExists()
        onNodeWithText("Child 3").assertExists()
    }

    @Test
    fun columnRendersWithVerticalArrangementSpaceBetween() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                verticalArrangement = "SpaceBetween",
                horizontalAlignment = "CenterHorizontally",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Top")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Bottom")
                    ),
                )
            )
        )

        setContent {
            node.ToColumn()
        }

        onNodeWithText("Top").assertExists()
        onNodeWithText("Bottom").assertExists()
    }
}
