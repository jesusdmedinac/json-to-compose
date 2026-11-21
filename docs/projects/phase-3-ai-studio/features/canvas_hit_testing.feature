Feature: Direct Canvas Hit-Testing
  As a Composy AI Studio user
  I want to click directly on rendered components in the preview
  So that I can inspect and target them for conversational refinements

  Scenario: Click-to-inspect in preview canvas
    Given a rendered screen in the live preview canvas
    When the user clicks on a rendered component
    Then the corresponding node is selected in EditorState with a visible selection outline

  Scenario: Contextual prompt targeting
    Given a selected component in the canvas
    When the user submits a refinement prompt
    Then the prompt context automatically attaches the target component ID

  Scenario: Device and Orientation switching
    Given the preview canvas
    When the user toggles device type or orientation
    Then the canvas bounds update instantly via EditorIntent
