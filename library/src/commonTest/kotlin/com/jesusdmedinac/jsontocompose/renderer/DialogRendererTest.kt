package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
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
class DialogRendererTest {

    // --- Scenario 1: Render a basic Dialog with title and content ---

    @Test
    fun dialogRendersWithTitleAndContent() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.DialogProps(
                title = "Confirm",
                content = "Do you want to continue?",
                confirmButtonText = "OK",
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Confirm").assertIsDisplayed()
        onNodeWithText("Do you want to continue?").assertIsDisplayed()
    }

    // --- Scenario 2: Render a Dialog with action buttons ---

    @Test
    fun dialogRendersWithActionButtons() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.DialogProps(
                title = "Action",
                content = "Choose an option",
                confirmButtonText = "Yes",
                dismissButtonText = "No",
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Yes").assertIsDisplayed()
        onNodeWithText("No").assertIsDisplayed()
    }

    // --- Scenario 3: Dialog emits event on confirm ---

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
            properties = NodeProperties.DialogProps(
                title = "Delete",
                content = "Are you sure?",
                confirmButtonText = "Delete",
                onConfirmEventName = "confirm_delete",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("confirm_delete" to mockBehavior)
            ) {
                node.ToCompose()
            }
        }

        onNodeWithTag("DialogConfirmButton").performClick()
        assertTrue(confirmed, "Confirm behavior should have been invoked")
    }

    // --- Scenario 4: Dialog emits event on cancel ---

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
            properties = NodeProperties.DialogProps(
                title = "Delete",
                content = "Are you sure?",
                confirmButtonText = "Delete",
                dismissButtonText = "Cancel",
                onDismissEventName = "cancel_delete",
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("cancel_delete" to mockBehavior)
            ) {
                node.ToCompose()
            }
        }

        onNodeWithTag("DialogDismissButton").performClick()
        assertTrue(dismissed, "Dismiss behavior should have been invoked")
    }

    // --- Scenario 5: Dialog with custom content ---

    @Test
    fun dialogRendersWithCustomContent() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.DialogProps(
                title = "Custom Dialog",
                confirmButtonText = "OK",
                child = ComposeNode(
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
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithText("Custom Dialog").assertIsDisplayed()
        onNodeWithText("Custom line 1").assertIsDisplayed()
        onNodeWithText("Custom line 2").assertIsDisplayed()
    }

    // --- Scenario 6: Serialize and deserialize a Dialog from JSON ---

    @Test
    fun dialogSerializationRoundtrip() {
        val original = ComposeNode(
            type = ComposeType.AlertDialog,
            properties = NodeProperties.DialogProps(
                title = "Confirm",
                content = "Continue?",
                confirmButtonText = "Yes",
                dismissButtonText = "No",
                onConfirmEventName = "evt_confirm",
                onDismissEventName = "evt_dismiss",
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Extra")
                )
            )
        )

        val json = Json.encodeToString(original)
        val decoded = Json.decodeFromString<ComposeNode>(json)

        assertEquals(ComposeType.AlertDialog, decoded.type)
        val props = decoded.properties as? NodeProperties.DialogProps
        assertNotNull(props)
        assertEquals("Confirm", props.title)
        assertEquals("Continue?", props.content)
        assertEquals("Yes", props.confirmButtonText)
        assertEquals("No", props.dismissButtonText)
        assertEquals("evt_confirm", props.onConfirmEventName)
        assertEquals("evt_dismiss", props.onDismissEventName)
        assertNotNull(props.child)
        assertEquals(ComposeType.Text, props.child?.type)
    }
}
