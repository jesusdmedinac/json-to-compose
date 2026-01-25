Feature: Critical Editor Bug Fixes
  As a composy editor user
  I want existing bugs to be fixed
  To have a stable and predictable editing experience

  Scenario: Node collapse state is maintained when editing
    Given a tree with Column > [Row > [Text("A"), Text("B")], Text("C")]
    And the Row node is collapsed in the visual tree
    When the user edits the text of Text("C") to "Modified"
    Then the Row node remains collapsed (it does not expand unintentionally)

  Scenario: Collapse state uses IDs instead of object references
    Given a tree with Column > [Row (collapsed) > [Text("A")]]
    When a child node inside the Row is updated (causing object recreation)
    Then the Row continues to appear as collapsed

  Scenario: Numeric input validation for Width modifier
    Given a selected node with Width modifier
    When the user types "abc" in the value field
    Then an error indicator is shown in the field
    And the value is not applied until it is a valid number

  Scenario: Numeric input validation for Height modifier
    Given a selected node with Height modifier
    When the user types "-50" in the value field
    Then an error indicator is shown indicating negatives are not accepted

  Scenario: Hex color validation for BackgroundColor modifier
    Given a selected node with BackgroundColor modifier
    When the user types "red" in the color field
    Then an error indicator is shown with the expected format "#AARRGGBB"
    And the color is not applied until it is a valid hex

  Scenario: Visual feedback when trying to add child to non-container node
    Given a selected Text node (does not support children)
    When the user clicks on a component in the palette
    Then a message is shown indicating that Text does not support children
    And it is suggested to select a container (Column, Row, Box)

  Scenario: Fixed width panels are responsive
    Given the editor window resized to a small width
    When the side panels are open
    Then the panels adjust proportionally without overlapping the preview

  Scenario: Clean up dead code and stub panels
    Given the files ChatPanel.kt, ProjectGeneratorPanel.kt, PreviewPanel.kt
    When checked if they are being used
    Then they are deleted if unused or completed if necessary
    And no imports or references to dead code remain