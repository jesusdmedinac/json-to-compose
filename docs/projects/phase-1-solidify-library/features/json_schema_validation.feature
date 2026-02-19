Feature: JSON Schema Validation
  As a developer using json-to-compose
  I want the library to validate the JSON before rendering
  To receive clear errors when my JSON has problems instead of silent crashes

  Scenario: Valid JSON processes without errors
    Given a well-formed JSON string with type "Text" and valid TextProps
    When the JSON is validated
    Then the validation is successful and can be rendered

  Scenario: JSON with unknown component type
    Given a JSON string with type "UnknownWidget"
    When the JSON is validated
    Then a descriptive error is returned indicating that "UnknownWidget" is not a valid ComposeType
    And valid types available are listed

  Scenario: JSON with properties incompatible with type
    Given a JSON string with type "Text" but with ColumnProps as properties
    When the JSON is validated
    Then an error is returned indicating that Text requires TextProps, not ColumnProps

  Scenario: JSON with unknown modifier operation
    Given a JSON string with a modifier operation of type "Blur"
    When the JSON is validated
    Then an error is returned indicating that "Blur" is not a valid ModifierOperation

  Scenario: JSON with invalid tree structure (child in leaf node)
    Given a JSON string with a Text that has children
    When the JSON is validated
    Then an error is returned indicating that Text does not support children

  Scenario: JSON with out-of-range modifier value
    Given a JSON string with Padding(value = -10)
    When the JSON is validated
    Then an error is returned indicating that Padding does not accept negative values

  Scenario: JSON with invalid hex color
    Given a JSON string with BackgroundColor(hexColor = "red")
    When the JSON is validated
    Then an error is returned indicating that "red" is not a valid hex color
    And the expected format "#AARRGGBB" is shown

  Scenario: JSON with invalid arrangement
    Given a JSON string with Column and verticalArrangement = "Diagonal"
    When the JSON is validated
    Then an error is returned indicating that "Diagonal" is not a valid Arrangement
    And available arrangements are listed

  Scenario: Empty JSON
    Given an empty JSON string ""
    When attempting to parse
    Then a descriptive error is returned indicating that the JSON is empty

  Scenario: Malformed JSON (invalid syntax)
    Given a JSON string with broken syntax "{ type: Text }"
    When attempting to parse
    Then an error indicating the syntax problem with the error position is returned

  Scenario: Validation in strict vs permissive mode
    Given a JSON with unrecognized extra properties
    When validated in permissive mode
    Then validation is successful and extra properties are ignored
    When validated in strict mode
    Then a warning is returned listing unrecognized properties