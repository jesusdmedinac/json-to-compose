Feature: Unit tests for renderer error paths
  As a library maintainer
  I want unit tests for renderer edge cases and error handling
  To ensure renderers fail gracefully when given incorrect props or missing dependencies

  Scenario: Renderer returns early when props type is wrong
    Given a ComposeNode of type Text with ButtonProps instead of TextProps
    When ToText() is called
    Then the renderer returns without crashing and nothing is rendered

  Scenario: TextField renders nothing when valueStateHostName is null
    Given a ComposeNode of type TextField with TextFieldProps(valueStateHostName = null)
    When the node is rendered
    Then the TextField is not rendered

  Scenario: TextField renders nothing when StateHost is not registered
    Given a ComposeNode of type TextField with valueStateHostName = "missing_key"
    And no StateHost is registered with that name
    When the node is rendered
    Then the TextField is not rendered

  Scenario: TextField renders nothing when StateHost has wrong type
    Given a ComposeNode of type TextField with valueStateHostName = "bool_state"
    And a StateHost<Boolean> is registered with name "bool_state"
    When the node is rendered
    Then the TextField is not rendered due to type mismatch

  Scenario: Dialog defaults to visible when visibilityStateHostName is not registered
    Given a ComposeNode of type Dialog with visibilityStateHostName = "missing_key"
    And no StateHost is registered with that name
    When the node is rendered
    Then the Dialog is rendered (defaults to visible)

  Scenario: Dialog defaults to visible when StateHost has wrong type
    Given a ComposeNode of type Dialog with visibilityStateHostName = "string_state"
    And a StateHost<String> is registered with name "string_state"
    When the node is rendered
    Then the Dialog is rendered (defaults to visible)

  Scenario: Button renders without crash when no Behavior is registered
    Given a ComposeNode of type Button with onClickEventName = "missing_action"
    And no Behavior is registered with that name
    When the node is rendered and clicked
    Then no crash occurs

  Scenario: Image renders fallback when resource is not found
    Given a ComposeNode of type Image with resourceName = "nonexistent"
    And no drawable resource is registered with that name
    When the node is rendered
    Then a fallback placeholder is rendered instead of crashing
