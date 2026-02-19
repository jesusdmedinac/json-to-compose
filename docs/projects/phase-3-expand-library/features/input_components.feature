Feature: Input Components rendered from JSON
  As a developer using json-to-compose
  I want to use Slider, RadioButton, SegmentedButton, DatePicker, TimePicker, and SearchBar from JSON
  So that I can build complete form interfaces from the backend

  Scenario: Render a Slider with default range 0 to 1
    Given a JSON with a node of type "Slider" with SliderProps(value = 0.5)
    When the node is processed by the renderer
    Then a Material 3 Slider at 50% position is shown

  Scenario: Render a Slider with custom valueRange
    Given a JSON with a Slider with SliderProps(value = 25, valueRangeStart = 0, valueRangeEnd = 100)
    When the node is processed by the renderer
    Then a Slider at 25% of the 0-100 range is shown

  Scenario: Render a Slider with steps
    Given a JSON with a Slider with SliderProps(value = 50, valueRangeStart = 0, valueRangeEnd = 100, steps = 4)
    When the node is processed by the renderer
    Then a Slider with 4 discrete steps (0, 25, 50, 75, 100) is shown

  Scenario: Slider value controlled by state
    Given a JSON with a Slider with SliderProps(valueStateHostName = "volume")
    And a StateHost "volume" with value 0.7
    When the node is processed by the renderer
    Then a Slider at 70% position is shown

  Scenario: Slider onValueChange triggers state update
    Given a ComposeDocument with a Slider with onValueChangeEventName = "updateVolume"
    And an action "updateVolume" that sets state "volume"
    When the user drags the Slider to 80%
    Then the state "volume" is updated to 0.8

  Scenario: Render a RadioButton selected
    Given a JSON with a RadioButton with RadioButtonProps(selected = true)
    When the node is processed by the renderer
    Then a filled Material 3 RadioButton is shown

  Scenario: Render a RadioButton unselected
    Given a JSON with a RadioButton with RadioButtonProps(selected = false)
    When the node is processed by the renderer
    Then an empty Material 3 RadioButton is shown

  Scenario: RadioButton selection controlled by state
    Given a JSON with a RadioButton with RadioButtonProps(selectedStateHostName = "option1Selected")
    And a StateHost "option1Selected" with value true
    When the node is processed by the renderer
    Then a filled RadioButton is shown

  Scenario: RadioButton onClick triggers action
    Given a ComposeDocument with a RadioButton with onClickEventName = "selectOption1"
    And an action "selectOption1" that sets state "selectedOption" to "option1"
    When the user clicks the RadioButton
    Then the state "selectedOption" becomes "option1"

  Scenario: Render a SingleChoiceSegmentedButtonRow
    Given a JSON with a SingleChoiceSegmentedButtonRow containing 3 SegmentedButton children
    When the node is processed by the renderer
    Then a Material 3 SegmentedButtonRow with 3 mutually exclusive segments is shown

  Scenario: Render a SegmentedButton with label and icon
    Given a JSON with a SegmentedButton with SegmentedButtonProps(label = "Day", iconName = "calendar", selected = true)
    When the node is processed by the renderer
    Then a selected SegmentedButton with "Day" label and calendar icon is shown

  Scenario: Render a MultiChoiceSegmentedButtonRow
    Given a JSON with a MultiChoiceSegmentedButtonRow containing 3 SegmentedButton children with multiple selected
    When the node is processed by the renderer
    Then a SegmentedButtonRow where multiple segments can be selected is shown

  Scenario: Render a DatePicker
    Given a JSON with a node of type "DatePicker" with DatePickerProps(visibilityStateHostName = "showDatePicker")
    And a StateHost "showDatePicker" with value true
    When the node is processed by the renderer
    Then a Material 3 DatePickerDialog is shown

  Scenario: DatePicker selection updates state
    Given a ComposeDocument with a DatePicker with onDateSelectedEventName = "dateChosen"
    And an action "dateChosen" that sets state "selectedDate"
    When the user selects a date
    Then the state "selectedDate" is updated with the selected date value

  Scenario: Render a TimePicker
    Given a JSON with a node of type "TimePicker" with TimePickerProps(visibilityStateHostName = "showTimePicker")
    And a StateHost "showTimePicker" with value true
    When the node is processed by the renderer
    Then a Material 3 TimePickerDialog is shown

  Scenario: Render a SearchBar
    Given a JSON with a node of type "SearchBar" with SearchBarProps(placeholder = "Search...", queryStateHostName = "searchQuery")
    When the node is processed by the renderer
    Then a Material 3 SearchBar with "Search..." placeholder is shown

  Scenario: SearchBar query updates state as user types
    Given a ComposeDocument with a SearchBar with queryStateHostName = "searchQuery"
    And a StateHost "searchQuery" with value ""
    When the user types "kotlin" in the SearchBar
    Then the state "searchQuery" becomes "kotlin"

  Scenario: Serialize and deserialize all input components
    Given JSON strings for Slider, RadioButton, SingleChoiceSegmentedButtonRow, MultiChoiceSegmentedButtonRow, SegmentedButton, DatePicker, TimePicker, and SearchBar
    When each is deserialized to ComposeNode and serialized back
    Then each resulting JSON maintains all input component properties
