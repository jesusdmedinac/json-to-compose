package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SpacerRendererTest {

    @Test
    fun renderASpacerWithFixedHeight() = runComposeUiTest {
        // Given a JSON with a Spacer node with a Height modifier of 16
        val node = ComposeNode(
            type = ComposeType.Spacer,
            properties = NodeProperties.SpacerProps,
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Height(16)
                )
            )
        )

        // When the node is processed by the renderer
        setContent {
            node.ToSpacer()
        }

        // Then an empty space of 16dp height is rendered
        onNodeWithTag("Spacer")
            .assertExists()
            .assertHeightIsEqualTo(16.dp)
    }

    @Test
    fun renderASpacerWithFixedWidthInARow() = runComposeUiTest {
        // Given a JSON with a Row containing [Text("A"), Spacer(Width=8), Text("B")]
        val node = ComposeNode(
            type = ComposeType.Row,
            properties = NodeProperties.RowProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "A")
                    ),
                    ComposeNode(
                        type = ComposeType.Spacer,
                        properties = NodeProperties.SpacerProps,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Width(8)
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "B")
                    )
                )
            )
        )

        // When the node is processed by the renderer
        setContent {
            node.ToRow()
        }

        // Then "A" and "B" are separated by 8dp of horizontal space
        onNodeWithTag("Spacer")
            .assertExists()
            .assertWidthIsEqualTo(8.dp)
    }

    @Test
    fun renderASpacerWithWeightModifierInAColumn() = runComposeUiTest {
        // Given a JSON with a Column containing [Text("Top"), Spacer(Weight=1), Text("Bottom")]
        val node = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Top")
                    ),
                    ComposeNode(
                        type = ComposeType.Spacer,
                        properties = NodeProperties.SpacerProps,
                        composeModifier = ComposeModifier(
                            operations = listOf(
                                ComposeModifier.Operation.Weight(1f)
                            )
                        )
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Bottom")
                    )
                )
            )
        )

        // When the node is processed by the renderer
        setContent {
            node.ToColumn()
        }

        // Then "Top" is at the top, "Bottom" at the bottom, with Spacer filling remaining space
        onNodeWithTag("Spacer")
            .assertExists()
    }
}
