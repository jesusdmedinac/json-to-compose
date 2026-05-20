package com.jesusdmedinac.jsontocompose.modifier

import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ModifierSerializationTest {
    private val json = Json { encodeDefaults = true }

    @Test
    fun clickableModifierSerialization() {
        val original = ComposeModifier.Operation.Clickable(onClickEventName = "my_event")
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.Clickable>(decoded)
        assertEquals("my_event", decoded.onClickEventName)
        assertTrue(encoded.contains("\"type\":\"Clickable\""))
    }

    @Test
    fun weightModifierSerialization() {
        val original = ComposeModifier.Operation.Weight(weight = 1.5f)
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.Weight>(decoded)
        assertEquals(1.5f, decoded.weight)
    }

    @Test
    fun verticalScrollModifierSerialization() {
        val original = ComposeModifier.Operation.VerticalScroll
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.VerticalScroll>(decoded)
    }

    @Test
    fun horizontalScrollModifierSerialization() {
        val original = ComposeModifier.Operation.HorizontalScroll
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.HorizontalScroll>(decoded)
    }

    @Test
    fun offsetModifierSerialization() {
        val original = ComposeModifier.Operation.Offset(x = 10, y = 20)
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.Offset>(decoded)
        assertEquals(10, decoded.x)
        assertEquals(20, decoded.y)
    }

    @Test
    fun sizeModifierSerialization() {
        val original = ComposeModifier.Operation.Size(width = 100, height = 200)
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.Size>(decoded)
        assertEquals(100, decoded.width)
        assertEquals(200, decoded.height)
    }

    @Test
    fun wrapContentWidthModifierSerialization() {
        val original = ComposeModifier.Operation.WrapContentWidth
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.WrapContentWidth>(decoded)
    }

    @Test
    fun wrapContentHeightModifierSerialization() {
        val original = ComposeModifier.Operation.WrapContentHeight
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.WrapContentHeight>(decoded)
    }

    @Test
    fun aspectRatioModifierSerialization() {
        val original = ComposeModifier.Operation.AspectRatio(ratio = 16f / 9f)
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.AspectRatio>(decoded)
        assertEquals(16f / 9f, decoded.ratio)
    }

    @Test
    fun zIndexModifierSerialization() {
        val original = ComposeModifier.Operation.ZIndex(zIndex = 5.0f)
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.ZIndex>(decoded)
        assertEquals(5.0f, decoded.zIndex)
    }

    @Test
    fun minWidthModifierSerialization() {
        val original = ComposeModifier.Operation.MinWidth(value = 50)
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.MinWidth>(decoded)
        assertEquals(50, decoded.value)
    }

    @Test
    fun minHeightModifierSerialization() {
        val original = ComposeModifier.Operation.MinHeight(value = 60)
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.MinHeight>(decoded)
        assertEquals(60, decoded.value)
    }

    @Test
    fun maxWidthModifierSerialization() {
        val original = ComposeModifier.Operation.MaxWidth(value = 500)
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.MaxWidth>(decoded)
        assertEquals(500, decoded.value)
    }

    @Test
    fun maxHeightModifierSerialization() {
        val original = ComposeModifier.Operation.MaxHeight(value = 600)
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.MaxHeight>(decoded)
        assertEquals(600, decoded.value)
    }

    @Test
    fun animateContentSizeModifierSerialization() {
        val original = ComposeModifier.Operation.AnimateContentSize
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.AnimateContentSize>(decoded)
    }

    @Test
    fun testTagModifierSerialization() {
        val original = ComposeModifier.Operation.TestTag(tag = "my_tag")
        val encoded = json.encodeToString<ComposeModifier.Operation>(original)
        val decoded = json.decodeFromString<ComposeModifier.Operation>(encoded)

        assertIs<ComposeModifier.Operation.TestTag>(decoded)
        assertEquals("my_tag", decoded.tag)
    }
}
