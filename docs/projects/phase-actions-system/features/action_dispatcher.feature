Feature: Action dispatcher executes declarative actions at runtime
  As a developer using json-to-compose
  I want the library to interpret and execute ComposeActions automatically
  To avoid writing imperative Kotlin code for common UI interactions

  Background:
    The ActionDispatcher receives a ComposeAction and a context containing
    the current state hosts and custom action handlers. It executes the
    action by modifying state, logging, or delegating to handlers.
    The dispatcher is the runtime engine of the actions system.

  Scenario: Dispatcher executes SetState and updates the corresponding StateHost
    Given a state host map with "counter" initialized to 0
    And a ComposeAction.SetState with stateKey "counter" and value 5
    When the dispatcher executes the action
    Then the StateHost for "counter" has state value 5
    And the Compose UI recomposes to reflect the new value

  Scenario: Dispatcher executes ToggleState and flips a boolean StateHost
    Given a state host map with "switch_state" initialized to false
    And a ComposeAction.ToggleState with stateKey "switch_state"
    When the dispatcher executes the action
    Then the StateHost for "switch_state" has state value true
    And executing the action again sets it back to false

  Scenario: Dispatcher executes Log and outputs the message
    Given a ComposeAction.Log with message "User clicked submit"
    When the dispatcher executes the action
    Then the message "User clicked submit" is printed to the console
    And no state is modified

  Scenario: Dispatcher executes Sequence and runs all child actions in order
    Given a state host map with "loading" initialized to false and "count" initialized to 0
    And a ComposeAction.Sequence containing SetState("loading", true) and SetState("count", 1)
    When the dispatcher executes the sequence
    Then "loading" StateHost has value true
    And "count" StateHost has value 1
    And the actions were executed in the order they were defined

  Scenario: Dispatcher executes Custom action via registered handler
    Given a custom action handler registered for type "navigate"
    And a ComposeAction.Custom with type "navigate" and params {"route": "home"}
    When the dispatcher executes the action
    Then the registered handler for "navigate" is invoked
    And the handler receives the params map with "route" equal to "home"

  Scenario: Dispatcher warns on SetState for non-existent state key
    Given a state host map with "counter" initialized to 0
    And a ComposeAction.SetState with stateKey "nonexistent" and value 5
    When the dispatcher executes the action
    Then a warning is logged that state key "nonexistent" does not exist
    And no state is modified
    And no exception is thrown

  Scenario: Dispatcher warns on ToggleState for non-boolean state key
    Given a state host map with "text_value" initialized to "hello"
    And a ComposeAction.ToggleState with stateKey "text_value"
    When the dispatcher executes the action
    Then a warning is logged that state key "text_value" is not a Boolean
    And no state is modified
    And no exception is thrown
