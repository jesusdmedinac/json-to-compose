Feature: Custom action handlers for consumer-defined extensibility
  As a developer using json-to-compose
  I want to register custom action handlers for actions the library does not know about
  To extend the actions system with domain-specific behavior like navigation or HTTP calls

  Background:
    The built-in actions (SetState, ToggleState, Log, Sequence) cover common
    UI interactions. But consumers need domain-specific actions like navigation,
    API calls, or analytics tracking. ComposeAction.Custom with registered
    handlers via LocalCustomActionHandlers provides this extensibility without
    modifying the library.

  Scenario: LocalCustomActionHandlers CompositionLocal accepts a handler map
    Given I define custom action handlers for "navigate" and "analytics"
    When I provide them via LocalCustomActionHandlers
    Then the dispatcher can access the handlers from the CompositionLocal
    And the handlers are available to all ComposeDocument.ToCompose() calls in the tree

  Scenario: Custom action type is dispatched to its registered handler
    Given a custom action handler registered for type "navigate"
    And a ComposeDocument with actions {"go_home": [Custom("navigate", {"route": "home"})]}
    When the Behavior for "go_home" is invoked
    Then the "navigate" handler is called
    And the handler receives params {"route": "home"}

  Scenario: Custom action with unregistered type logs a warning
    Given no custom action handler is registered for type "sendEmail"
    And a ComposeAction.Custom with type "sendEmail" and params {"to": "test@example.com"}
    When the dispatcher executes the action
    Then a warning is logged that no handler is registered for type "sendEmail"
    And no exception is thrown
    And execution continues with any remaining actions in the sequence

  Scenario: Custom action handler receives action params map
    Given a custom action handler registered for type "httpRequest"
    And a ComposeAction.Custom with type "httpRequest" and params {"url": "/api/data", "method": "POST"}
    When the dispatcher executes the action
    Then the handler receives a params map with 2 entries
    And the handler can read "url" as "/api/data" and "method" as "POST"
