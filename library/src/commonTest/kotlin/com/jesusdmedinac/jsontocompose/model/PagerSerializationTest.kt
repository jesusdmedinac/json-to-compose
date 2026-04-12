package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PagerSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun horizontalPagerRoundTripSerialization() {
        val original = ComposeNode(
            type = ComposeType.HorizontalPager,
            properties = NodeProperties.PagerProps(
                pages = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 1")
                    ),
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Page 2")
                    )
                ),
                currentPage = 1,
                currentPageStateHostName = "pageState",
                beyondViewportPageCount = 2,
                userScrollEnabled = false,
                userScrollEnabledStateHostName = "scrollEnabledState"
            )
        )

        val serialized = json.encodeToString(original)
        val deserialized = json.decodeFromString<ComposeNode>(serialized)

        assertEquals(original.type, deserialized.type)
        val originalProps = original.properties as NodeProperties.PagerProps
        val deserializedProps = deserialized.properties as NodeProperties.PagerProps

        assertEquals(originalProps.pages?.size, deserializedProps.pages?.size)
        assertEquals(originalProps.currentPage, deserializedProps.currentPage)
        assertEquals(originalProps.currentPageStateHostName, deserializedProps.currentPageStateHostName)
        assertEquals(originalProps.beyondViewportPageCount, deserializedProps.beyondViewportPageCount)
        assertEquals(originalProps.userScrollEnabled, deserializedProps.userScrollEnabled)
        assertEquals(originalProps.userScrollEnabledStateHostName, deserializedProps.userScrollEnabledStateHostName)
    }

    @Test
    fun verticalPagerRoundTripSerialization() {
        val original = ComposeNode(
            type = ComposeType.VerticalPager,
            properties = NodeProperties.PagerProps(
                pages = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Vertical Page")
                    )
                ),
                userScrollEnabled = true
            )
        )

        val serialized = json.encodeToString(original)
        val deserialized = json.decodeFromString<ComposeNode>(serialized)

        assertEquals(original.type, deserialized.type)
        val originalProps = original.properties as NodeProperties.PagerProps
        val deserializedProps = deserialized.properties as NodeProperties.PagerProps

        assertEquals(originalProps.pages?.size, deserializedProps.pages?.size)
        assertEquals(originalProps.userScrollEnabled, deserializedProps.userScrollEnabled)
    }
}
