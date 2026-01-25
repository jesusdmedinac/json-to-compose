Feature: Tree Node Deletion
  As a composy editor user
  I want to be able to delete nodes from the component tree
  To fix errors and reorganize my design without starting from scratch

  Scenario: Delete a leaf node (Text) from the tree
    Given a tree with Column > [Text("A"), Text("B"), Text("C")]
    And the Text("B") node is selected
    When the user presses the delete node button
    Then the tree becomes Column > [Text("A"), Text("C")]
    And the selection moves to the next sibling node Text("C")

  Scenario: Delete a node with children (Nested Column)
    Given a tree with Column > [Row > [Text("X"), Text("Y")], Text("Z")]
    And the Row node is selected
    When the user presses the delete node button
    Then a confirmation dialog is shown indicating that children will also be deleted
    When the user confirms deletion
    Then the tree becomes Column > [Text("Z")]

  Scenario: Cancel deletion of node with children
    Given a tree with Column > [Row > [Text("X")], Text("Z")]
    And the Row node is selected
    When the user presses the delete node button
    And the confirmation dialog is shown
    When the user cancels deletion
    Then the tree remains unchanged

  Scenario: Delete the last child of a container
    Given a tree with Column > [Text("Solo")]
    And the Text("Solo") node is selected
    When the user presses the delete node button
    Then the tree becomes Column without children
    And the selection moves to the parent Column

  Scenario: Root node cannot be deleted
    Given a tree with Column as root node
    And the root Column node is selected
    When the user presses the delete node button
    Then a message is shown indicating that the root node cannot be deleted
    And the tree remains unchanged

  Scenario: Delete node with keyboard shortcut
    Given a selected Text node
    When the user presses the Delete or Backspace key
    Then the node is deleted from the tree

  Scenario: Preview updates after deleting a node
    Given a tree with Column > [Text("Visible"), Text("To delete")]
    And the Text("To delete") node is selected
    When the user deletes the node
    Then the real-time preview updates showing only Text("Visible")