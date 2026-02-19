package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class TextRendererTest {

    @Test
    fun textRendersWithCorrectContent() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello World")
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Hello World").assertExists()
        onNodeWithText("Hello World").assertTextEquals("Hello World")
    }

    @Test
    fun textRendersEmptyWhenTextIsNull() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = null)
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("").assertExists()
    }
}