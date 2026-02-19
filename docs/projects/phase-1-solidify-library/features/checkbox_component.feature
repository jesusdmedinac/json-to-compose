Feature: Checkbox Component rendered from JSON
  As a developer using json-to-compose
  I want to be able to define a Checkbox in JSON
  To create multiple selection controls controlled from the backend

  Scenario: Render an unchecked Checkbox
    Given a JSON with a node of type "Checkbox" with CheckboxProps(checked = false)
    When the node is processed by the renderer
    Then an unchecked Material 3 Checkbox is shown

  Scenario: Render a checked Checkbox
    Given a JSON with a node of type "Checkbox" with CheckboxProps(checked = true)
    When the node is processed by the renderer
    Then a checked Material 3 Checkbox is shown

  Scenario: Checkbox emits event when state changes
    Given a rendered Checkbox with CheckboxProps(onCheckedChangeEventName = "accept_terms")
    And a registered Behavior
    When the user checks the Checkbox
    Then Behavior.onEvent is invoked with eventName "accept_terms"

  Scenario: Checkbox with label as child
    Given a JSON with a Row containing a Checkbox and a Text "Accept terms"
    When the tree is processed by the renderer
    Then the Checkbox is shown next to the text "Accept terms"

  Scenario: Serialize and deserialize a Checkbox from JSON
    Given a JSON string with type "Checkbox" and all its properties
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all Checkbox properties