Feature: Unit tests for existing modifiers
  As a library maintainer
  I want unit tests for each modifier operation
  To ensure that modifiers are correctly applied to the Compose UI

  Scenario: Unit test for Padding modifier
    Given a ComposeModifier with operation Padding(value = 16)
    When applied to the Compose Modifier
    Then the resulting Modifier has 16dp padding on all sides

  Scenario: Unit test for FillMaxSize modifier
    Given a ComposeModifier with operation FillMaxSize
    When applied to the Compose Modifier
    Then the resulting Modifier has fillMaxSize applied

  Scenario: Unit test for FillMaxWidth modifier
    Given a ComposeModifier with operation FillMaxWidth
    When applied to the Compose Modifier
    Then the resulting Modifier has fillMaxWidth applied

  Scenario: Unit test for FillMaxHeight modifier
    Given a ComposeModifier with operation FillMaxHeight
    When applied to the Compose Modifier
    Then the resulting Modifier has fillMaxHeight applied

  Scenario: Unit test for Width modifier
    Given a ComposeModifier with operation Width(value = 200)
    When applied to the Compose Modifier
    Then the resulting Modifier has width of 200dp

  Scenario: Unit test for Height modifier
    Given a ComposeModifier with operation Height(value = 100)
    When applied to the Compose Modifier
    Then the resulting Modifier has height of 100dp

  Scenario: Unit test for BackgroundColor modifier
    Given a ComposeModifier with operation BackgroundColor(hexColor = "#FF0000FF")
    When applied to the Compose Modifier
    Then the resulting Modifier has blue background color (0xFF0000FF)

  Scenario: Unit test for BackgroundColor with invalid color
    Given a ComposeModifier with operation BackgroundColor(hexColor = "invalid")
    When applied to the Compose Modifier
    Then the resulting Modifier has white background color (fallback)

  Scenario: Unit test for multiple modifiers combined
    Given a ComposeModifier with operations [Padding(8), FillMaxWidth, BackgroundColor("#FFFF0000")]
    When applied to the Compose Modifier in order
    Then the resulting Modifier has padding, fillMaxWidth, and backgroundColor applied in that order