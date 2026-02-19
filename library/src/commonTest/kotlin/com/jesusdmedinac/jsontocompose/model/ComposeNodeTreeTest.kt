package com.jesusdmedinac.jsontocompose.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ComposeNodeTreeTest {

    // --- Scenario 1: countLevels for root node ---

    @Test
    fun countLevelsForRootNodeIsZero() {
        val root = ComposeNode(type = ComposeType.Text)
        assertEquals(0, root.countLevels())
    }

    // --- Scenario 2: countLevels for nested node ---

    @Test
    fun countLevelsForNestedNodeReturnsDepth() {
        val grandparent = ComposeNode(type = ComposeType.Column)
        val parent = ComposeNode(type = ComposeType.Row, parent = grandparent)
        val child = ComposeNode(type = ComposeType.Box, parent = parent)
        val leaf = ComposeNode(type = ComposeType.Text, parent = child)

        assertEquals(0, grandparent.countLevels())
        assertEquals(1, parent.countLevels())
        assertEquals(2, child.countLevels())
        assertEquals(3, leaf.countLevels())
    }

    // --- Scenario 3: parents for root node ---

    @Test
    fun parentsForRootNodeIsEmpty() {
        val root = ComposeNode(type = ComposeType.Text)
        assertTrue(root.parents().isEmpty())
    }

    // --- Scenario 4: parents for nested node ---

    @Test
    fun parentsForNestedNodeReturnsAncestorsInOrder() {
        val grandparent = ComposeNode(type = ComposeType.Column)
        val parent = ComposeNode(type = ComposeType.Row, parent = grandparent)
        val child = ComposeNode(type = ComposeType.Text, parent = parent)

        val parents = child.parents()
        assertEquals(2, parents.size)
        assertEquals(parent, parents[0])
        assertEquals(grandparent, parents[1])
    }

    // --- Scenario 5: asList for leaf node ---

    @Test
    fun asListForLeafNodeContainsOnlyItself() {
        val leaf = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Leaf")
        )
        val list = leaf.asList()
        assertEquals(1, list.size)
        assertEquals(leaf, list[0])
    }

    // --- Scenario 6: asList for container with children ---

    @Test
    fun asListForContainerWithChildren() {
        val child1 = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "A"))
        val child2 = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "B"))
        val child3 = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "C"))
        val column = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(children = listOf(child1, child2, child3))
        )

        val list = column.asList()
        assertEquals(4, list.size)
        assertEquals(column, list[0])
        assertEquals(child1, list[1])
        assertEquals(child2, list[2])
        assertEquals(child3, list[3])
    }

    // --- Scenario 7: asList for node with single child ---

    @Test
    fun asListForNodeWithSingleChild() {
        val textChild = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Click me")
        )
        val button = ComposeNode(
            type = ComposeType.Button,
            properties = NodeProperties.ButtonProps(child = textChild)
        )

        val list = button.asList()
        assertEquals(2, list.size)
        assertEquals(button, list[0])
        assertEquals(textChild, list[1])
    }

    // --- Scenario 8: asList for deep nested tree ---

    @Test
    fun asListForDeepNestedTree() {
        val text = ComposeNode(type = ComposeType.Text, properties = NodeProperties.TextProps(text = "Deep"))
        val box = ComposeNode(
            type = ComposeType.Box,
            properties = NodeProperties.BoxProps(children = listOf(text))
        )
        val row = ComposeNode(
            type = ComposeType.Row,
            properties = NodeProperties.RowProps(children = listOf(box))
        )
        val column = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(children = listOf(row))
        )

        val list = column.asList()
        assertEquals(4, list.size)
        assertEquals(ComposeType.Column, list[0].type)
        assertEquals(ComposeType.Row, list[1].type)
        assertEquals(ComposeType.Box, list[2].type)
        assertEquals(ComposeType.Text, list[3].type)
    }

    // --- Scenario 9: id generation for root node ---

    @Test
    fun idForRootNodeIsZero() {
        val root = ComposeNode(type = ComposeType.Text)
        assertEquals("0", root.id)
    }

    // --- Scenario 10: id generation for child node ---

    @Test
    fun idForChildNodeIncludesParentIdTypeAndSiblingIndex() {
        val parent = ComposeNode(type = ComposeType.Column)
        val child = ComposeNode(
            type = ComposeType.Text,
            parent = parent
        )

        // parent.id = "0", child has no children() so size = 0, index = 0 + 1 = 1
        assertEquals("0_Text_1", child.id)
    }

    // --- Scenario 11: toString produces valid JSON ---

    @Test
    fun toStringProducesValidJson() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello")
        )

        val jsonString = node.toString()
        val decoded = Json.decodeFromString<ComposeNode>(jsonString)

        assertEquals(ComposeType.Text, decoded.type)
        val props = decoded.properties as? NodeProperties.TextProps
        assertEquals("Hello", props?.text)
    }
}
