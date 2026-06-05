Feature: MVI Editor Operations
  As a developer
  I want to handle node property and modifier edits through Intents
  So that modifying a node safely produces a new root tree state

  Scenario: Migrate Update Node Type
    Given a selected node
    When the UpdateNodeType intent is dispatched
    Then the EditorState should update the root node, mapping properties properly

  Scenario: Migrate Update Node Text
    Given a selected text node
    When the UpdateNodeText intent is dispatched
    Then the EditorState should update the root node with the new text

  Scenario: Migrate Modifiers CRUD
    Given a selected node
    When AddModifier, UpdateModifier, or DeleteModifier intents are dispatched
    Then the EditorState should update the root node's modifiers accordingly
