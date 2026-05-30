package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EnhancedPropertiesTest {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun testImagePropsSerialization() {
        val original = NodeProperties.ImageProps(
            url = "https://example.com/image.png",
            alignment = "TopStart",
            colorFilter = "#FFFF0000",
            alignmentStateHostName = "imgAlign",
            colorFilterStateHostName = "imgTint"
        )
        val encoded = json.encodeToString(NodeProperties.serializer(), original)
        val decoded = json.decodeFromString(NodeProperties.serializer(), encoded) as NodeProperties.ImageProps

        assertEquals(original.url, decoded.url)
        assertEquals(original.alignment, decoded.alignment)
        assertEquals(original.colorFilter, decoded.colorFilter)
        assertEquals(original.alignmentStateHostName, decoded.alignmentStateHostName)
        assertEquals(original.colorFilterStateHostName, decoded.colorFilterStateHostName)
    }

    @Test
    fun testButtonPropsSerialization() {
        val original = NodeProperties.ButtonProps(
            shape = ComposeShape.Circle,
            containerColor = "#FF00FF00",
            contentColor = "#FFFFFFFF"
        )
        val encoded = json.encodeToString(NodeProperties.serializer(), original)
        val decoded = json.decodeFromString(NodeProperties.serializer(), encoded) as NodeProperties.ButtonProps

        assertEquals(original.shape, decoded.shape)
        assertEquals(original.containerColor, decoded.containerColor)
        assertEquals(original.contentColor, decoded.contentColor)
    }

    @Test
    fun testCardPropsSerialization() {
        val original = NodeProperties.CardProps(
            containerColor = "#FFFF00FF",
            borderColor = "#FF000000",
            borderWidth = 2,
            containerColorStateHostName = "cardBg",
            borderColorStateHostName = "cardBorderColor",
            borderWidthStateHostName = "cardBorderWidth"
        )
        val encoded = json.encodeToString(NodeProperties.serializer(), original)
        val decoded = json.decodeFromString(NodeProperties.serializer(), encoded) as NodeProperties.CardProps

        assertEquals(original.containerColor, decoded.containerColor)
        assertEquals(original.borderColor, decoded.borderColor)
        assertEquals(original.borderWidth, decoded.borderWidth)
        assertEquals(original.containerColorStateHostName, decoded.containerColorStateHostName)
        assertEquals(original.borderColorStateHostName, decoded.borderColorStateHostName)
        assertEquals(original.borderWidthStateHostName, decoded.borderWidthStateHostName)
    }

    @Test
    fun testOutlinedCardPropsSerialization() {
        val original = NodeProperties.OutlinedCardProps(
            containerColor = "#FFF0F0F0",
            borderWidth = 3,
            containerColorStateHostName = "outlinedCardBg",
            borderWidthStateHostName = "outlinedCardBorderWidth"
        )
        val encoded = json.encodeToString(NodeProperties.serializer(), original)
        val decoded = json.decodeFromString(NodeProperties.serializer(), encoded) as NodeProperties.OutlinedCardProps

        assertEquals(original.containerColor, decoded.containerColor)
        assertEquals(original.borderWidth, decoded.borderWidth)
        assertEquals(original.containerColorStateHostName, decoded.containerColorStateHostName)
        assertEquals(original.borderWidthStateHostName, decoded.borderWidthStateHostName)
    }

    @Test
    fun testAlertDialogPropsSerialization() {
        val original = NodeProperties.AlertDialogProps(
            icon = ComposeNode(type = ComposeType.Icon, properties = NodeProperties.IconProps(iconName = "Filled.Warning")),
            tonalElevation = 8,
            tonalElevationStateHostName = "dialogElevation"
        )
        val encoded = json.encodeToString(NodeProperties.serializer(), original)
        val decoded = json.decodeFromString(NodeProperties.serializer(), encoded) as NodeProperties.AlertDialogProps

        assertEquals(original.icon?.type, decoded.icon?.type)
        assertEquals(original.tonalElevation, decoded.tonalElevation)
        assertEquals(original.tonalElevationStateHostName, decoded.tonalElevationStateHostName)
    }

    @Test
    fun testTopAppBarVariantsSerialization() {
        val node1 = ComposeNode(type = ComposeType.CenterAlignedTopAppBar, properties = NodeProperties.TopAppBarProps())
        val node2 = ComposeNode(type = ComposeType.MediumTopAppBar, properties = NodeProperties.TopAppBarProps())
        val node3 = ComposeNode(type = ComposeType.LargeTopAppBar, properties = NodeProperties.TopAppBarProps())

        val enc1 = json.encodeToString(ComposeNode.serializer(), node1)
        val dec1 = json.decodeFromString(ComposeNode.serializer(), enc1)
        assertEquals(ComposeType.CenterAlignedTopAppBar, dec1.type)

        val enc2 = json.encodeToString(ComposeNode.serializer(), node2)
        val dec2 = json.decodeFromString(ComposeNode.serializer(), enc2)
        assertEquals(ComposeType.MediumTopAppBar, dec2.type)

        val enc3 = json.encodeToString(ComposeNode.serializer(), node3)
        val dec3 = json.decodeFromString(ComposeNode.serializer(), enc3)
        assertEquals(ComposeType.LargeTopAppBar, dec3.type)
    }

    @Test
    fun testBoxPropsSerialization() {
        val original = NodeProperties.BoxProps(
            propagateMinConstraints = true,
            propagateMinConstraintsStateHostName = "boxMinConstraints"
        )
        val encoded = json.encodeToString(NodeProperties.serializer(), original)
        val decoded = json.decodeFromString(NodeProperties.serializer(), encoded) as NodeProperties.BoxProps

        assertEquals(original.propagateMinConstraints, decoded.propagateMinConstraints)
        assertEquals(original.propagateMinConstraintsStateHostName, decoded.propagateMinConstraintsStateHostName)
    }
}
