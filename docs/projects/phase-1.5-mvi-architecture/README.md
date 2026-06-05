# Phase 1.5: MVI Architecture Refactoring

## Overview
Currently, `composy` utilizes multiple `ScreenModel`s (`EditNodeScreenModel`, `ComposeTreeScreenModel`, `ComposeComponentsScreenModel`, `MainScreenModel`), each managing their own `StateFlow`. This distributed state can lead to synchronization bugs (e.g. `selectedComposeNode` getting stale or UI panels disjointed from the core node data).

This phase (`phase-1.5-mvi-architecture`) will centralize the application state into a strict Unidirectional Data Flow pattern (MVI). The objective is to establish a solid, bug-free foundation before continuing with the Editor MVP (Phase 2).

## Architecture

We will create a central `EditorState` and `EditorIntent` to model the entire UI state and all possible user interactions.

### `EditorState`
A single data class acting as the definitive source of truth:
```kotlin
data class EditorState(
    val rootNode: ComposeNode,
    val selectedNodeId: String?,
    val isLeftPanelDisplayed: Boolean,
    val isRightPanelDisplayed: Boolean,
    val keyword: String,
    val viewportMode: ViewportMode
)
```

### `EditorIntent`
A sealed interface capturing all user actions:
```kotlin
sealed interface EditorIntent {
    // Tree Actions
    data class SelectNode(val id: String?) : EditorIntent
    data class AddNode(val parentId: String, val type: ComposeType) : EditorIntent
    data class DeleteNode(val id: String) : EditorIntent
    data class ReorderNode(val id: String, val direction: ReorderDirection) : EditorIntent
    
    // Editor Actions
    data class UpdateNodeType(val id: String, val type: ComposeType) : EditorIntent
    data class UpdateNodeText(val id: String, val text: String) : EditorIntent
    // Modifier CRUD...

    // UI Actions
    data class SetLeftPanelDisplayed(val displayed: Boolean) : EditorIntent
    // Viewport Mode changes...
}
```

## Features

This phase is broken down into 4 main features, detailed in the `features/` directory:
1. **MVI Architecture Foundation**: Base classes and the new central ScreenModel.
2. **MVI Tree Operations**: Migrating operations like Add, Delete, and Reorder.
3. **MVI Editor Operations**: Migrating properties mapping and modifiers.
4. **MVI UI State**: Viewports, panels, and keyword filtering.

Please refer to `PROGRESS.md` to track scenario completion.
