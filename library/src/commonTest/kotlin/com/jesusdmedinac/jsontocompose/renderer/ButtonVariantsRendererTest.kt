package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.onNodeWithTag
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
class ButtonVariantsRendererTest {

    @Test
    fun outlinedButtonRendersWithTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.OutlinedButton,
            properties = NodeProperties.ButtonProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Cancel")
                )
            )
        )

        setContent {
            node.ToButton()
        }

        onNodeWithText("Cancel").assertExists()
        onNodeWithTag("OutlinedButton").assertExists()
    }

    @Test
    fun textButtonRendersWithTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TextButton,
            properties = NodeProperties.ButtonProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Learn more")
                )
            )
        )

        setContent {
            node.ToButton()
        }

        onNodeWithText("Learn more").assertExists()
        onNodeWithTag("TextButton").assertExists()
    }

    @Test
    fun elevatedButtonRendersWithTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ElevatedButton,
            properties = NodeProperties.ButtonProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Submit")
                )
            )
        )

        setContent {
            node.ToButton()
        }

        onNodeWithText("Submit").assertExists()
        onNodeWithTag("ElevatedButton").assertExists()
    }

    @Test
    fun filledTonalButtonRendersWithTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.FilledTonalButton,
            properties = NodeProperties.ButtonProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Save")
                )
            )
        )

        setContent {
            node.ToButton()
        }

        onNodeWithText("Save").assertExists()
        onNodeWithTag("FilledTonalButton").assertExists()
    }

    @Test
    fun iconButtonRendersWithIconChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.IconButton,
            properties = NodeProperties.ButtonProps(
                child = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "favorite")
                )
            )
        )

        setContent {
            node.ToIconButton()
        }

        onNodeWithTag("IconButton").assertExists()
        onNodeWithText("favorite").assertExists() // Since we use Text as fallback in ToIcon
    }

    @Test
    fun floatingActionButtonRendersWithIcon() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.FloatingActionButton,
            properties = NodeProperties.FabProps(
                icon = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "add")
                )
            )
        )

        setContent {
            node.ToFloatingActionButton()
        }

        onNodeWithTag("FloatingActionButton").assertExists()
        onNodeWithText("add").assertExists()
    }

    @Test
    fun extendedFloatingActionButtonRendersWithIconAndText() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ExtendedFloatingActionButton,
            properties = NodeProperties.ExtendedFabProps(
                icon = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "add")
                ),
                text = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Create")
                )
            )
        )

        setContent {
            androidx.compose.material3.MaterialTheme {
                node.ToExtendedFloatingActionButton()
            }
        }

        onNodeWithTag("ExtendedFloatingActionButton").assertExists()
        onNodeWithText("Create").assertExists()
        onNodeWithText("add").assertExists()
    }

    @Test
    fun floatingActionButtonWithCustomContainerColor() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.FloatingActionButton,
            properties = NodeProperties.FabProps(
                containerColor = 0xFFFF0000.toInt(), // Red
                icon = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "add")
                )
            )
        )

        setContent {
            node.ToFloatingActionButton()
        }

        onNodeWithTag("FloatingActionButton").assertExists()
        onNodeWithText("add").assertExists()
    }

    @Test
    fun buttonWithEnabledFalseIsDisabled() = runComposeUiTest {
        var clicked = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                clicked = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.Button,
            properties = NodeProperties.ButtonProps(
                onClickEventName = "click",
                enabled = false,
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Disabled")
                )
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("click" to mockBehavior)
            ) {
                node.ToButton()
            }
        }

        onNodeWithText("Disabled").assertExists()
        // In some environments, we can't easily assert "is not enabled" without custom matchers,
        // but we can try to click it and see if the behavior is invoked.
        onNodeWithText("Disabled").performClick()
        assertTrue(!clicked, "Behavior should not be invoked when button is disabled")
    }

    @Test
    fun buttonWithStateDrivenEnabledProperty() = runComposeUiTest {
        var clicked = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                clicked = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.Button,
            properties = NodeProperties.ButtonProps(
                onClickEventName = "click",
                enabledStateHostName = "canSubmit",
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Submit")
                )
            )
        )

        val canSubmitStateHost = object : com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost<Boolean> {
            override val state: Boolean = false
            override fun onStateChange(newState: Boolean) {}
        }

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("click" to mockBehavior),
                com.jesusdmedinac.jsontocompose.LocalStateHost provides mapOf("canSubmit" to canSubmitStateHost)
            ) {
                node.ToButton()
            }
        }

        onNodeWithText("Submit").assertExists()
        onNodeWithText("Submit").performClick()
        assertTrue(!clicked, "Behavior should not be invoked when state-driven enabled is false")
    }
}
