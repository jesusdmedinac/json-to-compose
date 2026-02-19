Feature: Unit tests for NodeProperties serialization
  As a library maintainer
  I want unit tests for each NodeProperties data class
  To ensure JSON serialization and deserialization work correctly for all property types

  Scenario: TextProps serialization with all fields
    Given a TextProps with text = "Hello"
    When serialized to JSON and deserialized back
    Then all fields are preserved and @SerialName produces "TextProps"

  Scenario: TextProps default values
    Given a TextProps with no fields specified
    When deserialized from JSON with only the type discriminator
    Then text defaults to null

  Scenario: ButtonProps serialization with child
    Given a ButtonProps with onClickEventName and a child ComposeNode
    When serialized to JSON and deserialized back
    Then all fields including the nested child are preserved

  Scenario: ColumnProps serialization with children and layout options
    Given a ColumnProps with children, verticalArrangement, and horizontalAlignment
    When serialized to JSON and deserialized back
    Then all fields including the children list are preserved

  Scenario: RowProps serialization with children and layout options
    Given a RowProps with children, verticalAlignment, and horizontalArrangement
    When serialized to JSON and deserialized back
    Then all fields including the children list are preserved

  Scenario: BoxProps serialization with all fields
    Given a BoxProps with children, contentAlignment, and propagateMinConstraints
    When serialized to JSON and deserialized back
    Then all fields are preserved and propagateMinConstraints defaults to false

  Scenario: ImageProps serialization with URL
    Given an ImageProps with url, contentDescription, and contentScale
    When serialized to JSON and deserialized back
    Then all fields are preserved and contentScale defaults to "Fit"

  Scenario: TextFieldProps serialization with valueStateHostName
    Given a TextFieldProps with valueStateHostName = "my_field"
    When serialized to JSON and deserialized back
    Then the JSON key is "valueStateHostName" and the value is preserved

  Scenario: ScaffoldProps serialization with child
    Given a ScaffoldProps with a child ComposeNode
    When serialized to JSON and deserialized back
    Then the child node is preserved

  Scenario: CardProps serialization with all fields
    Given a CardProps with child, elevation = 8, and cornerRadius = 16
    When serialized to JSON and deserialized back
    Then all fields are preserved

  Scenario: DialogProps serialization with all fields
    Given a DialogProps with title, content, child, button texts, event names, and visibilityStateHostName
    When serialized to JSON and deserialized back
    Then all fields are preserved including visibilityStateHostName

  Scenario: CustomProps serialization with customData
    Given a CustomProps with customType = "MyWidget" and a JsonObject customData
    When serialized to JSON and deserialized back
    Then customType and customData are preserved

  Scenario: NodeProperties polymorphic deserialization
    Given a JSON string with type discriminator "TextProps"
    When deserialized as NodeProperties
    Then the result is an instance of TextProps
