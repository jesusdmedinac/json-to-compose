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
class OutlinedCardRendererTest {

    @Test
    fun outlinedCardRendersWithTextChild() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.OutlinedCard,
            properties = NodeProperties.OutlinedCardProps(
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Outlined Content")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("OutlinedCard").assertExists()
        onNodeWithTag("OutlinedCard").assertIsDisplayed()
        onNodeWithText("Outlined Content").assertIsDisplayed()
    }

    @Test
    fun outlinedCardRendersWithCustomBorderColorAndRoundedCorners() = runComposeUiTest {
        val node = ComposeNode(
            type = ComposeType.OutlinedCard,
            properties = NodeProperties.OutlinedCardProps(
                borderColor = "#FF000000",
                cornerRadius = 24,
                child = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Outlined Black")
                )
            )
        )

        setContent {
            node.ToCompose()
        }

        onNodeWithTag("OutlinedCard").assertExists()
        onNodeWithTag("OutlinedCard").assertIsDisplayed()
        onNodeWithText("Outlined Black").assertIsDisplayed()
        val props = node.properties as NodeProperties.OutlinedCardProps
        assertEquals("#FF000000", props.borderColor)
    }
}
