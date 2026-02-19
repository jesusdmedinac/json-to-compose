Feature: JSON Export and Import
  As a composy editor user
  I want to export and import component trees as JSON files
  To save my work, share it with others, and resume it later

  Scenario: Export full tree to JSON
    Given a tree with Column > [Text("Hello"), Row > [Text("A"), Text("B")]]
    When the user presses the export JSON button
    Then a file save dialog opens
    When the user selects location and filename
    Then a valid JSON file with the entire tree structure is saved

  Scenario: Exported JSON is valid and consumable by json-to-compose
    Given a JSON file exported from composy
    When the JSON is loaded in an app using json-to-compose via String.ToCompose()
    Then the same interface seen in the editor preview is rendered

  Scenario: Exported JSON includes all modifiers
    Given a tree where a Text has modifiers [Padding(16), BackgroundColor("#FF0000FF")]
    When exported to JSON
    Then the JSON includes the composeModifier field with Padding and BackgroundColor operations

  Scenario: Exported JSON includes all properties of each component
    Given a tree with Column(verticalArrangement = "SpaceBetween") and children
    When exported to JSON
    Then the JSON includes verticalArrangement = "SpaceBetween" in properties

  Scenario: Import JSON from file
    Given the user presses the import JSON button
    When a file selection dialog opens
    And the user selects a valid JSON file
    Then the editor tree is replaced with the structure of the imported JSON
    And the preview shows the interface of the imported JSON

  Scenario: Import invalid JSON shows error
    Given the user attempts to import a JSON file with invalid syntax
    When the file is processed
    Then a descriptive error message is shown
    And the current tree is not modified

  Scenario: Import JSON with unknown types shows warning
    Given the user imports a JSON with a node of type "VideoPlayer" (not supported)
    When the file is processed
    Then a warning showing unrecognized types is displayed
    And recognized nodes are imported correctly

  Scenario: Full export-import cycle maintains fidelity
    Given a complex tree with multiple levels, modifiers, and properties
    When exported to JSON and then the same file is imported
    Then the resulting tree is identical to the original
    And the preview shows the same interface