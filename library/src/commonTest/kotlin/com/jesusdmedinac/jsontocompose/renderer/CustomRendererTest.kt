package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.material.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.LocalCustomRenderers
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalTestApi::class)
class CustomRendererTest {

    @Test
    fun customRendererIsInvokedWithCorrectData() = runComposeUiTest {
        var receivedNode: ComposeNode? = null

        val customData = JsonObject(
            mapOf("stars" to JsonPrimitive(5))
        )

        val node = ComposeNode(
            type = ComposeType.Custom,
            properties = NodeProperties.CustomProps(
                customType = "Rating",
                customData = customData
            )
        )

        setContent {
            CompositionLocalProvider(
                LocalCustomRenderers provides mapOf(
                    "Rating" to { composeNode ->
                        receivedNode = composeNode
                        val props = composeNode.properties as? NodeProperties.CustomProps
                        val stars = props?.customData?.get("stars")
                        Text("Stars: $stars")
                    }
                )
            ) {
                node.ToCustom()
            }
        }

        assertNotNull(receivedNode)
        val receivedProps = receivedNode!!.properties as? NodeProperties.CustomProps
        assertNotNull(receivedProps)
        assertEquals("Rating", receivedProps.customType)
        assertEquals(customData, receivedProps.customData)
        onNodeWithText("Stars: 5").assertExists()
    }
}
