package com.jesusdmedinac.jsontocompose.runtime

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeAction
import com.jesusdmedinac.jsontocompose.model.ComposeDocument
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class BackwardCompatibilityTest {

    // --- Scenario 4: Manual and auto-wired state and behaviors can coexist ---

    @Test
    fun manualAndAutoWiredCoexist() = runComposeUiTest {
        var manualClicked = false
        val manualBehavior = object : Behavior {
            override fun invoke() { manualClicked = true }
        }

        val doc = ComposeDocument(
            initialState = mapOf(
                "auto_flag" to JsonPrimitive(false),
            ),
            actions = mapOf(
                "auto_toggle" to listOf(
                    ComposeAction.ToggleState(stateKey = "auto_flag"),
                ),
            ),
            root = ComposeNode(
                type = ComposeType.Column,
                properties = NodeProperties.ColumnProps(
                    children = listOf(
                        // This button uses a MANUAL behavior
                        ComposeNode(
                            type = ComposeType.Button,
                            properties = NodeProperties.ButtonProps(
                                onClickEventName = "manual_action",
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Manual"),
                                ),
                            ),
                        ),
                        // This button uses an AUTO-WIRED behavior
                        ComposeNode(
                            type = ComposeType.Button,
                            properties = NodeProperties.ButtonProps(
                                onClickEventName = "auto_toggle",
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Auto"),
                                ),
                            ),
                        ),
                        // This switch uses an AUTO-WIRED state host
                        ComposeNode(
                            type = ComposeType.Switch,
                            properties = NodeProperties.SwitchProps(
                                checkedStateHostName = "auto_flag",
                            ),
                        ),
                    ),
                ),
            ),
        )

        setContent {
            // Provide manual behavior OUTSIDE the document
            CompositionLocalProvider(
                LocalBehavior provides mapOf("manual_action" to manualBehavior)
            ) {
                doc.ToCompose()
            }
        }

        // Auto-wired switch starts off
        onNodeWithTag("Switch").assertIsOff()

        // Auto-wired behavior works
        onNodeWithText("Auto").performClick()
        onNodeWithTag("Switch").assertIsOn()

        // Manual behavior works
        onNodeWithText("Manual").performClick()
        assertTrue(manualClicked, "Manual behavior should still work alongside auto-wired")
    }
}
