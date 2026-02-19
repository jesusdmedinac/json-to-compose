Feature: Improved Real-time Preview
  As a composy editor user
  I want the preview to reflect my changes instantly
  To see the result of my edits without delay

  Scenario: Preview updates when adding a node
    Given a tree with empty Column
    When the user adds a Text node from the palette
    Then the preview shows the Text immediately

  Scenario: Preview updates when editing text
    Given a tree with Column > [Text("Original")]
    When the user changes the text to "Modified" in the property panel
    Then the preview shows "Modified" immediately

  Scenario: Preview updates when changing modifier
    Given a tree with Column > [Text("Test")] without modifiers
    When the user adds a Padding(16) modifier to the Text
    Then the preview shows the Text with padding immediately

  Scenario: Preview updates when deleting a node
    Given a tree with Column > [Text("A"), Text("B")]
    When the user deletes Text("B")
    Then the preview shows only Text("A") immediately

  Scenario: Preview updates when reordering nodes
    Given a tree with Column > [Text("Second"), Text("First")]
    When the user moves Text("First") up
    Then the preview shows Text("First") above Text("Second")

  Scenario: Preview shows visual error for components with invalid config
    Given a tree with Image without URL or resourceName
    When the preview attempts to render
    Then an error placeholder is shown instead of the Image
    And the placeholder indicates "Image: missing url or resourceName"

  Scenario: Preview respects selected device
    Given a tree with components
    When the user selects device "Tablet" in "Landscape" orientation
    Then the preview resizes to horizontal tablet size

  Scenario: Preview allows zoom and pan
    Given a tree rendered in the preview
    When the user scrolls to zoom
    Then the preview zooms in or out
    When the user drags the preview
    Then the preview pans in the direction of the drag