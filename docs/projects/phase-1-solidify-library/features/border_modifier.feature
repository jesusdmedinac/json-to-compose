Feature: Border Modifier applied from JSON
  As a developer using json-to-compose
  I want to be able to define a border modifier in JSON
  To add borders to my components controlled from the backend

  Scenario: Apply border with width and color
    Given a ComposeModifier with operation Border(width = 2, hexColor = "#FF000000")
    When applied to the Compose Modifier
    Then the component shows a 2dp black border

  Scenario: Apply border with rounded corners
    Given a ComposeModifier with operation Border(width = 1, hexColor = "#FF2196F3", cornerRadius = 8)
    When applied to the Compose Modifier
    Then the component shows a 1dp blue border with rounded corners of 8dp

  Scenario: Serialize and deserialize Border modifier
    Given a JSON with modifier Border(width = 2, hexColor = "#FFFF0000", cornerRadius = 12)
    When deserialized and serialized back
    Then the resulting JSON maintains width, hexColor, and cornerRadius