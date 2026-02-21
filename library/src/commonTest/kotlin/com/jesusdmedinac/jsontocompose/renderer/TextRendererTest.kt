package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.state.StateHost
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

    @Test
    fun textRendersWithFontSize() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Large Text",
                fontSize = 32.0,
            )
        )

        setContent {
            node.ToText()
        }

        onNodeWithText("Large Text").assertExists()
    }

    @Test
    fun textRendersWithStateDrivenFontSize() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Dynamic Text",
                fontSizeStateHostName = "font_size_state",
            )
        )

        val mockStateHost = object : StateHost<Double> {
            override val state: Double = 40.0
            override fun onStateChange(state: Double) {}
        }

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("font_size_state" to mockStateHost)
            ) {
                node.ToText()
            }
        }

        onNodeWithText("Dynamic Text").assertExists()
    }

    @Test
    fun textRendersWithFontWeight() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Bold Text",
                fontWeight = "Bold",
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Bold Text").assertExists()
    }

    @Test
    fun textRendersWithFontStyle() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Italic Text",
                fontStyle = "Italic",
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Italic Text").assertExists()
    }

    @Test
    fun textRendersWithColor() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Colored Text",
                color = 0xFFFF0000.toInt(),
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Colored Text").assertExists()
    }

    @Test
    fun textRendersWithTextAlign() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Centered Text",
                textAlign = "Center",
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Centered Text").assertExists()
    }

    @Test
    fun textRendersWithMaxLines() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Multi-line Text",
                maxLines = 2,
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Multi-line Text").assertExists()
    }

    @Test
    fun textRendersWithOverflow() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Overflow Text",
                overflow = "Ellipsis",
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Overflow Text").assertExists()
    }

    @Test
    fun textRendersWithLetterSpacing() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Spaced Text",
                letterSpacing = 2.0,
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Spaced Text").assertExists()
    }

    @Test
    fun textRendersWithLineHeight() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Line Height Text",
                lineHeight = 30.0,
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Line Height Text").assertExists()
    }

    @Test
    fun textRendersWithTextDecoration() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Underlined Text",
                textDecoration = "Underline",
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Underlined Text").assertExists()
    }

    @Test
    fun textRendersWithMinLines() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(
                text = "Min Lines Text",
                minLines = 3,
            )
        )
        setContent { node.ToText() }
        onNodeWithText("Min Lines Text").assertExists()
    }
}
