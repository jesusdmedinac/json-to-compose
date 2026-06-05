Feature: MVI Tree Operations
  As a developer
  I want to handle node tree manipulations through Intents
  So that adding, deleting, and reordering nodes is predictable

  Scenario: Migrate Add Node
    Given a selected node ID
    When the AddNode intent is dispatched with a new ComposeType
    Then the EditorState should update its root node with the new child safely inserted
    And the tree should reflect the changes

  Scenario: Migrate Delete Node
    Given a node selected in the tree
    When the DeleteNode intent is dispatched
    Then the node and its children should be removed from the EditorState root node
    And the selected node ID should be cleared

  Scenario: Migrate Reorder Node
    Given a node with siblings
    When the ReorderNode intent is dispatched with UP or DOWN direction
    Then the EditorState should update its root node swapping the elements
