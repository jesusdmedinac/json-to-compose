# Phase 1.5: MVI Architecture Refactoring

This phase focuses on migrating the scattered ScreenModels to a single, unidirectional data flow (MVI) architecture using a centralized `EditorState` and `EditorIntent`.

## Feature: MVI Architecture Foundation
- [x] Scenario: Define central EditorState
- [x] Scenario: Define EditorIntent hierarchy
- [x] Scenario: Create central EditorScreenModel

## Feature: MVI Tree Operations
- [x] Scenario: Migrate Add Node
- [x] Scenario: Migrate Delete Node
- [x] Scenario: Migrate Reorder Node

## Feature: MVI Editor Operations
- [x] Scenario: Migrate Update Node Type
- [x] Scenario: Migrate Update Node Text
- [x] Scenario: Migrate Modifiers CRUD

## Feature: MVI UI State
- [ ] Scenario: Migrate Panel Display toggles
- [ ] Scenario: Migrate Viewport Modes
- [ ] Scenario: Migrate Keyword Search

## Feature: MVI UI Integration
- [ ] Scenario: Tree panel dispatches intents
- [ ] Scenario: Node editor dispatches intents
- [ ] Scenario: Preview renders from EditorState
- [ ] Scenario: Legacy ScreenModels removed

> **Exit criterion:** the phase is complete only when the UI runs entirely on
> `EditorScreenModel` and the legacy ScreenModels are deleted — not merely when
> all state has been migrated into `EditorState`.
