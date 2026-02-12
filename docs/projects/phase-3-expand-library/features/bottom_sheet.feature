Feature: ModalBottomSheet Component rendered from JSON
  As a developer using json-to-compose
  I want to use ModalBottomSheet from JSON
  So that I can show contextual content that slides up from the bottom

  Scenario: Render a ModalBottomSheet with content
    Given a JSON with a ModalBottomSheet containing a Column with options
    And BottomSheetProps(visibilityStateHostName = "showSheet") with state "showSheet" = true
    When the node is processed by the renderer
    Then a Material 3 ModalBottomSheet slides up from the bottom with the content

  Scenario: ModalBottomSheet hidden when state is false
    Given a JSON with a ModalBottomSheet with BottomSheetProps(visibilityStateHostName = "showSheet")
    And a StateHost "showSheet" with value false
    When the node is processed by the renderer
    Then the ModalBottomSheet is not visible

  Scenario: ModalBottomSheet dismiss updates state
    Given a ComposeDocument with a ModalBottomSheet with onDismissRequestEventName = "closeSheet"
    And an action "closeSheet" that sets state "showSheet" to false
    When the user swipes down or taps the scrim
    Then the state "showSheet" becomes false and the sheet is hidden

  Scenario: ModalBottomSheet with dragHandle
    Given a JSON with a ModalBottomSheet with BottomSheetProps(showDragHandle = true)
    When the node is processed by the renderer
    Then a ModalBottomSheet with a drag handle bar at the top is shown

  Scenario: ModalBottomSheet with custom shape
    Given a JSON with a ModalBottomSheet with BottomSheetProps(shape = RoundedCorner(topStart = 28, topEnd = 28))
    When the node is processed by the renderer
    Then a ModalBottomSheet with 28dp rounded top corners is shown

  Scenario: ModalBottomSheet with scrimColor
    Given a JSON with a ModalBottomSheet with BottomSheetProps(scrimColor = "#80000000")
    When the node is processed by the renderer
    Then a ModalBottomSheet with a semi-transparent black scrim behind it is shown

  Scenario: Scaffold with ModalBottomSheet integration
    Given a JSON with a Scaffold containing main content and a ModalBottomSheet
    When the sheet visibility state is toggled
    Then the ModalBottomSheet appears over the Scaffold content

  Scenario: Serialize and deserialize ModalBottomSheet
    Given a JSON string with a ModalBottomSheet containing all properties
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all ModalBottomSheet properties
