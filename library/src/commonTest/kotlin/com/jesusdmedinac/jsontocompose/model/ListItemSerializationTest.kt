package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ListItemSerializationTest {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    @Test
    fun serializeAndDeserializeListItem() {
        val originalNode = ComposeNode(
            type = ComposeType.ListItem,
            properties = NodeProperties.ListItemProps(
                headlineContent = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Headline")
                ),
                supportingContent = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Supporting")
                ),
                overlineContent = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "Overline")
                ),
                leadingContent = ComposeNode(
                    type = ComposeType.Icon,
                    properties = NodeProperties.IconProps(iconName = "Filled.AccountCircle")
                ),
                trailingContent = ComposeNode(
                    type = ComposeType.Text,
                    properties = NodeProperties.TextProps(text = "100")
                ),
                onClickEventName = "item_click"
            )
        )

        val jsonString = json.encodeToString(ComposeNode.serializer(), originalNode)
        val deserializedNode = json.decodeFromString(ComposeNode.serializer(), jsonString)

        assertEquals(originalNode, deserializedNode)
    }
}
