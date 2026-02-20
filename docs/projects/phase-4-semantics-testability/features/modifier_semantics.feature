Feature: Modifier Semantics

  As a test engineer
  I want Modifier operations to expose their properties through semantics
  So that I can verify that layout modifications (padding, size, background) are correctly applied

  Scenario: Verify Padding semantics
    Given a Box with Padding "16"
    When the component is rendered
    Then the semantic property "Padding" should be 16.dp

  Scenario: Verify Width/Height semantics
    Given a Box with Width "100" and Height "200"
    When the component is rendered
    Then the semantic property "Size" should be (100.dp, 200.dp)

  Scenario: Verify FillMax semantics
    Given a Box with FillMaxSize
    When the component is rendered
    Then the semantic property "FillMaxWidth" and "FillMaxHeight" should be true

  Scenario: Verify BackgroundColor semantics
    Given a Box with BackgroundColor "0xFF00FF00"
    When the component is rendered
    Then the semantic property "BackgroundColor" should be Color.Green

  Scenario: Verify Border semantics
    Given a Box with Border "2.dp" and color "0xFF0000FF"
    When the component is rendered
    Then the semantic property "Border" should be (2.dp, Color.Blue)

  Scenario: Verify Clip semantics
    Given a Box with Clip "Circle"
    When the component is rendered
    Then the semantic property "Clip" should be "Circle"

  Scenario: Verify Shadow semantics
    Given a Box with Shadow "4.dp"
    When the component is rendered
    Then the semantic property "Shadow" should be 4.dp
