package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalBehavior
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SemanticStructureTest {

    // --- Scenario 1: Structure of basic Text ---

    @Test
    fun structureOfBasicText() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Snapshot Test")
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Text").assertIsDisplayed()
        onNodeWithTag("Text").assertTextEquals("Snapshot Test")
    }

    // --- Scenario 2: Structure of Column with multiple children ---

    @Test
    fun structureOfColumnWithMultipleChildren() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "A")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "B")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "C")
                    ),
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Column").assertExists()
        onAllNodesWithTag("Text").assertCountEquals(3)
        onNodeWithText("A").assertExists()
        onNodeWithText("B").assertExists()
        onNodeWithText("C").assertExists()
    }

    // --- Scenario 3: Structure of Row with SpaceEvenly arrangement ---

    @Test
    fun structureOfRowWithSpaceEvenlyArrangement() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Row,
            properties = NodeProperties.RowProps(
                horizontalArrangement = "SpaceEvenly",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "X")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Y")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Z")
                    ),
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Row").assertExists()
        onAllNodesWithTag("Text").assertCountEquals(3)
    }

    // --- Scenario 4: Structure of Box with contentAlignment Center ---

    @Test
    fun structureOfBoxWithContentAlignmentCenter() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Box,
            properties = NodeProperties.BoxProps(
                contentAlignment = "Center",
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Centered")
                    ),
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Box").assertExists()
        onNodeWithText("Centered").assertIsDisplayed()
    }

    // --- Scenario 5: Structure of Button with Text child ---

    @Test
    fun structureOfButtonWithTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Button,
            properties = NodeProperties.ButtonProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Click Me")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Button").assertExists()
        onNodeWithTag("Button").assertHasClickAction()
        onNodeWithText("Click Me").assertExists()
    }

    // --- Scenario 6: Structure of component with modifiers applied ---

    @Test
    fun structureOfComponentWithModifiersApplied() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Styled Text"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(value = 16),
                    ComposeModifier.Operation.BackgroundColor(hexColor = "#FF2196F3"),
                    ComposeModifier.Operation.FillMaxWidth,
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("Text").assertExists()
        onNodeWithTag("Text").assertIsDisplayed()
        onNodeWithTag("Text").assertTextEquals("Styled Text")
    }

    // --- Scenario 7: Structure of complex nested layout ---

    @Test
    fun structureOfComplexNestedLayout() = runComposeUiTest {
        val mockBehavior = object : Behavior {
            override fun onClick() {}
        }

        val node = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Row,
                        properties = NodeProperties.RowProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Title")
                                ),
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Subtitle")
                                ),
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Button,
                        properties = NodeProperties.ButtonProps(
                            onClickEventName = "action",
                            child = ComposeNode(
                                type = ComposeType.Text,
                                properties = NodeProperties.TextProps(text = "Action")
                            )
                        )
                    ),
                )
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalBehavior provides mapOf("action" to mockBehavior)
            ) {
                node.ToCompose()
            }
        }

        onNodeWithTag("Column").assertExists()
        onNodeWithTag("Row").assertExists()
        onNodeWithTag("Button").assertExists()
        onNodeWithTag("Button").assertHasClickAction()
        onNodeWithText("Title").assertExists()
        onNodeWithText("Subtitle").assertExists()
        onNodeWithText("Action").assertExists()
    }

    // --- Scenario 8: Structure of LazyColumn with items ---

    @Test
    fun structureOfLazyColumnWithItems() = runComposeUiTest {
        val children = (1..5).map { index ->
            ComposeNode(
                type = ComposeType.Text,
                properties = NodeProperties.TextProps(text = "Item $index"),
                composeModifier = ComposeModifier(
                    operations = listOf(
                        ComposeModifier.Operation.Padding(value = index * 4)
                    )
                )
            )
        }

        val node = ComposeNode(
            type = ComposeType.LazyColumn,
            properties = NodeProperties.ColumnProps(children = children)
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("LazyColumn").assertExists()
        onNodeWithText("Item 1").assertIsDisplayed()
        onNodeWithText("Item 2").assertIsDisplayed()
    }
}
