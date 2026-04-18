package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class LayoutSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun serializeAndDeserializeSpacer() {
        val node = ComposeNode(
            type = ComposeType.Spacer,
            properties = NodeProperties.SpacerProps
        )
        val jsonString = json.encodeToString(node)
        val decodedNode = json.decodeFromString<ComposeNode>(jsonString)
        
        assertEquals(ComposeType.Spacer, decodedNode.type)
        assertEquals(NodeProperties.SpacerProps, decodedNode.properties)
    }

    @Test
    fun serializeAndDeserializeDividers() {
        val horizontal = ComposeNode(
            type = ComposeType.HorizontalDivider,
            properties = NodeProperties.DividerProps(thickness = 2, color = "#FF0000")
        )
        val vertical = ComposeNode(
            type = ComposeType.VerticalDivider,
            properties = NodeProperties.DividerProps(thickness = 1, color = "#0000FF")
        )

        val hJson = json.encodeToString(horizontal)
        val vJson = json.encodeToString(vertical)

        val hDecoded = json.decodeFromString<ComposeNode>(hJson)
        val vDecoded = json.decodeFromString<ComposeNode>(vJson)

        assertEquals(ComposeType.HorizontalDivider, hDecoded.type)
        assertEquals(2, (hDecoded.properties as NodeProperties.DividerProps).thickness)
        
        assertEquals(ComposeType.VerticalDivider, vDecoded.type)
        assertEquals("#0000FF", (vDecoded.properties as NodeProperties.DividerProps).color)
    }

    @Test
    fun serializeAndDeserializeFlowLayouts() {
        val flowRow = ComposeNode(
            type = ComposeType.FlowRow,
            properties = NodeProperties.FlowRowProps(
                horizontalArrangement = "SpaceBetween",
                verticalArrangement = "Center",
                children = listOf(
                    ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Item"))
                )
            )
        )

        val jsonString = json.encodeToString(flowRow)
        val decoded = json.decodeFromString<ComposeNode>(jsonString)

        assertEquals(ComposeType.FlowRow, decoded.type)
        val props = decoded.properties as NodeProperties.FlowRowProps
        assertEquals("SpaceBetween", props.horizontalArrangement)
        assertEquals("Center", props.verticalArrangement)
        assertEquals(1, props.children?.size)
    }

    @Test
    fun serializeAndDeserializeSurface() {
        val surface = ComposeNode(
            type = ComposeType.Surface,
            properties = NodeProperties.SurfaceProps(
                tonalElevation = 4,
                color = "#FFFFFF",
                shape = ComposeShape.RoundedCorner(all = 8),
                child = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Inside"))
            )
        )

        val jsonString = json.encodeToString(surface)
        val decoded = json.decodeFromString<ComposeNode>(jsonString)

        assertEquals(ComposeType.Surface, decoded.type)
        val props = decoded.properties as NodeProperties.SurfaceProps
        assertEquals(4, props.tonalElevation)
        assertEquals("#FFFFFF", props.color)
        val shape = props.shape as ComposeShape.RoundedCorner
        assertEquals(8, shape.all)
    }
}
