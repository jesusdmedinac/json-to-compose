Feature: Layout Semantics

  As a test engineer
  I want Layout components (Row, Column, Box) to expose their arrangement and alignment through semantics
  So that I can verify that children are positioned correctly

  Scenario: Verify Column verticalArrangement semantics
    Given a Column with verticalArrangement "SpaceBetween"
    When the component is rendered
    Then the semantic property "VerticalArrangement" should be "SpaceBetween"

  Scenario: Verify Column horizontalAlignment semantics
    Given a Column with horizontalAlignment "CenterHorizontally"
    When the component is rendered
    Then the semantic property "HorizontalAlignment" should be "CenterHorizontally"

  Scenario: Verify Row horizontalArrangement semantics
    Given a Row with horizontalArrangement "SpaceEvenly"
    When the component is rendered
    Then the semantic property "HorizontalArrangement" should be "SpaceEvenly"

  Scenario: Verify Row verticalAlignment semantics
    Given a Row with verticalAlignment "CenterVertically"
    When the component is rendered
    Then the semantic property "VerticalAlignment" should be "CenterVertically"

  Scenario: Verify Box contentAlignment semantics
    Given a Box with contentAlignment "BottomEnd"
    When the component is rendered
    Then the semantic property "ContentAlignment" should be "BottomEnd"
