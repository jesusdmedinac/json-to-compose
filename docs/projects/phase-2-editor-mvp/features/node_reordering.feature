Feature: Tree Node Reordering
  As a composy editor user
  I want to be able to reorder nodes within their parent container
  To adjust the layout without deleting and recreating nodes

  Scenario: Move node up within the same parent
    Given a tree with Column > [Text("A"), Text("B"), Text("C")]
    And the Text("B") node is selected
    When the user presses the move up button
    Then the tree becomes Column > [Text("B"), Text("A"), Text("C")]
    And Text("B") remains selected

  Scenario: Move node down within the same parent
    Given a tree with Column > [Text("A"), Text("B"), Text("C")]
    And the Text("B") node is selected
    When the user presses the move down button
    Then the tree becomes Column > [Text("A"), Text("C"), Text("B")]
    And Text("B") remains selected

  Scenario: Cannot move first node up
    Given a tree with Column > [Text("First"), Text("Second")]
    And the Text("First") node is selected
    When the user attempts to move up
    Then the move up button is disabled
    And the tree remains unchanged

  Scenario: Cannot move last node down
    Given a tree with Column > [Text("First"), Text("Last")]
    And the Text("Last") node is selected
    When the user attempts to move down
    Then the move down button is disabled
    And the tree remains unchanged

  Scenario: Drag and drop to reorder nodes
    Given a tree with Column > [Text("A"), Text("B"), Text("C")]
    When the user drags Text("C") and drops it before Text("A")
    Then the tree becomes Column > [Text("C"), Text("A"), Text("B")]

  Scenario: Drag and drop between different parents
    Given a tree with Column > [Row > [Text("X")], Row > [Text("Y")]]
    When the user drags Text("X") from the first Row to the second Row
    Then the tree becomes Column > [Row (empty), Row > [Text("Y"), Text("X")]]

  Scenario: Drag and drop with visual position indicator
    Given a tree with Column > [Text("A"), Text("B"), Text("C")]
    When the user drags Text("C") over the position between Text("A") and Text("B")
    Then an insertion indicator line is shown between Text("A") and Text("B")

  Scenario: Preview updates when reordering nodes
    Given a tree with Column > [Text("First"), Text("Second")]
    When the user moves Text("Second") up
    Then the preview shows Text("Second") above Text("First")