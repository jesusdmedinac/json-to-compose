package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class EditorScreenModelTest {

    @Test
    fun testUpdateNodeType_preservesChildrenIfCompatible() {
        val screenModel = EditorScreenModel()
        
        // Root is Box. We add a Column.
        screenModel.onIntent(EditorIntent.AddNode(screenModel.state.value.rootNode.id, ComposeType.Column))
        val columnNode = screenModel.state.value.rootNode.children().first()
        
        // Add a Text child to the Column
        screenModel.onIntent(EditorIntent.AddNode(columnNode.id, ComposeType.Text))
        
        // Verify state
        val updatedColumn = screenModel.state.value.rootNode.children().first()
        assertEquals(ComposeType.Column, updatedColumn.type)
        assertEquals(1, updatedColumn.children().size)
        
        // Now dispatch UpdateNodeType to change Column to Row
        screenModel.onIntent(EditorIntent.UpdateNodeType(updatedColumn.id, ComposeType.Row))
        
        val rowNode = screenModel.state.value.rootNode.children().first()
        assertEquals(ComposeType.Row, rowNode.type)
        assertIs<NodeProperties.RowProps>(rowNode.properties)
        // Children should be preserved
        assertEquals(1, rowNode.children().size)
        assertEquals(ComposeType.Text, rowNode.children().first().type)
    }

    @Test
    fun testUpdateNodeText() {
        val screenModel = EditorScreenModel()
        
        // Add a Text node
        screenModel.onIntent(EditorIntent.AddNode(screenModel.state.value.rootNode.id, ComposeType.Text))
        val textNodeId = screenModel.state.value.rootNode.children().first().id
        
        // Update its text
        screenModel.onIntent(EditorIntent.UpdateNodeText(textNodeId, "Hello MVI"))
        
        val updatedTextNode = screenModel.state.value.rootNode.children().first()
        val props = updatedTextNode.properties
        assertIs<NodeProperties.TextProps>(props)
        assertEquals("Hello MVI", props.text)
    }

    @Test
    fun testModifiersCRUD() {
        val screenModel = EditorScreenModel()
        val rootId = screenModel.state.value.rootNode.id
        
        // ADD
        screenModel.onIntent(EditorIntent.AddModifier(rootId, ModifierOperation.Padding))
        
        var ops = screenModel.state.value.rootNode.composeModifier.operations
        assertEquals(1, ops.size)
        assertIs<ComposeModifier.Operation.Padding>(ops[0])
        assertEquals(0, (ops[0] as ComposeModifier.Operation.Padding).value)
        
        // UPDATE
        val updatedOp = ComposeModifier.Operation.Padding(32)
        screenModel.onIntent(EditorIntent.UpdateModifier(rootId, 0, updatedOp))
        
        ops = screenModel.state.value.rootNode.composeModifier.operations
        assertEquals(1, ops.size)
        assertEquals(32, (ops[0] as ComposeModifier.Operation.Padding).value)
        
        // DELETE
        screenModel.onIntent(EditorIntent.DeleteModifier(rootId, 0))
        
        ops = screenModel.state.value.rootNode.composeModifier.operations
        assertTrue(ops.isEmpty())
    }
}
