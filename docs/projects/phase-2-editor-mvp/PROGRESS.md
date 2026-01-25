# PROGRESS - Phase 2: Functional Editor (MVP)

## Feature: Tree Node Deletion
- [ ] Scenario: Delete a leaf node (Text) from the tree
- [ ] Scenario: Delete a node with children (Nested Column)
- [ ] Scenario: Cancel deletion of node with children
- [ ] Scenario: Delete the last child of a container
- [ ] Scenario: Root node cannot be deleted
- [ ] Scenario: Delete node with keyboard shortcut
- [ ] Scenario: Preview updates after deleting a node

## Feature: Modifier Deletion in Node Editor
- [ ] Scenario: Delete a modifier from the list
- [ ] Scenario: Delete the last modifier of a node
- [ ] Scenario: Delete modifier and verify JSON reflects the change
- [ ] Scenario: Modifier delete button has visual confirmation

## Feature: Tree Node Reordering
- [ ] Scenario: Move node up within the same parent
- [ ] Scenario: Move node down within the same parent
- [ ] Scenario: Cannot move first node up
- [ ] Scenario: Cannot move last node down
- [ ] Scenario: Drag and drop to reorder nodes
- [ ] Scenario: Drag and drop between different parents
- [ ] Scenario: Drag and drop with visual position indicator
- [ ] Scenario: Preview updates when reordering nodes

## Feature: Full Property Panel per Component
- [ ] Scenario: Edit Text properties
- [ ] Scenario: Edit Column properties
- [ ] Scenario: Edit Column verticalArrangement
- [ ] Scenario: Edit Row properties
- [ ] Scenario: Edit Box properties
- [ ] Scenario: Edit Button properties
- [ ] Scenario: Edit Image properties
- [ ] Scenario: Edit TextField properties
- [ ] Scenario: Edit Scaffold properties
- [ ] Scenario: Change component type maintains compatible children
- [ ] Scenario: Change component type with incompatible children shows warning

## Feature: Improved Real-time Preview
- [ ] Scenario: Preview updates when adding a node
- [ ] Scenario: Preview updates when editing text
- [ ] Scenario: Preview updates when changing modifier
- [ ] Scenario: Preview updates when deleting a node
- [ ] Scenario: Preview updates when reordering nodes
- [ ] Scenario: Preview shows visual error for components with invalid config
- [ ] Scenario: Preview respects selected device
- [ ] Scenario: Preview allows zoom and pan

## Feature: JSON Export and Import
- [ ] Scenario: Export full tree to JSON
- [ ] Scenario: Exported JSON is valid and consumable by json-to-compose
- [ ] Scenario: Exported JSON includes all modifiers
- [ ] Scenario: Exported JSON includes all properties of each component
- [ ] Scenario: Import JSON from file
- [ ] Scenario: Import invalid JSON shows error
- [ ] Scenario: Import JSON with unknown types shows warning
- [ ] Scenario: Full export-import cycle maintains fidelity

## Feature: Critical Editor Bug Fixes
- [ ] Scenario: Node collapse state is maintained when editing
- [ ] Scenario: Collapse state uses IDs instead of object references
- [ ] Scenario: Numeric input validation for Width modifier
- [ ] Scenario: Numeric input validation for Height modifier
- [ ] Scenario: Hex color validation for BackgroundColor modifier
- [ ] Scenario: Visual feedback when trying to add child to non-container node
- [ ] Scenario: Fixed width panels are responsive
- [ ] Scenario: Clean up dead code and stub panels