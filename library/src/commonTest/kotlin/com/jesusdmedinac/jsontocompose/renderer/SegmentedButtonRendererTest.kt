package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.*
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SegmentedButtonRendererTest {

    // --- Scenario: Render a SingleChoiceSegmentedButtonRow ---
    @Test
    fun singleChoiceSegmentedButtonRowRendersWithChildren() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.SingleChoiceSegmentedButtonRow,
            properties = NodeProperties.SegmentedButtonRowProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.SegmentedButton,
                        properties = NodeProperties.SegmentedButtonProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Day")
                            ),
                            selected = true
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.SegmentedButton,
                        properties = NodeProperties.SegmentedButtonProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Month")
                            ),
                            selected = false
                        )
                    )
                )
            )
        )

        setContent { node.ToSingleChoiceSegmentedButtonRow() }

        onNodeWithText("Day").assertExists()
        onNodeWithText("Month").assertExists()
        
        // Verify selection on the node that contains the text
        onNodeWithText("Day", useUnmergedTree = true).onParent().assertIsSelected()
    }

    // --- Scenario: Render a SegmentedButton with label and icon ---
    @Test
    fun segmentedButtonRendersWithLabelAndIcon() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.SegmentedButton,
            properties = NodeProperties.SegmentedButtonProps(
                label = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Calendar")
                ),
                icon = ComposeNode(
                    type = ComposeType.Text, // Using text as placeholder for icon in test
                    properties = NodeProperties.TextProps(text = "cal_icon")
                ),
                selected = true
            )
        )

        setContent { 
            // Needs a row scope
            val rowNode = ComposeNode(
                type = ComposeType.SingleChoiceSegmentedButtonRow,
                properties = NodeProperties.SegmentedButtonRowProps(
                    children = listOf(node)
                )
            )
            rowNode.ToSingleChoiceSegmentedButtonRow()
        }

        onNodeWithText("Calendar").assertExists()
        onNodeWithText("cal_icon").assertExists()
    }

    // --- Scenario: Render a MultiChoiceSegmentedButtonRow ---
    @Test
    fun multiChoiceSegmentedButtonRowRendersWithChildren() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.MultiChoiceSegmentedButtonRow,
            properties = NodeProperties.SegmentedButtonRowProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.SegmentedButton,
                        properties = NodeProperties.SegmentedButtonProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "A")
                            ),
                            selected = true
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.SegmentedButton,
                        properties = NodeProperties.SegmentedButtonProps(
                            label = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "B")
                            ),
                            selected = true
                        )
                    )
                )
            )
        )

        setContent { node.ToMultiChoiceSegmentedButtonRow() }

        onNodeWithText("A").assertExists()
        onNodeWithText("B").assertExists()
        
        onNodeWithText("A", useUnmergedTree = true).onParent().assertIsSelected()
        onNodeWithText("B", useUnmergedTree = true).onParent().assertIsSelected()
    }
}
