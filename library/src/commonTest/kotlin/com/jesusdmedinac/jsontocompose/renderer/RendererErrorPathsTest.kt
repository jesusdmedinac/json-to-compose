package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class RendererErrorPathsTest {

    // --- Scenario 1: Renderer returns early when props type is wrong ---

    @Test
    fun textRendererReturnsEarlyWithWrongProps() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.ButtonProps(onClickEventName = "wrong")
        )

        setContent {
            node.ToCompose()
        }

        // Text renderer should early-return; no Text tag in the tree
        onAllNodesWithTag("Text").assertCountEquals(0)
    }

    // --- Scenario 2: TextField renders with default empty value when no StateHost ---

    @Test
    fun textFieldRendersWithDefaultWhenStateHostNameIsNull() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TextField,
            properties = NodeProperties.TextFieldProps(valueStateHostName = null)
        )

        setContent {
            node.ToCompose()
        }

        // TextField renders with empty default value
        onAllNodesWithTag("TextField").assertCountEquals(1)
    }

    // --- Scenario 3: TextField renders with default when StateHost is not registered ---

    @Test
    fun textFieldRendersWithDefaultWhenStateHostNotRegistered() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TextField,
            properties = NodeProperties.TextFieldProps(valueStateHostName = "missing_key")
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides emptyMap()
            ) {
                node.ToCompose()
            }
        }

        // TextField renders with empty default, logs warning about missing StateHost
        onAllNodesWithTag("TextField").assertCountEquals(1)
    }

    // --- Scenario 4: TextField renders with default when StateHost has wrong type ---

    @Test
    fun textFieldRendersWithDefaultWhenStateHostHasWrongType() = runComposeUiTest {
        val boolStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = true
            override fun onStateChange(state: Boolean) {}
        }

        val node = ComposeNode(
            type = ComposeType.TextField,
            properties = NodeProperties.TextFieldProps(valueStateHostName = "bool_state")
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("bool_state" to boolStateHost)
            ) {
                node.ToCompose()
            }
        }

        // TextField renders with empty default, logs warning about type mismatch
        onAllNodesWithTag("TextField").assertCountEquals(1)
    }

    // --- Scenario 5: TopAppBar returns early when props type is wrong ---

    @Test
    fun topAppBarRendererReturnsEarlyWithWrongProps() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.TopAppBar,
            properties = NodeProperties.TextProps(text = "wrong")
        )

        setContent {
            node.ToCompose()
        }

        onAllNodesWithTag("TopAppBar").assertCountEquals(0)
    }

    // --- Scenario 6: Dialog defaults to visible when visibilityStateHostName is not registered ---

    @Test
    fun dialogDefaultsToVisibleWhenStateHostNotRegistered() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.AlertDialogProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Visible Dialog")
                ),
                confirmButton = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "OK")
                        )
                    )
                ),
                visibilityStateHostName = "missing_key",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides emptyMap()
            ) {
                node.ToCompose()
            }
        }

        onNodeWithText("Visible Dialog").assertExists()
    }

    // --- Scenario 6: Dialog defaults to visible when StateHost has wrong type ---

    @Test
    fun dialogDefaultsToVisibleWhenStateHostHasWrongType() = runComposeUiTest {
        val stringStateHost = object : StateHost<String> {
            override val state: String get() = "hello"
            override fun onStateChange(state: String) {}
        }

        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.AlertDialogProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Wrong Type Dialog")
                ),
                confirmButton = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "OK")
                        )
                    )
                ),
                visibilityStateHostName = "string_state",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("string_state" to stringStateHost)
            ) {
                node.ToCompose()
            }
        }

        onNodeWithText("Wrong Type Dialog").assertExists()
    }

    // --- Scenario 7: Button renders without crash when no Behavior is registered ---

    @Test
    fun buttonRendersWithoutCrashWhenNoBehaviorRegistered() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Button,
            properties = NodeProperties.ButtonProps(
                onClickEventName = "missing_action",
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Orphan Button")
                )
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides emptyMap()
            ) {
                node.ToCompose()
            }
        }

        onNodeWithText("Orphan Button").assertExists()
        // Click should not crash
        onNodeWithText("Orphan Button").performClick()
    }

    // --- Scenario 8: Image renders fallback when resource is not found ---

    @Test
    fun imageRendersFallbackWhenResourceNotFound() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Image,
            properties = NodeProperties.ImageProps(
                resourceName = "nonexistent",
                contentDescription = "Missing image"
            )
        )

        setContent {
            node.ToCompose()
        }

        // The renderer shows a fallback Box with "Res not found: nonexistent"
        onNodeWithText("Res not found: nonexistent").assertExists()
    }
}
