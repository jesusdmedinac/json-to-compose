package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.LocalStateHost
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class DisplayComponentsRendererTest {

    @Test
    fun iconRendersWithCustomSizeViaModifier() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Icon,
            properties = NodeProperties.IconProps(
                iconName = "settings"
            ),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Size(48, 48)
                )
            )
        )

        setContent {
            node.ToIcon()
        }

        onNodeWithTag("Icon").assertExists()
        onNodeWithText("settings").assertExists() // Fallback to Text in ToIcon when resource is not found
    }

    @Test
    fun badgeRendersWithNoContentAndWithCountText() = runComposeUiTest {
        val emptyBadgeNode = ComposeNode(
            type = ComposeType.Badge,
            properties = NodeProperties.BadgeProps(
                text = null
            )
        )

        val countBadgeNode = ComposeNode(
            type = ComposeType.Badge,
            properties = NodeProperties.BadgeProps(
                text = "9"
            )
        )

        setContent {
            emptyBadgeNode.ToBadge()
        }
        onNodeWithTag("Badge").assertExists()

        setContent {
            countBadgeNode.ToBadge()
        }
        onNodeWithTag("Badge").assertExists()
        onNodeWithText("9").assertExists()
    }

    @Test
    fun badgedBoxRendersWithIconAndBadge() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.BadgedBox,
            properties = NodeProperties.BadgedBoxProps(
                badge = ComposeNode(
                    type = ComposeType.Badge,
                    properties = NodeProperties.BadgeProps(text = "New")
                ),
                child = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "mail")
                )
            )
        )

        setContent {
            node.ToBadgedBox()
        }

        onNodeWithTag("BadgedBox").assertExists()
        onNodeWithTag("Badge").assertExists()
        onNodeWithText("New").assertExists()
        onNodeWithTag("Icon").assertExists()
        onNodeWithText("mail").assertExists()
    }

    @Test
    fun chipsRenderWithLabelsAndIcons() = runComposeUiTest {
        val assistChipNode = ComposeNode(
            type = ComposeType.AssistChip,
            properties = NodeProperties.ChipProps(
                label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Assist")),
                leadingIcon = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "star"))
            )
        )

        val suggestionChipNode = ComposeNode(
            type = ComposeType.SuggestionChip,
            properties = NodeProperties.ChipProps(
                label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Suggest"))
            )
        )

        setContent {
            assistChipNode.ToAssistChip()
        }
        onNodeWithTag("AssistChip").assertExists()
        onNodeWithText("Assist").assertExists()
        onNodeWithText("star").assertExists()

        setContent {
            suggestionChipNode.ToSuggestionChip()
        }
        onNodeWithTag("SuggestionChip").assertExists()
        onNodeWithText("Suggest").assertExists()
    }

    @Test
    fun filterChipStateDrivenSelectionAndOnClickToggle() = runComposeUiTest {
        var clicked = false
        val mockBehavior = object : Behavior {
            override fun invoke() {
                clicked = true
            }
        }

        var stateValue = false
        val selectedStateHost = object : StateHost<Boolean> {
            override val state: Boolean get() = stateValue
            override fun onStateChange(state: Boolean) {
                stateValue = state
            }
        }

        val filterChipNode = ComposeNode(
            type = ComposeType.FilterChip,
            properties = NodeProperties.FilterChipProps(
                selectedStateHostName = "selected_state",
                label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Filter")),
                onClickEventName = "on_click"
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("on_click" to mockBehavior),
                LocalStateHost provides mapOf("selected_state" to selectedStateHost)
            ) {
                filterChipNode.ToFilterChip()
            }
        }

        onNodeWithTag("FilterChip").assertExists()
        onNodeWithText("Filter").performClick()
        assertTrue(clicked)
        assertEquals(true, stateValue)
    }

    @Test
    fun inputChipRendersWithTrailingDeleteIcon() = runComposeUiTest {
        val inputChipNode = ComposeNode(
            type = ComposeType.InputChip,
            properties = NodeProperties.InputChipProps(
                label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Input")),
                trailingIcon = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "close"))
            )
        )

        setContent {
            inputChipNode.ToInputChip()
        }

        onNodeWithTag("InputChip").assertExists()
        onNodeWithText("Input").assertExists()
        onNodeWithText("close").assertExists()
    }

    @Test
    fun progressIndicatorsRenderIndeterminateAndDeterminateStateDriven() = runComposeUiTest {
        val indeterminateCirc = ComposeNode(
            type = ComposeType.CircularProgressIndicator,
            properties = NodeProperties.ProgressIndicatorProps(progress = null)
        )

        val determinateCirc = ComposeNode(
            type = ComposeType.CircularProgressIndicator,
            properties = NodeProperties.ProgressIndicatorProps(
                progressStateHostName = "progress_state",
                color = "#FFFF0000",
                trackColor = "#FF00FF00"
            )
        )

        val indeterminateLin = ComposeNode(
            type = ComposeType.LinearProgressIndicator,
            properties = NodeProperties.ProgressIndicatorProps(progress = null)
        )

        val determinateLin = ComposeNode(
            type = ComposeType.LinearProgressIndicator,
            properties = NodeProperties.ProgressIndicatorProps(
                progress = 0.5f,
                color = "#FFFF0000",
                trackColor = "#FF00FF00"
            )
        )

        val progressStateHost = object : StateHost<Float> {
            override val state: Float = 0.75f
            override fun onStateChange(state: Float) {}
        }

        setContent {
            indeterminateCirc.ToCircularProgressIndicator()
        }
        onNodeWithTag("CircularProgressIndicator").assertExists()

        setContent {
            CompositionLocalProvider(
                LocalStateHost provides mapOf("progress_state" to progressStateHost)
            ) {
                determinateCirc.ToCircularProgressIndicator()
            }
        }
        onNodeWithTag("CircularProgressIndicator").assertExists()

        setContent {
            indeterminateLin.ToLinearProgressIndicator()
        }
        onNodeWithTag("LinearProgressIndicator").assertExists()

        setContent {
            determinateLin.ToLinearProgressIndicator()
        }
        onNodeWithTag("LinearProgressIndicator").assertExists()
    }

    @Test
    fun tooltipsRenderWithAnchorsAndContent() = runComposeUiTest {
        val plainTooltip = ComposeNode(
            type = ComposeType.PlainTooltip,
            properties = NodeProperties.PlainTooltipProps(
                text = "Plain Tooltip Content",
                anchor = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "info")
                )
            )
        )

        val richTooltip = ComposeNode(
            type = ComposeType.RichTooltip,
            properties = NodeProperties.RichTooltipProps(
                title = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Rich Title")),
                text = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Rich Body")),
                action = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Action")),
                anchor = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "settings")
                )
            )
        )

        setContent {
            plainTooltip.ToPlainTooltip()
        }
        onNodeWithTag("PlainTooltip").assertExists()
        
        // Simular pulsación larga sobre el ancla del Plain Tooltip
        onNodeWithTag("Icon", useUnmergedTree = true).performTouchInput {
            longClick()
        }
        
        // Assert de que el contenido del Plain Tooltip se renderiza
        onNodeWithText("Plain Tooltip Content", useUnmergedTree = true).assertExists()

        setContent {
            richTooltip.ToRichTooltip()
        }
        onNodeWithTag("RichTooltip").assertExists()
        
        // Simular pulsación larga sobre el ancla del Rich Tooltip
        onNodeWithTag("Icon", useUnmergedTree = true).performTouchInput {
            longClick()
        }
        
        // Assert de que el contenido del Rich Tooltip se renderiza
        onNodeWithText("Rich Title", useUnmergedTree = true).assertExists()
        onNodeWithText("Rich Body", useUnmergedTree = true).assertExists()
        onNodeWithText("Action", useUnmergedTree = true).assertExists()
    }
}
