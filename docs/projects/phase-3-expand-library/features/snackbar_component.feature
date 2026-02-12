Feature: Snackbar Component and Integration from JSON
  As a developer using json-to-compose
  I want to trigger and display Snackbars from JSON actions
  So that I can show transient messages controlled from the backend

  Scenario: Render a Scaffold with SnackbarHost
    Given a JSON with a Scaffold with ScaffoldProps(snackbarHostStateHostName = "snackbarState")
    When the node is processed by the renderer
    Then a Scaffold with a SnackbarHost ready to display snackbars is rendered

  Scenario: Show a Snackbar via ShowSnackbar action
    Given a ComposeDocument with a Button with onClickEventName = "showMessage"
    And an action "showMessage" of type ShowSnackbar with message = "Item saved"
    When the user clicks the Button
    Then a Snackbar with "Item saved" message appears at the bottom

  Scenario: Show a Snackbar with action button
    Given a ComposeDocument with a ShowSnackbar action with message = "Item deleted" and actionLabel = "Undo"
    When the Snackbar is shown
    Then a Snackbar with "Item deleted" message and "Undo" action button is displayed

  Scenario: Snackbar action button triggers follow-up action
    Given a ComposeDocument with a ShowSnackbar action with actionLabel = "Undo" and onActionEventName = "undoDelete"
    And an action "undoDelete" that sets state "itemDeleted" to false
    When the user clicks the "Undo" button on the Snackbar
    Then the state "itemDeleted" becomes false

  Scenario: Snackbar with duration Short
    Given a ComposeDocument with a ShowSnackbar action with duration = "Short"
    When the Snackbar is shown
    Then the Snackbar auto-dismisses after a short duration

  Scenario: Snackbar with duration Indefinite
    Given a ComposeDocument with a ShowSnackbar action with duration = "Indefinite"
    When the Snackbar is shown
    Then the Snackbar remains visible until explicitly dismissed

  Scenario: Snackbar with withDismissAction = true
    Given a ComposeDocument with a ShowSnackbar action with withDismissAction = true
    When the Snackbar is shown
    Then a Snackbar with a dismiss (X) icon is displayed

  Scenario: Serialize and deserialize ShowSnackbar action
    Given a JSON string with a ShowSnackbar action containing message, actionLabel, duration, and withDismissAction
    When deserialized to ComposeAction and serialized back
    Then the resulting JSON maintains all ShowSnackbar action properties
