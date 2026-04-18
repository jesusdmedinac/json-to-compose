package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SurfaceRendererTest {

    @Test
    fun renderASurfaceWithDefaultColor() = runComposeUiTest {
        // Given a JSON with a Surface with no color specified
        val node = ComposeNode(
            type = ComposeType.Surface,
            properties = NodeProperties.SurfaceProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Default Surface")
                )
            )
        )

        var expectedColor: Color = Color.Unspecified

        // When the node is processed by the renderer
        setContent {
            MaterialTheme {
                expectedColor = MaterialTheme.colorScheme.surface
                node.ToSurface()
            }
        }

        // Then Surface with default Material 3 surface color is shown
        onNodeWithTag("Surface")
            .assertExists()
            .assert(SemanticsMatcher.expectValue(SurfaceColorKey, expectedColor))
    }

    @Test
    fun renderASurfaceWithTonalElevation() = runComposeUiTest {
        // Given a JSON with a Surface with SurfaceProps(tonalElevation = 4) and a Text child
        val node = ComposeNode(
            type = ComposeType.Surface,
            properties = NodeProperties.SurfaceProps(
                tonalElevation = 4,
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Elevated Content")
                )
            )
        )

        // When the node is processed by the renderer
        setContent {
            node.ToSurface()
        }

        // Then a Material 3 Surface with tonal elevation is shown
        onNodeWithTag("Surface")
            .assertExists()
            .assert(SemanticsMatcher.expectValue(SurfaceTonalElevationKey, 4.dp))
        onNodeWithText("Elevated Content").assertExists()
    }

    @Test
    fun renderASurfaceWithCustomColor() = runComposeUiTest {
        // Given a JSON with a Surface with custom color
        val node = ComposeNode(
            type = ComposeType.Surface,
            properties = NodeProperties.SurfaceProps(
                color = "#FFF5F5F5",
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Gray Surface")
                )
            )
        )

        // When the node is processed by the renderer
        setContent {
            node.ToSurface()
        }

        // Then Surface with light gray background is shown
        onNodeWithTag("Surface")
            .assertExists()
            .assert(SemanticsMatcher.expectValue(SurfaceColorKey, "#FFF5F5F5".toColor()))
    }
}
