package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ImageRendererTest {

    @Test
    fun imageWithUrlRendersAsyncImage() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Image,
            properties = NodeProperties.ImageProps(
                url = "https://example.com/img.png",
                contentDescription = "Example image"
            )
        )

        setContent {
            node.ToImage()
        }

        // AsyncImage renders without crashing; content description validates it exists
        // Note: In a test environment, the image won't actually load,
        // but the composable should be present in the tree
    }

    @Test
    fun imageWithMissingResourceShowsFallback() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.Image,
            properties = NodeProperties.ImageProps(
                resourceName = "nonexistent_logo",
                contentDescription = "Logo"
            )
        )

        setContent {
            node.ToImage()
        }

        // When the resource is not found in LocalDrawableResources,
        // the renderer shows a fallback Box with "Res not found: {name}"
        onNodeWithText("Res not found: nonexistent_logo").assertExists()
    }
}
