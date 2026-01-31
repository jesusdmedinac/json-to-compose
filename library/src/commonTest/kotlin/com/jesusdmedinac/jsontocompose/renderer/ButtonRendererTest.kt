package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class ButtonRendererTest {

    @Test
    fun buttonRendersWithTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Button,
            properties = NodeProperties.ButtonProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Press me")
                )
            )
        )

        setContent {
            node.ToButton()
        }

        onNodeWithText("Press me").assertExists()
    }

    @Test
    fun buttonEmitsClickEvent() = runComposeUiTest {
        var clicked = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                clicked = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.Button,
            properties = NodeProperties.ButtonProps(
                onClickEventName = "submit",
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Submit")
                )
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("submit" to mockBehavior)
            ) {
                node.ToButton()
            }
        }

        onNodeWithText("Submit").performClick()
        assertTrue(clicked, "Behavior.onClick should have been invoked")
    }
}
