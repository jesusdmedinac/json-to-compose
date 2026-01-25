Feature: Switch Component rendered from JSON
  As a developer using json-to-compose
  I want to be able to define a Switch in JSON
  To create on/off toggles controlled from the backend

  Scenario: Render a Switch in off state
    Given a JSON with a node of type "Switch" with SwitchProps(checked = false)
    When the node is processed by the renderer
    Then a Material 3 Switch in off state is shown

  Scenario: Render a Switch in on state
    Given a JSON with a node of type "Switch" with SwitchProps(checked = true)
    When the node is processed by the renderer
    Then a Material 3 Switch in on state is shown

  Scenario: Switch emits event when state changes
    Given a rendered Switch with SwitchProps(onCheckedChangeEventName = "toggle_notifications")
    And a registered Behavior
    When the user toggles the Switch from off to on
    Then Behavior.onEvent is invoked with eventName "toggle_notifications"

  Scenario: Switch reflects state from StateHost
    Given a rendered Switch with SwitchProps(onCheckedChangeEventName = "dark_mode")
    And a StateHost with initial state checked = true
    When the Switch is rendered
    Then the Switch shows on state based on the StateHost

  Scenario: Serialize and deserialize a Switch from JSON
    Given a JSON string with type "Switch" and all its properties
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all Switch properties