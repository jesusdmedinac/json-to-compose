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
            parent = rootNode
        )
        
        // Verify that default properties were created automatically (non-null)
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
}
