Feature: ComposeAction model for declarative behavior definitions
  As a developer using json-to-compose
  I want a sealed class hierarchy that represents actions declaratively
  To define what happens on user interactions without writing Kotlin callbacks

  Background:
    Currently, behaviors are opaque string keys that map to Kotlin Behavior
    objects. The consumer must write the implementation for each one.
    ComposeAction replaces this with a finite set of declarative action
    types that the library can interpret and execute automatically.

  Scenario: SetState action holds a stateKey and a value
    Given I define a ComposeAction.SetState
    When I set stateKey to "counter" and value to 5
    Then the action holds stateKey "counter"
    And the action holds the value 5 as a JsonElement
    And the action can represent String, Boolean, Int, and Float values

  Scenario: ToggleState action holds a stateKey
    Given I define a ComposeAction.ToggleState
    When I set stateKey to "switch_state"
    Then the action holds stateKey "switch_state"
    And the action is a shortcut for inverting a boolean state

  Scenario: Log action holds a message string
    Given I define a ComposeAction.Log
    When I set message to "Button was clicked"
    Then the action holds message "Button was clicked"
    And the action is intended for debug output

  Scenario: Sequence action holds a list of child actions
    Given I define a ComposeAction.Sequence
    When I add a SetState and a Log action as children
    Then the action holds a list of 2 child ComposeActions
    And the children are executed in order when the action is dispatched

  Scenario: Custom action holds a type and a params map
    Given I define a ComposeAction.Custom
    When I set type to "navigate" and params to {"route": "home"}
    Then the action holds type "navigate"
    And the action holds params with key "route" and value "home"
    And the action is delegated to consumer-registered handlers

  Scenario: ComposeAction is serializable to and from JSON
    Given a ComposeAction.SetState with stateKey "visible" and value true
    When I serialize the action to JSON
    Then the JSON contains {"action": "setState", "stateKey": "visible", "value": true}
    And I can deserialize the JSON back to an equivalent ComposeAction.SetState
