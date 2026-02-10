Feature: ComposeDocument model as the root declarative UI document
  As a developer using json-to-compose
  I want a single document object that contains state, actions, and UI tree
  To define an entire interactive screen from a single JSON payload

  Background:
    Today, a consumer provides a ComposeNode for the UI tree and separately
    creates StateHost and Behavior instances in Kotlin. ComposeDocument
    combines all three concerns into one serializable object, enabling
    fully server-driven interactive UIs.

  Scenario: ComposeDocument contains an initialState map
    Given I create a ComposeDocument
    When I define initialState with "switch_state" as false and "text_value" as ""
    Then the document holds an initialState map with 2 entries
    And values can be Boolean, String, Int, or Float types
    And each entry becomes a MutableStateHost at runtime

  Scenario: ComposeDocument contains an actions map of name to action list
    Given I create a ComposeDocument
    When I define an action named "toggle_switch" with a ToggleState action
    And I define an action named "dismiss_dialog" with a SetState and Log action
    Then the document holds an actions map with 2 entries
    And "toggle_switch" maps to a list of 1 ComposeAction
    And "dismiss_dialog" maps to a list of 2 ComposeActions

  Scenario: ComposeDocument contains a root ComposeNode
    Given I create a ComposeDocument
    When I set root to a ComposeNode of type Column with children
    Then the document holds the root ComposeNode
    And the root is the entry point for rendering the UI tree

  Scenario: ComposeDocument is serializable to and from JSON
    Given a ComposeDocument with initialState, actions, and root
    When I serialize the document to JSON
    Then the JSON contains "initialState", "actions", and "root" keys
    And I can deserialize the JSON back to an equivalent ComposeDocument
    And the deserialized root is a valid ComposeNode tree

  Scenario: ComposeDocument with empty state and empty actions is valid
    Given I create a ComposeDocument with empty initialState and empty actions
    When I set root to a ComposeNode of type Text with text "Hello"
    Then the document is valid
    And it renders as a static non-interactive UI
    And this is equivalent to using ComposeNode.ToCompose() directly
