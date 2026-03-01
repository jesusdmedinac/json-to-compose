Feature: Text Semantics

  As a test engineer
  I want Text components to expose their visual properties through semantics
  So that I can verify that specific styles (fontSize, color, weight) are correctly applied

  Scenario: Verify fontSize semantics
    Given a Text component with fontSize "24.0"
    When the component is rendered
    Then the semantic property "FontSize" should be 24.sp

  Scenario: Verify fontWeight semantics
    Given a Text component with fontWeight "Bold"
    When the component is rendered
    Then the semantic property "FontWeight" should be FontWeight.Bold

  Scenario: Verify color semantics
    Given a Text component with color "0xFFFF0000"
    When the component is rendered
    Then the semantic property "Color" should be Color.Red

  Scenario: Verify textAlign semantics
    Given a Text component with textAlign "Center"
    When the component is rendered
    Then the semantic property "TextAlign" should be TextAlign.Center

  Scenario: Verify fontStyle semantics
    Given a Text component with fontStyle "Italic"
    When the component is rendered
    Then the semantic property "FontStyle" should be FontStyle.Italic

  Scenario: Verify letterSpacing semantics
    Given a Text component with letterSpacing "0.5"
    When the component is rendered
    Then the semantic property "LetterSpacing" should be 0.5.sp

  Scenario: Verify lineHeight semantics
    Given a Text component with lineHeight "32.0"
    When the component is rendered
    Then the semantic property "LineHeight" should be 32.sp

  Scenario: Verify textDecoration semantics
    Given a Text component with textDecoration "Underline"
    When the component is rendered
    Then the semantic property "TextDecoration" should be TextDecoration.Underline
