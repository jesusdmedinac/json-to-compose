Feature: MVI Editor Operations
  As a developer using Composy
  I want to modify the type, text, and modifiers of nodes via a unified MVI intent
  So that the UI updates predictively based on a centralized immutable state flow

  Scenario: Migrate Update Node Type
    Given a node of a specific type exists in the EditorState
    When the user dispatches the EditorIntent.UpdateNodeType to a new compatible type
    Then the node type should change to the new type
    And the children should be preserved in the new container if compatible
    And incompatible types should not be presented in the UI

  Scenario: Migrate Update Node Text
    Given a node with TextProps exists in the EditorState
    When the user dispatches the EditorIntent.UpdateNodeText with "New Text"
    Then the node's properties should be updated to reflect "New Text"
    And the node ID and parent reference should remain the same

  Scenario: Migrate Modifiers CRUD
    Given a node with a ComposeModifier exists in the EditorState
    When the user dispatches EditorIntent.AddModifier
    Then a new modifier operation should be appended
    When the user dispatches EditorIntent.UpdateModifier at an index
    Then the specific operation should be replaced
    When the user dispatches EditorIntent.DeleteModifier at an index
    Then the specific operation should be removed from the list
