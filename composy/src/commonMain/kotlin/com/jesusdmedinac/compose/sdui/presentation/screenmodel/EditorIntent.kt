package com.jesusdmedinac.compose.sdui.presentation.screenmodel


import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.modifier.ModifierOperation

sealed interface EditorIntent {
    fun accept(visitor: Visitor)

    interface Visitor {
        fun visit(intent: SelectNode)
        fun visit(intent: AddNode)
        fun visit(intent: DeleteNode)
        fun visit(intent: UpdateNodeType)
        fun visit(intent: UpdateNodeText)
        fun visit(intent: AddModifier)
        fun visit(intent: UpdateModifier)
        fun visit(intent: DeleteModifier)
        fun visit(intent: SetLeftPanelDisplayed)
        fun visit(intent: SetRightPanelDisplayed)
        fun visit(intent: SetDeviceType)
        fun visit(intent: SetDeviceOrientation)
        fun visit(intent: SetSearchKeyword)
    }

    // Tree Actions
    data class SelectNode(val id: String?) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class AddNode(val parentId: String, val type: ComposeType) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class DeleteNode(val id: String) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    
    // Editor Actions
    data class UpdateNodeType(val id: String, val type: ComposeType) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class UpdateNodeText(val id: String, val text: String) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class AddModifier(val id: String, val operation: ModifierOperation) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class UpdateModifier(val id: String, val index: Int, val operation: ComposeModifier.Operation) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class DeleteModifier(val id: String, val index: Int) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }

    // UI Actions
    data class SetLeftPanelDisplayed(val displayed: Boolean) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class SetRightPanelDisplayed(val displayed: Boolean) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class SetDeviceType(val deviceType: DeviceType) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class SetDeviceOrientation(val deviceOrientation: DeviceOrientation) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
    data class SetSearchKeyword(val keyword: String) : EditorIntent {
        override fun accept(visitor: Visitor) = visitor.visit(this)
    }
}
