# Phase 1.5: MVI Architecture Refactoring

This phase focuses on migrating the scattered ScreenModels to a single, unidirectional data flow (MVI) architecture using a centralized `EditorState` and `EditorIntent`.

## Feature: MVI Architecture Foundation
- [ ] Scenario: Define central EditorState
- [ ] Scenario: Define EditorIntent hierarchy
- [ ] Scenario: Create central EditorScreenModel

## Feature: MVI Tree Operations
- [ ] Scenario: Migrate Add Node
- [ ] Scenario: Migrate Delete Node
- [ ] Scenario: Migrate Reorder Node

## Feature: MVI Editor Operations
- [ ] Scenario: Migrate Update Node Type
- [ ] Scenario: Migrate Update Node Text
- [ ] Scenario: Migrate Modifiers CRUD

## Feature: MVI UI State
- [ ] Scenario: Migrate Panel Display toggles
- [ ] Scenario: Migrate Viewport Modes
- [ ] Scenario: Migrate Keyword Search
