Feature: Component Semantics

  As a test engineer
  I want specific component properties (like TextField value, Button enabled state) to be exposed via semantics
  So that I can verify their behavior without just relying on user interaction

  Scenario: Verify TextField value semantics
    Given a TextField with value "My Value"
    When the component is rendered
    Then the semantic property "Text" should be "My Value"

  Scenario: Verify Checkbox checked semantics
    Given a Checkbox with checked "true"
    When the component is rendered
    Then the semantic property "ToggleableState" should be On

  Scenario: Verify Switch checked semantics
    Given a Switch with checked "false"
    When the component is rendered
    Then the semantic property "ToggleableState" should be Off

  Scenario: Verify Button enabled semantics
    Given a Button with enabled "false"
    When the component is rendered
    Then the semantic property "Enabled" should be false

  Scenario: Verify Image contentDescription semantics
    Given an Image with contentDescription "An accessible image"
    When the component is rendered
    Then the semantic property "ContentDescription" should be "An accessible image"

  Scenario: Verify Image contentScale semantics
    Given an Image with contentScale "Crop"
    When the component is rendered
    Then the semantic property "ContentScale" should be ContentScale.Crop
