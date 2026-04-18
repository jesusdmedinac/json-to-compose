package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class InputComponentsSerializationTest {

    private val json = Json { encodeDefaults = true }

    @Test
    fun sliderPropsSerialization() {
        val original = NodeProperties.SliderProps(
            value = 0.5f,
            valueRange = NodeProperties.FloatRange(0f, 100f),
            steps = 4,
            onValueChangeEventName = "on_change"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.SliderProps>(decoded)
        assertEquals(0.5f, decoded.value)
        assertEquals(0f, decoded.valueRange?.start)
        assertEquals(100f, decoded.valueRange?.endInclusive)
        assertEquals(4, decoded.steps)
        assertEquals("on_change", decoded.onValueChangeEventName)
    }

    @Test
    fun radioButtonPropsSerialization() {
        val original = NodeProperties.RadioButtonProps(
            selected = true,
            onClickEventName = "on_click"
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.RadioButtonProps>(decoded)
        assertEquals(true, decoded.selected)
        assertEquals("on_click", decoded.onClickEventName)
    }

    @Test
    fun segmentedButtonPropsSerialization() {
        val original = NodeProperties.SegmentedButtonProps(
            selected = true,
            label = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Option"))
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.SegmentedButtonProps>(decoded)
        assertEquals(true, decoded.selected)
        assertNotNull(decoded.label)
    }

    @Test
    fun datePickerPropsSerialization() {
        val original = NodeProperties.DatePickerProps(
            selectedDateMillis = 123456789L
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.DatePickerProps>(decoded)
        assertEquals(123456789L, decoded.selectedDateMillis)
    }

    @Test
    fun timePickerPropsSerialization() {
        val original = NodeProperties.TimePickerProps(
            hour = 10,
            minute = 30,
            is24Hour = true
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.TimePickerProps>(decoded)
        assertEquals(10, decoded.hour)
        assertEquals(30, decoded.minute)
        assertEquals(true, decoded.is24Hour)
    }

    @Test
    fun searchBarPropsSerialization() {
        val original = NodeProperties.SearchBarProps(
            query = "Search",
            placeholder = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Placeholder"))
        )
        val encoded = json.encodeToString<NodeProperties>(original)
        val decoded = json.decodeFromString<NodeProperties>(encoded)

        assertIs<NodeProperties.SearchBarProps>(decoded)
        assertEquals("Search", decoded.query)
        assertNotNull(decoded.placeholder)
    }
}
