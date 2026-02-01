package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class AlertDialogRendererTest {

    // --- Scenario 1: Render a basic AlertDialog with title and text ---

    @Test
    fun dialogRendersWithTitleAndText() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.AlertDialogProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Confirm")
                ),
                text = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Do you want to continue?")
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
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Confirm").assertIsDisplayed()
        onNodeWithText("Do you want to continue?").assertIsDisplayed()
    }

    // --- Scenario 2: Render an AlertDialog with action buttons ---

    @Test
    fun dialogRendersWithActionButtons() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.AlertDialogProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Action")
                ),
                text = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Choose an option")
                ),
                confirmButton = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Yes")
                        )
                    )
                ),
                dismissButton = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "No")
                        )
                    )
                ),
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Yes").assertIsDisplayed()
        onNodeWithText("No").assertIsDisplayed()
    }

    // --- Scenario 3: AlertDialog confirm button emits event ---

    @Test
    fun dialogEmitsConfirmEvent() = runComposeUiTest {
        var confirmed = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                confirmed = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.AlertDialogProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Delete")
                ),
                text = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Are you sure?")
                ),
                confirmButton = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        onClickEventName = "confirm_delete",
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Delete")
                        )
                    )
                ),
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("confirm_delete" to mockBehavior)
            ) {
                node.ToCompose()
            }
        }

        onNodeWithText("Delete").performClick()
        assertTrue(confirmed, "Confirm behavior should have been invoked")
    }

    // --- Scenario 4: AlertDialog dismiss button emits event ---

    @Test
    fun dialogEmitsDismissEvent() = runComposeUiTest {
        var dismissed = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                dismissed = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.AlertDialogProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Delete")
                ),
                text = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Are you sure?")
                ),
                confirmButton = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Delete")
                        )
                    )
                ),
                dismissButton = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        onClickEventName = "cancel_delete",
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Cancel")
                        )
                    )
                ),
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("cancel_delete" to mockBehavior)
            ) {
                node.ToCompose()
            }
        }

        onNodeWithText("Cancel").performClick()
        assertTrue(dismissed, "Dismiss behavior should have been invoked")
    }

    // --- Scenario 5: AlertDialog with custom content nodes ---

    @Test
    fun dialogRendersWithCustomContent() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.AlertDialogProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Custom Dialog")
                ),
                text = ComposeNode(
                    type = ComposeType.Column,
                    properties = NodeProperties.ColumnProps(
                        children = listOf(
                            ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Custom line 1")
                            ),
                            ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Custom line 2")
                            ),
                        )
                    )
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
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Custom Dialog").assertIsDisplayed()
        onNodeWithText("Custom line 1").assertIsDisplayed()
        onNodeWithText("Custom line 2").assertIsDisplayed()
    }

    // --- Scenario 6: Serialize and deserialize an AlertDialog from JSON ---

    @Test
    fun dialogSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.AlertDialogProps(
                title = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Confirm")
                ),
                text = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Continue?")
                ),
                confirmButton = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        onClickEventName = "evt_confirm",
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "Yes")
                        )
                    )
                ),
                dismissButton = ComposeNode(
                    type = ComposeType.Button,
                    properties = NodeProperties.ButtonProps(
                        onClickEventName = "evt_dismiss",
                        child = ComposeNode(
                            type = ComposeType.Text,
                            properties = NodeProperties.TextProps(text = "No")
                        )
                    )
                ),
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.AlertDialog, decoded.type)
        val props = decoded.properties as? NodeProperties.AlertDialogProps
        assertNotNull(props)
        assertNotNull(props.title)
        assertEquals(ComposeType.Text, props.title?.type)
        assertNotNull(props.text)
        assertEquals(ComposeType.Text, props.text?.type)
        assertNotNull(props.confirmButton)
        assertEquals(ComposeType.Button, props.confirmButton?.type)
        assertNotNull(props.dismissButton)
        assertEquals(ComposeType.Button, props.dismissButton?.type)
    }
}
