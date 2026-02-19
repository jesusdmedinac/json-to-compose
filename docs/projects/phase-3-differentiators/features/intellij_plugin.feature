Feature: IntelliJ/Android Studio Plugin for preview
  As a developer using json-to-compose in my daily workflow
  I want to preview json-to-compose JSON directly in my IDE
  To see the result without opening the web editor or running the app

  Scenario: Preview JSON file in IDE
    Given a .json file open in IntelliJ/Android Studio containing a valid ComposeNode
    When the developer activates the preview panel (via toolbar or shortcut)
    Then a side panel is shown with the rendered component

  Scenario: Preview updates when editing JSON
    Given the preview panel is open for a JSON file
    When the developer edits the text of a Text node in the JSON
    Then the preview updates automatically showing the new text

  Scenario: Preview automatically detects json-to-compose files
    Given a JSON file with the structure {"type": "...", "properties": {...}}
    When the developer opens the file
    Then the IDE shows an icon or gutter icon indicating it is a json-to-compose JSON
    And offers the option to open the preview

  Scenario: Preview supports multiple devices
    Given the preview panel is open
    When the developer selects "Tablet" in the panel's device selector
    Then the preview resizes to the tablet size

  Scenario: Real-time JSON validation
    Given a JSON file open in the editor
    When the developer writes an invalid component type
    Then the IDE shows an inline warning indicating the type is invalid
    And suggests available types via code completion

  Scenario: Code completion for component types
    Given the developer is editing the "type" field in a json-to-compose JSON
    When they trigger autocomplete (Ctrl+Space)
    Then they see the list of available ComposeTypes (Text, Column, Row, Box, etc.)

  Scenario: Code completion for properties
    Given the developer is editing the properties of a "Column" node
    When they trigger autocomplete inside "properties"
    Then they see available properties for ColumnProps (children, verticalArrangement, horizontalAlignment)

  Scenario: Code completion for modifier operations
    Given the developer is editing the operations list of a modifier
    When they trigger autocomplete
    Then they see available operations (Padding, Width, Height, FillMaxSize, BackgroundColor, etc.)

  Scenario: Quick fix for common errors
    Given a JSON with TextProps assigned to a Column node
    When the IDE detects the incompatibility
    Then it offers a quick fix to change to ColumnProps

  Scenario: Open JSON file in web composy editor
    Given a json-to-compose JSON file open in the IDE
    When the developer selects "Open in Composy Editor" from the context menu
    Then the web composy editor opens with the JSON loaded