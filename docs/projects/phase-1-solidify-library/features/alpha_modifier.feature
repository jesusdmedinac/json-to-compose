Feature: Alpha Modifier applied from JSON
  As a developer using json-to-compose
  I want to be able to define an alpha modifier in JSON
  To control the opacity of my components from the backend

  Scenario: Apply full alpha (opaque)
    Given a ComposeModifier with operation Alpha(value = 1.0)
    When applied to the Compose Modifier
    Then the component is shown completely opaque

  Scenario: Apply partial alpha (semi-transparent)
    Given a ComposeModifier with operation Alpha(value = 0.5)
    When applied to the Compose Modifier
    Then the component is shown with 50% opacity

  Scenario: Apply zero alpha (invisible)
    Given a ComposeModifier with operation Alpha(value = 0.0)
    When applied to the Compose Modifier
    Then the component is shown completely transparent

  Scenario: Alpha with out-of-range value is clamped
    Given a ComposeModifier with operation Alpha(value = 1.5)
    When applied to the Compose Modifier
    Then the value is clamped to 1.0 and the component is shown opaque

  Scenario: Serialize and deserialize Alpha modifier
    Given a JSON with modifier Alpha(value = 0.7)
    When deserialized and serialized back
    Then the resulting JSON maintains the value 0.7