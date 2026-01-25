Feature: Dialog Component rendered from JSON
  As a developer using json-to-compose
  I want to be able to define a Dialog in JSON
  To show modal dialogs controlled from the backend

  Scenario: Render a basic Dialog with title and content
    Given a JSON with a node of type "Dialog" with DialogProps(title = "Confirm", content = "Do you want to continue?")
    When the node is processed by the renderer and the dialog is visible
    Then an AlertDialog with title "Confirm" and text "Do you want to continue?" is shown

  Scenario: Render a Dialog with action buttons
    Given a JSON with a Dialog with DialogProps(confirmButtonText = "Yes", dismissButtonText = "No")
    And DialogProps has onConfirmEventName = "dialog_confirm" and onDismissEventName = "dialog_dismiss"
    When the node is processed by the renderer
    Then a Dialog with buttons "Yes" and "No" is shown

  Scenario: Dialog emits event on confirm
    Given a rendered Dialog with onConfirmEventName = "confirm_delete"
    And a registered Behavior
    When the user presses the confirm button
    Then Behavior.onEvent is invoked with eventName "confirm_delete"

  Scenario: Dialog emits event on cancel
    Given a rendered Dialog with onDismissEventName = "cancel_delete"
    And a registered Behavior
    When the user presses the cancel button
    Then Behavior.onEvent is invoked with eventName "cancel_delete"

  Scenario: Dialog with custom content
    Given a JSON with a Dialog whose content is a ComposeNode Column with children
    When the node is processed by the renderer
    Then a Dialog with the rendered custom content is shown

  Scenario: Serialize and deserialize a Dialog from JSON
    Given a JSON string with type "Dialog" and all its properties
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all Dialog properties