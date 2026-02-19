Feature: Clip Modifier applied from JSON
  As a developer using json-to-compose
  I want to be able to define a clip modifier in JSON
  To clip my components to a specific shape controlled from the backend

  Scenario: Apply clip with circular shape
    Given a ComposeModifier with operation Clip(shape = "Circle")
    When applied to the Compose Modifier
    Then the component is clipped to a circular shape

  Scenario: Apply clip with rounded corners
    Given a ComposeModifier with operation Clip(shape = "RoundedCorner", cornerRadius = 12)
    When applied to the Compose Modifier
    Then the component is clipped with rounded corners of 12dp

  Scenario: Apply rectangular clip
    Given a ComposeModifier with operation Clip(shape = "Rectangle")
    When applied to the Compose Modifier
    Then the component is clipped to a rectangular shape

  Scenario: Serialize and deserialize Clip modifier
    Given a JSON with modifier Clip(shape = "RoundedCorner", cornerRadius = 8)
    When deserialized and serialized back
    Then the resulting JSON maintains shape and cornerRadius