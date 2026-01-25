Feature: Shape Modifier (Background with shape) applied from JSON
  As a developer using json-to-compose
  I want to be able to define a background with shape modifier in JSON
  To shape the background of my components controlled from the backend

  Scenario: Apply background with circular shape
    Given a ComposeModifier with operation BackgroundShape(hexColor = "#FF2196F3", shape = "Circle")
    When applied to the Compose Modifier
    Then the component has blue background with circular shape

  Scenario: Apply background with rounded corners
    Given a ComposeModifier with operation BackgroundShape(hexColor = "#FFFF5722", shape = "RoundedCorner", cornerRadius = 16)
    When applied to the Compose Modifier
    Then the component has orange background with rounded corners of 16dp

  Scenario: Apply background with custom rounded corners per side
    Given a ComposeModifier with operation BackgroundShape with topStart = 16, topEnd = 16, bottomStart = 0, bottomEnd = 0
    When applied to the Compose Modifier
    Then the component has rounded corners only at the top

  Scenario: Serialize and deserialize BackgroundShape modifier
    Given a JSON with modifier BackgroundShape with all properties
    When deserialized and serialized back
    Then the resulting JSON maintains all BackgroundShape properties