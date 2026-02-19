Feature: Semantic structure tests for rendered components
  As a library maintainer
  I want structure tests that verify the composable tree hierarchy and content
  To detect unintended structural changes without relying on pixel-based snapshots
  See ADR-001 for the rationale behind this approach.

  Scenario: Structure of basic Text
    Given a ComposeNode of type Text with text "Snapshot Test"
    When the component is rendered in a test host
    Then a node with testTag "Text" exists and is displayed
    And the node text equals "Snapshot Test"

  Scenario: Structure of Column with multiple children
    Given a ComposeNode Column with 3 Text children ("A", "B", "C")
    When the component is rendered in a test host
    Then a node with testTag "Column" exists
    And there are exactly 3 nodes with testTag "Text"
    And the texts "A", "B", "C" each exist once

  Scenario: Structure of Row with SpaceEvenly arrangement
    Given a ComposeNode Row with SpaceEvenly arrangement and 3 Text children
    When the component is rendered in a test host
    Then a node with testTag "Row" exists
    And there are exactly 3 nodes with testTag "Text"

  Scenario: Structure of Box with contentAlignment Center
    Given a ComposeNode Box with contentAlignment Center and a Text child "Centered"
    When the component is rendered in a test host
    Then a node with testTag "Box" exists
    And the text "Centered" is displayed

  Scenario: Structure of Button with Text child
    Given a ComposeNode Button with a Text child "Click Me"
    When the component is rendered in a test host
    Then a node with testTag "Button" exists and has a click action
    And the text "Click Me" exists

  Scenario: Structure of component with modifiers applied
    Given a ComposeNode Text with Padding(16), BackgroundColor("#FF2196F3"), FillMaxWidth
    When the component is rendered in a test host
    Then a node with testTag "Text" exists and is displayed
    And the component renders without error

  Scenario: Structure of complex nested layout
    Given a ComposeNode Column > [Row > [Text("Title"), Text("Subtitle")], Button > Text("Action")]
    When the component is rendered in a test host
    Then nodes with testTags "Column", "Row", "Button" each exist
    And the Button node has a click action
    And the texts "Title", "Subtitle", "Action" each exist

  Scenario: Structure of LazyColumn with items
    Given a ComposeNode LazyColumn with 5 Text children with different modifiers
    When the component is rendered in a test host
    Then a node with testTag "LazyColumn" exists
    And the first visible items are displayed
