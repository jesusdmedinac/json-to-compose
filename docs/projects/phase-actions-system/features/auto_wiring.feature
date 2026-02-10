Feature: Auto-wiring ComposeDocument state and behaviors to Compose
  As a developer using json-to-compose
  I want ComposeDocument.ToCompose to automatically set up all state and behaviors
  To render a fully interactive UI from JSON without writing Kotlin setup code

  Background:
    Today the consumer manually creates MutableStateHost instances, Behavior
    objects, and provides them via CompositionLocalProvider. Auto-wiring
    reads the initialState and actions from a ComposeDocument and does all
    of this automatically. The consumer only calls document.ToCompose().

  Scenario: ComposeDocument.ToCompose creates MutableStateHost for each initialState entry
    Given a ComposeDocument with initialState {"switch": false, "name": ""}
    When ComposeDocument.ToCompose() is called
    Then a MutableStateHost<Boolean> is created for "switch" with initial value false
    And a MutableStateHost<String> is created for "name" with initial value ""
    And both are provided via LocalStateHost

  Scenario: ComposeDocument.ToCompose creates Behavior for each actions entry
    Given a ComposeDocument with actions {"toggle": [ToggleState("switch")]}
    When ComposeDocument.ToCompose() is called
    Then a Behavior is created for "toggle"
    And the Behavior is provided via LocalBehavior
    And the Behavior dispatches the ToggleState action when invoked

  Scenario: Auto-created Behaviors dispatch their action lists through the dispatcher
    Given a ComposeDocument with initialState {"visible": false}
    And actions {"show": [SetState("visible", true), Log("shown")]}
    When the Behavior for "show" is invoked
    Then the dispatcher executes SetState("visible", true)
    And then the dispatcher executes Log("shown")
    And the StateHost for "visible" now has value true

  Scenario: Auto-created StateHosts are accessible by renderers via LocalStateHost
    Given a ComposeDocument with initialState {"text_value": "hello"}
    And root is a Text with textStateHostName "text_value"
    When ComposeDocument.ToCompose() renders
    Then the Text component displays "hello"
    And the text comes from the auto-created MutableStateHost

  Scenario: Auto-created Behaviors are accessible by renderers via LocalBehavior
    Given a ComposeDocument with actions {"click": [Log("clicked")]}
    And root is a Button with onClickEventName "click"
    When ComposeDocument.ToCompose() renders and the button is clicked
    Then the Behavior for "click" is invoked
    And "clicked" is logged

  Scenario: TextField updates work end-to-end with auto-wired String StateHost
    Given a ComposeDocument with initialState {"input": ""}
    And root is a TextField with valueStateHostName "input"
    When ComposeDocument.ToCompose() renders
    And the user types "hello" into the TextField
    Then the StateHost for "input" has value "hello"
    And the TextField displays "hello"
    And no Kotlin code was needed beyond document.ToCompose()
