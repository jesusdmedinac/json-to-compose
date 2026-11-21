# PROGRESS - Phase 2: Functional Editor (MVP) [ARCHIVED]

> **Status (2026-11-20):** ARCHIVED & DESCOPED.  
> The remaining manual WYSIWYG editor features (reordering with hover buttons, drag & drop, manual property forms for 60 components) have been descoped and archived. In the modern AI-first generative UI paradigm (v0, Lovable, Pencil, A2UI), the AI is the primary author and Composy focuses on Generative UI in `phase-3-ai-studio`.

## Feature: Tree Node Deletion
- [x] Scenario: Delete a leaf node (Text) from the tree
- [x] Scenario: Delete a node with children (Nested Column)
- [x] Scenario: Cancel deletion of node with children
- [x] Scenario: Delete the last child of a container
- [x] Scenario: Root node cannot be deleted
- [x] Scenario: Delete node with keyboard shortcut
- [x] Scenario: Preview updates after deleting a node

## Feature: Modifier Deletion in Node Editor
- [x] Scenario: Delete a modifier from the list
- [x] Scenario: Delete the last modifier of a node
- [x] Scenario: Delete modifier and verify JSON reflects the change
- [x] Scenario: Modifier delete button has visual confirmation

## Feature: Tree Node Reordering
- ~~Scenario: Move node up within the same parent~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Move node down within the same parent~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Cannot move first node up~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Cannot move last node down~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Sibling reordering with hover buttons~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Sibling reordering with keyboard shortcut~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Cannot reorder node beyond parent bounds~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Preview updates when reordering nodes~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)

## Feature: Full Property Panel per Component
- ~~Scenario: Edit Text properties~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit Column properties~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit Column verticalArrangement~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit Row properties~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit Box properties~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit Button properties~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit Image properties~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit TextField properties~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit Scaffold properties~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Change component type maintains compatible children~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Change component type with incompatible children shows warning~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit properties for new Card components~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit properties for dynamic Input components~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit properties for custom Display components~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit properties for Navigation components~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Edit properties for new Modifier operations~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)

## Feature: Improved Real-time Preview
- ~~Scenario: Preview updates when adding a node~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Preview updates when editing text~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Preview updates when changing modifier~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Preview updates when deleting a node~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Preview updates when reordering nodes~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Preview shows visual error for components with invalid config~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Preview respects selected device~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Preview allows zoom and pan~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)

## Feature: JSON Export and Import
- ~~Scenario: Export full tree to JSON~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Exported JSON is valid and consumable by json-to-compose~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Exported JSON includes all modifiers~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Exported JSON includes all properties of each component~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Import JSON from file~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Import invalid JSON shows error~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Import JSON with unknown types shows warning~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Full export-import cycle maintains fidelity for ComposeDocument~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)

## Feature: Critical Editor Bug Fixes
- ~~Scenario: Node collapse state is maintained when editing~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Collapse state uses IDs instead of object references~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Numeric input validation for Width modifier~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Numeric input validation for Height modifier~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Hex color validation for BackgroundColor modifier~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Visual feedback when trying to add child to non-container node~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Fixed width panels are responsive~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)
- ~~Scenario: Verify AI Chat dependencies (ChatPanel, ProjectGeneratorPanel, PreviewPanel) compilation and imports~~ — descoped: obsolete manual editor paradigm superseded by phase-3-ai-studio (2026-11-20)

## Feature: Composy Unit Testing
- [x] Scenario: Test adding nodes to various container types
- ~~Scenario: Test updating nodes recursively~~ — descoped: covered by EditorScreenModelTest in phase-1.5 (2026-11-20)
- ~~Scenario: Test deleting nodes and selection behavior~~ — descoped: covered by EditorScreenModelTest in phase-1.5 (2026-11-20)
- ~~Scenario: Test node selection and expansion state~~ — descoped: covered by EditorScreenModelTest in phase-1.5 (2026-11-20)
