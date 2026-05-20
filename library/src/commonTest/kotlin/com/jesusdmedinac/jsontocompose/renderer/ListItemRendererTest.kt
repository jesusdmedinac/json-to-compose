package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ListItemRendererTest {
    @Test
    fun listItemRendersAllSlotsAndClickBehavior() = runComposeUiTest {
        var clicked = false
        val behaviors = mapOf("on_item_click" to object : Behavior {
            override fun invoke() {
                clicked = true
            }
        })

        val node = ComposeNode(
            type = ComposeType.ListItem,
            properties = NodeProperties.ListItemProps(
                headlineContent = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Headline")
                ),
                supportingContent = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Supporting")
                ),
                overlineContent = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Overline")
                ),
                leadingContent = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Leading")
                ),
                trailingContent = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Trailing")
                ),
                onClickEventName = "on_item_click"
            )
        )

        setContent {
            androidx.compose.runtime.CompositionLocalProvider(LocalBehavior provides behaviors) {
                node.ToCompose()
            }
        }

        onNodeWithText("Headline").assertExists().assertIsDisplayed()
        onNodeWithText("Supporting").assertExists().assertIsDisplayed()
        onNodeWithText("Overline").assertExists().assertIsDisplayed()
        onNodeWithText("Leading").assertExists().assertIsDisplayed()
        onNodeWithText("Trailing").assertExists().assertIsDisplayed()
        
        onNodeWithTag("ListItem").performClick()
        assert(clicked) { "ListItem was not clicked" }
    }
}
