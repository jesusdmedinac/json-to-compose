package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalTestApi::class)
class ElevatedCardRendererTest {

    @Test
    fun elevatedCardRendersWithTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ElevatedCard,
            properties = NodeProperties.CardProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Elevated Content")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("ElevatedCard").assertExists()
        onNodeWithTag("ElevatedCard").assertIsDisplayed()
        onNodeWithText("Elevated Content").assertIsDisplayed()
    }

    @Test
    fun elevatedCardRendersWithCustomElevationAndRoundedCorners() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.ElevatedCard,
            properties = NodeProperties.CardProps(
                elevation = 12,
                cornerRadius = 24,
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Elevated 12dp")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("ElevatedCard").assertExists()
        onNodeWithTag("ElevatedCard").assertIsDisplayed()
        onNodeWithText("Elevated 12dp").assertIsDisplayed()
        val props = node.properties as NodeProperties.CardProps
        assertEquals(12, props.elevation)
        assertEquals(24, props.cornerRadius)
    }
}
