Feature: MVI Architecture Foundation
  As a developer
  I want a centralized MVI architecture
  So that the editor UI state is predictable, debuggable, and has a single source of truth

  Scenario: Define central EditorState
    Given the current scattered state in multiple ScreenModels
    When the MVI refactor begins
    Then a single EditorState data class should exist holding root node, selection, and UI panels status

  Scenario: Define EditorIntent hierarchy
    Given the need to model all user actions
    When the MVI foundation is created
    Then a sealed interface EditorIntent should exist defining all user actions (Tree, Editor, UI)

  Scenario: Create central EditorScreenModel
    Given the central EditorState and EditorIntent
    When the EditorScreenModel is implemented
    Then it should expose a single StateFlow of EditorState
    And provide a single `onIntent(intent: EditorIntent)` method for state reduction
