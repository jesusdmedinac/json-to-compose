package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.MutableStateHost
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class RadioButtonRendererTest {

    // --- Scenario: Render a RadioButton selected ---
    @Test
    fun radioButtonRendersSelected() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.RadioButton,
            properties = NodeProperties.RadioButtonProps(
                selected = true
            )
        )

        setContent { node.ToRadioButton() }

        onNodeWithTag(ComposeType.RadioButton.name).assertIsSelected()
    }

    // --- Scenario: Render a RadioButton unselected ---
    @Test
    fun radioButtonRendersUnselected() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.RadioButton,
            properties = NodeProperties.RadioButtonProps(
                selected = false
            )
        )

        setContent { node.ToRadioButton() }

        onNodeWithTag(ComposeType.RadioButton.name).assertIsNotSelected()
    }

    // --- Scenario: RadioButton selection controlled by state ---
    @Test
    fun radioButtonSelectionControlledByState() = runComposeUiTest {
        val state = MutableStateHost(true)
        val node = ComposeNode(
            type = ComposeType.RadioButton,
            properties = NodeProperties.RadioButtonProps(
                selectedStateHostName = "option1Selected"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("option1Selected" to state)
            ) {
                node.ToRadioButton()
            }
        }

        onNodeWithTag(ComposeType.RadioButton.name).assertIsSelected()

        state.onStateChange(false)
        onNodeWithTag(ComposeType.RadioButton.name).assertIsNotSelected()
    }

    // --- Scenario: RadioButton onClick triggers action ---
    @Test
    fun radioButtonOnClickTriggersAction() = runComposeUiTest {
        var clicked = false
        val behavior = object : Behavior {
            override fun invoke() {
                clicked = true
            }
        }

        val node = ComposeNode(
            type = ComposeType.RadioButton,
            properties = NodeProperties.RadioButtonProps(
                onClickEventName = "selectOption1"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("selectOption1" to behavior)
            ) {
                node.ToRadioButton()
            }
        }

        onNodeWithTag(ComposeType.RadioButton.name).performClick()
        assertTrue(clicked)
    }
}
