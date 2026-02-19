Feature: Button Variant Components rendered from JSON
  As a developer using json-to-compose
  I want to use all Material 3 button variants from JSON
  So that I can build rich UIs with the correct button styles from the backend

  Scenario: Render an OutlinedButton with Text child
    Given a JSON with a node of type "OutlinedButton" and a Text child "Cancel"
    When the node is processed by the renderer
    Then a Material 3 OutlinedButton with border and "Cancel" text is shown

  Scenario: Render a TextButton with Text child
    Given a JSON with a node of type "TextButton" and a Text child "Learn more"
    When the node is processed by the renderer
    Then a Material 3 TextButton with no background and "Learn more" text is shown

  Scenario: Render an ElevatedButton with Text child
    Given a JSON with a node of type "ElevatedButton" and a Text child "Submit"
    When the node is processed by the renderer
    Then a Material 3 ElevatedButton with shadow elevation and "Submit" text is shown

  Scenario: Render a FilledTonalButton with Text child
    Given a JSON with a node of type "FilledTonalButton" and a Text child "Save"
    When the node is processed by the renderer
    Then a Material 3 FilledTonalButton with tonal color and "Save" text is shown

  Scenario: Render an IconButton with Image child
    Given a JSON with a node of type "IconButton" and an Icon child with name "favorite"
    When the node is processed by the renderer
    Then a Material 3 IconButton with the favorite icon is shown

  Scenario: Render a FloatingActionButton with Icon
    Given a JSON with a node of type "FloatingActionButton" and an Icon child with name "add"
    When the node is processed by the renderer
    Then a Material 3 FAB with the add icon is shown

  Scenario: Render an ExtendedFloatingActionButton with icon and text
    Given a JSON with a node of type "ExtendedFloatingActionButton" with ExtendedFabProps(text = "Create", iconName = "add")
    When the node is processed by the renderer
    Then a Material 3 Extended FAB with "Create" text and add icon is shown

  Scenario: Render a Button with enabled = false
    Given a JSON with a node of type "Button" with ButtonProps(enabled = false) and a Text child "Disabled"
    When the node is processed by the renderer
    Then a disabled Button that cannot be clicked is shown

  Scenario: Render a Button with state-driven enabled property
    Given a JSON with a node of type "Button" with ButtonProps(enabledStateHostName = "canSubmit")
    And a StateHost "canSubmit" with value false
    When the node is processed by the renderer
    Then a disabled Button is shown

  Scenario: Button onClick triggers associated action
    Given a ComposeDocument with a Button with onClickEventName = "submitForm"
    And an action "submitForm" that sets state "submitted" to true
    When the user clicks the Button
    Then the state "submitted" becomes true

  Scenario: FloatingActionButton with custom containerColor
    Given a JSON with a FloatingActionButton with FabProps(containerColor = "#FF6200EE")
    When the node is processed by the renderer
    Then a FAB with the specified purple background color is shown

  Scenario: Serialize and deserialize all button variants
    Given JSON strings for OutlinedButton, TextButton, ElevatedButton, FilledTonalButton, IconButton, FloatingActionButton, and ExtendedFloatingActionButton
    When each is deserialized to ComposeNode and serialized back
    Then each resulting JSON maintains all button variant properties
