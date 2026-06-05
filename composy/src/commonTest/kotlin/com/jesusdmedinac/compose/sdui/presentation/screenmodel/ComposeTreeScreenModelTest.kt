package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ComposeTreeScreenModelTest {

    @Test
    fun testAddNodeToChildren() {
        val screenModel = ComposeTreeScreenModel()
        
        // Initial state should have a Box root node
        val rootNode = screenModel.state.value.composeNodeRoot
        assertEquals(ComposeType.Box, rootNode.type)
        val rootProps = rootNode.properties
        assertIs<NodeProperties.BoxProps>(rootProps)
        assertTrue(rootProps.children.isNullOrEmpty())

        // Add a Text node
        val newTextNode = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(),
            parent = rootNode
        )
        
        // Verify properties
        val textProps = newTextNode.properties
        assertIs<NodeProperties.TextProps>(textProps)
        
        screenModel.onAddNewNodeToChildren(newTextNode)
        
        // Verify the node was added to the root
        val updatedRoot = screenModel.state.value.composeNodeRoot
        val updatedRootProps = updatedRoot.properties as NodeProperties.BoxProps
        val children = updatedRootProps.children
        
        assertTrue(children != null && children.isNotEmpty())
        val addedChild = children!![0]
        assertEquals(ComposeType.Text, addedChild.type)
        assertIs<NodeProperties.TextProps>(addedChild.properties)
    }

    @Test
    fun testNestedNodeAddition() {
        val screenModel = ComposeTreeScreenModel()
        
        // Initial state should have a Box root node
        val rootNode = screenModel.state.value.composeNodeRoot

        // 1. Add a Column to the root Box
        val columnNode = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(),
            parent = rootNode
        )
        screenModel.onAddNewNodeToChildren(columnNode)
        
        // Re-fetch the updated Column from the tree
        val updatedRoot = screenModel.state.value.composeNodeRoot
        val childrenOfRoot = (updatedRoot.properties as NodeProperties.BoxProps).children
        val addedColumn = childrenOfRoot!![0]
        
        // 2. Add a Text node to the Column
        val newTextNode = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(),
            parent = addedColumn
        )
        screenModel.onAddNewNodeToChildren(newTextNode)
        
        // Verify the node was added deeply inside the root
        val finalRoot = screenModel.state.value.composeNodeRoot
        val finalColumn = (finalRoot.properties as NodeProperties.BoxProps).children!![0]
        val columnChildren = (finalColumn.properties as NodeProperties.ColumnProps).children
        
        assertTrue(columnChildren != null && columnChildren.isNotEmpty())
        val finalAddedChild = columnChildren[0]
        assertEquals(ComposeType.Text, finalAddedChild.type)
        assertIs<NodeProperties.TextProps>(finalAddedChild.properties)
    }
}
