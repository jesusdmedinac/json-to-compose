Feature: Backward compatibility with existing Behavior and StateHost API
  As a developer using json-to-compose
  I want the new actions system to be additive and not break existing code
  To adopt the new system incrementally without rewriting existing integrations

  Background:
    The library already has consumers using the Behavior interface,
    StateHost interface, LocalBehavior, and LocalStateHost directly.
    The actions system must be a new layer on top that does not change
    the behavior of existing code. Consumers can adopt ComposeDocument
    at their own pace while their existing code continues to work.

  Scenario: Existing Behavior interface works unchanged with LocalBehavior
    Given a consumer provides Behavior objects via LocalBehavior as before
    And a ComposeNode with onClickEventName "my_button"
    When the node renders and the button is clicked
    Then the Behavior registered for "my_button" is invoked
    And the behavior is identical to how it worked before the actions system was added

  Scenario: Existing StateHost interface works unchanged with LocalStateHost
    Given a consumer provides MutableStateHost instances via LocalStateHost as before
    And a ComposeNode with checkedStateHostName "my_switch"
    When the node renders
    Then the Switch reads state from the registered StateHost
    And state changes trigger recomposition as before

  Scenario: ComposeNode.ToCompose works without a ComposeDocument wrapper
    Given a consumer uses ComposeNode.toString().ToCompose() as before
    And no ComposeDocument is involved
    When the node renders
    Then the UI displays correctly
    And the existing rendering pipeline is unchanged

  Scenario: Manual and auto-wired state and behaviors can coexist
    Given a consumer provides some Behaviors manually via LocalBehavior
    And uses ComposeDocument.ToCompose() which auto-wires additional Behaviors
    When both manual and auto-wired Behaviors are referenced by nodes
    Then manual Behaviors work as registered by the consumer
    And auto-wired Behaviors work as defined in the ComposeDocument actions
    And there are no conflicts between the two sources
