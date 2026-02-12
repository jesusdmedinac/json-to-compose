Feature: Advanced Action Types for ComposeAction
  As a developer using json-to-compose
  I want additional action types for navigation, conditionals, delays, and more
  So that I can build interactive server-driven UIs with complex behavior from JSON

  # Navigate Action
  Scenario: Navigate action changes current route
    Given a ComposeDocument with a Button with onClickEventName = "goToProfile"
    And an action "goToProfile" of type Navigate with route = "profile"
    When the user clicks the Button
    Then the navigation state changes to route "profile"

  Scenario: Navigate action with arguments
    Given a ComposeDocument with a Navigate action with route = "user/{id}" and args = {"id": "123"}
    When the action is dispatched
    Then the navigation state changes to route "user/123"

  # NavigateBack Action
  Scenario: NavigateBack action pops the navigation stack
    Given a ComposeDocument with a Button with onClickEventName = "goBack"
    And an action "goBack" of type NavigateBack
    When the user clicks the Button
    Then the navigation stack is popped to the previous route

  # Delay Action
  Scenario: Delay action waits before executing next action in sequence
    Given a ComposeDocument with a Sequence action containing [SetState("loading", true), Delay(2000), SetState("loading", false)]
    When the sequence is dispatched
    Then "loading" is set to true, waits 2 seconds, then "loading" is set to false

  # Conditional Action
  Scenario: Conditional action executes then-branch when condition is true
    Given a ComposeDocument with a Conditional action checking state "isLoggedIn" == true
    And thenAction = Navigate("dashboard") and elseAction = Navigate("login")
    And state "isLoggedIn" is true
    When the action is dispatched
    Then the navigation changes to "dashboard"

  Scenario: Conditional action executes else-branch when condition is false
    Given a ComposeDocument with a Conditional action checking state "isLoggedIn" == true
    And thenAction = Navigate("dashboard") and elseAction = Navigate("login")
    And state "isLoggedIn" is false
    When the action is dispatched
    Then the navigation changes to "login"

  # IncrementState / DecrementState Actions
  Scenario: IncrementState action increments numeric state
    Given a ComposeDocument with an IncrementState action for stateKey = "counter" with amount = 1
    And state "counter" is 5
    When the action is dispatched
    Then state "counter" becomes 6

  Scenario: DecrementState action decrements numeric state
    Given a ComposeDocument with a DecrementState action for stateKey = "quantity" with amount = 1
    And state "quantity" is 3
    When the action is dispatched
    Then state "quantity" becomes 2

  # ShowSnackbar Action (integration with Snackbar feature)
  Scenario: ShowSnackbar action displays message
    Given a ComposeDocument with a ShowSnackbar action with message = "Changes saved"
    When the action is dispatched
    Then a Snackbar with "Changes saved" appears

  # LaunchUrl Action
  Scenario: LaunchUrl action opens external URL
    Given a ComposeDocument with a Button with onClickEventName = "openDocs"
    And an action "openDocs" of type LaunchUrl with url = "https://example.com/docs"
    When the user clicks the Button
    Then the system browser opens "https://example.com/docs"

  # CopyToClipboard Action
  Scenario: CopyToClipboard action copies text
    Given a ComposeDocument with a CopyToClipboard action with text from state "shareLink"
    And state "shareLink" is "https://example.com/share/abc"
    When the action is dispatched
    Then "https://example.com/share/abc" is copied to the system clipboard

  # UpdateList Action
  Scenario: UpdateList action adds item to list state
    Given a ComposeDocument with an UpdateList action with operation = "add", stateKey = "items", value = "New Item"
    And state "items" is ["Item 1", "Item 2"]
    When the action is dispatched
    Then state "items" becomes ["Item 1", "Item 2", "New Item"]

  Scenario: UpdateList action removes item from list state
    Given a ComposeDocument with an UpdateList action with operation = "removeAt", stateKey = "items", index = 0
    And state "items" is ["Item 1", "Item 2"]
    When the action is dispatched
    Then state "items" becomes ["Item 2"]

  Scenario: Serialize and deserialize all advanced actions
    Given JSON strings for Navigate, NavigateBack, Delay, Conditional, IncrementState, DecrementState, ShowSnackbar, LaunchUrl, CopyToClipboard, and UpdateList actions
    When each is deserialized to ComposeAction and serialized back
    Then each resulting JSON maintains all action properties
