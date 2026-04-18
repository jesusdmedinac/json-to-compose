package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.DividerDefaults
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class DividerRendererTest {

    @Test
    fun renderAHorizontalDivider() = runComposeUiTest {
        // Given a JSON with a node of type "HorizontalDivider"
        val node = ComposeNode(
            type = ComposeType.HorizontalDivider,
            properties = NodeProperties.DividerProps()
        )

        // When the node is processed by the renderer
        setContent {
            node.ToHorizontalDivider()
        }

        // Then a thin horizontal line separator is shown with default thickness
        onNodeWithTag("HorizontalDivider")
            .assertExists()
            .assert(SemanticsMatcher.expectValue(HorizontalDividerThicknessKey, DividerDefaults.Thickness))
    }

    @Test
    fun renderAHorizontalDividerWithCustomThicknessAndColor() = runComposeUiTest {
        // Given a JSON with a HorizontalDivider with DividerProps(thickness = 2, color = "#FF888888")
        val node = ComposeNode(
            type = ComposeType.HorizontalDivider,
            properties = NodeProperties.DividerProps(
                thickness = 2,
                color = "#FF888888"
            )
        )

        // When the node is processed by the renderer
        setContent {
            node.ToHorizontalDivider()
        }

        // Then a 2dp gray horizontal divider is shown
        onNodeWithTag("HorizontalDivider")
            .assertExists()
            .assert(SemanticsMatcher.expectValue(HorizontalDividerThicknessKey, 2.dp))
            .assert(SemanticsMatcher.expectValue(HorizontalDividerColorKey, "#FF888888".toColor()))
    }

    @Test
    fun renderAVerticalDivider() = runComposeUiTest {
        // Given a JSON with a VerticalDivider
        val node = ComposeNode(
            type = ComposeType.VerticalDivider,
            properties = NodeProperties.DividerProps()
        )

        // When the node is processed by the renderer
        setContent {
            node.ToVerticalDivider()
        }

        // Then a thin vertical line separator is shown with default thickness
        onNodeWithTag("VerticalDivider")
            .assertExists()
            .assert(SemanticsMatcher.expectValue(VerticalDividerThicknessKey, DividerDefaults.Thickness))
    }
}
