Feature: Modifier Deletion in Node Editor
  As a composy editor user
  I want to be able to delete modifiers from a node
  To correct styles without recreating the node from scratch

  Scenario: Delete a modifier from the list
    Given a selected Text node with modifiers [Padding(16), FillMaxWidth, BackgroundColor("#FFFF0000")]
    When the user presses the delete button next to the Padding(16) modifier
    Then the modifier list becomes [FillMaxWidth, BackgroundColor("#FFFF0000")]
    And the preview updates without the padding

  Scenario: Delete the last modifier of a node
    Given a selected Text node with modifiers [Padding(8)]
    When the user presses the delete button next to the Padding(8) modifier
    Then the modifier list becomes empty
    And the preview updates without any modifier

  Scenario: Delete modifier and verify JSON reflects the change
    Given a node with modifiers [Width(200), Height(100)]
    When the user deletes Width(200)
    And exports the JSON
    Then the exported JSON only contains the Height(100) modifier

  Scenario: Modifier delete button has visual confirmation
    Given a node with visible modifiers in the right panel
    When the user hovers over the delete button
    Then the button shows a hover state with error color