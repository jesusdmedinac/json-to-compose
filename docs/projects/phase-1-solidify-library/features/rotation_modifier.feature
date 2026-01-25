Feature: Rotation Modifier applied from JSON
  As a developer using json-to-compose
  I want to be able to define a rotation modifier in JSON
  To rotate my components controlled from the backend

  Scenario: Apply 0 degree rotation
    Given a ComposeModifier with operation Rotation(degrees = 0)
    When applied to the Compose Modifier
    Then the component is shown without rotation

  Scenario: Apply 90 degree rotation
    Given a ComposeModifier with operation Rotation(degrees = 90)
    When applied to the Compose Modifier
    Then the component is shown rotated 90 degrees clockwise

  Scenario: Apply 180 degree rotation
    Given a ComposeModifier with operation Rotation(degrees = 180)
    When applied to the Compose Modifier
    Then the component is shown rotated 180 degrees

  Scenario: Apply negative rotation
    Given a ComposeModifier with operation Rotation(degrees = -45)
    When applied to the Compose Modifier
    Then the component is shown rotated 45 degrees counter-clockwise

  Scenario: Serialize and deserialize Rotation modifier
    Given a JSON with modifier Rotation(degrees = 270)
    When deserialized and serialized back
    Then the resulting JSON maintains the value 270