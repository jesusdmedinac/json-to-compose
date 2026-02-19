Feature: Shadow Modifier applied from JSON
  As a developer using json-to-compose
  I want to be able to define a shadow modifier in JSON
  To add shadows to my components controlled from the backend

  Scenario: Apply shadow with elevation
    Given a ComposeModifier with operation Shadow(elevation = 4)
    When applied to the Compose Modifier
    Then the component shows a shadow with 4dp elevation

  Scenario: Apply shadow with rounded shape
    Given a ComposeModifier with operation Shadow(elevation = 8, shape = "RoundedCorner", cornerRadius = 16)
    When applied to the Compose Modifier
    Then the component shows a shadow with rounded corners of 16dp

  Scenario: Apply shadow with clip enabled
    Given a ComposeModifier with operation Shadow(elevation = 4, clip = true)
    When applied to the Compose Modifier
    Then the component is clipped to the shadow shape

  Scenario: Serialize and deserialize Shadow modifier
    Given a JSON with modifier Shadow(elevation = 6, shape = "RoundedCorner", cornerRadius = 12, clip = true)
    When deserialized and serialized back
    Then the resulting JSON maintains all Shadow properties