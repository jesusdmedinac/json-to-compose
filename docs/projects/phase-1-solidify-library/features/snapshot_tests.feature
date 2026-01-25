Feature: Snapshot tests for rendered components
  As a library maintainer
  I want snapshot tests that capture the visual output of components
  To detect unintended visual changes in regressions

  Scenario: Snapshot of basic Text
    Given a ComposeNode of type Text with text "Snapshot Test"
    When the component is rendered in a test host
    Then the snapshot matches the reference image "text_basic.png"

  Scenario: Snapshot of Column with multiple children
    Given a ComposeNode Column with 3 Text children ("A", "B", "C")
    When the component is rendered in a test host
    Then the snapshot matches the reference image "column_three_texts.png"

  Scenario: Snapshot of Row with SpaceEvenly arrangement
    Given a ComposeNode Row with SpaceEvenly arrangement and 3 Text children
    When the component is rendered in a test host
    Then the snapshot matches the reference image "row_space_evenly.png"

  Scenario: Snapshot of Box with contentAlignment Center
    Given a ComposeNode Box with contentAlignment Center and a Text child
    When the component is rendered in a test host
    Then the snapshot matches the reference image "box_center.png"

  Scenario: Snapshot of Button with Text child
    Given a ComposeNode Button with a Text child "Click Me"
    When the component is rendered in a test host
    Then the snapshot matches the reference image "button_text.png"

  Scenario: Snapshot of component with modifiers applied
    Given a ComposeNode Text with Padding(16), BackgroundColor("#FF2196F3"), FillMaxWidth
    When the component is rendered in a test host
    Then the snapshot matches the reference image "text_with_modifiers.png"

  Scenario: Snapshot of complex nested layout
    Given a ComposeNode Scaffold > Column > [Row > [Image, Column > [Text, Text]], Button > Text]
    When the component is rendered in a test host
    Then the snapshot matches the reference image "complex_layout.png"

  Scenario: Snapshot of LazyColumn with items
    Given a ComposeNode LazyColumn with 5 children having different modifiers
    When the component is rendered in a test host
    Then the snapshot matches the reference image "lazy_column_items.png"