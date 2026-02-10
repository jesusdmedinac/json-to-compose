Feature: JSON serialization for ComposeDocument and ComposeAction
  As a developer using json-to-compose
  I want ComposeDocument to be fully serializable to and from JSON
  To enable server-driven UI where the entire interactive screen comes from a backend

  Background:
    The library already uses kotlinx.serialization for ComposeNode, ComposeType,
    ComposeModifier, and NodeProperties. ComposeDocument and ComposeAction must
    integrate with the existing serialization infrastructure and follow the
    same patterns. This enables a backend to send a complete interactive UI
    definition as a single JSON payload.

  Scenario: ComposeDocument serializes to a JSON string
    Given a ComposeDocument with initialState, actions, and root
    When I call Json.encodeToString(document)
    Then the result is a valid JSON string
    And it contains top-level keys "initialState", "actions", and "root"
    And the "root" value follows the existing ComposeNode JSON format

  Scenario: ComposeDocument deserializes from a JSON string
    Given a JSON string with "initialState", "actions", and "root" keys
    When I call Json.decodeFromString<ComposeDocument>(jsonString)
    Then the result is a valid ComposeDocument
    And initialState entries have the correct types and values
    And actions entries contain the correct ComposeAction lists
    And root is a valid ComposeNode tree

  Scenario: Round-trip serialization preserves all data
    Given a ComposeDocument with mixed state types, multiple action types, and a nested root
    When I serialize to JSON and deserialize back
    Then the deserialized document is equal to the original
    And Boolean, String, Int, and Float state values are preserved
    And SetState, ToggleState, Log, Sequence, and Custom actions are preserved

  Scenario: Malformed JSON returns a validation error
    Given a JSON string with missing required "root" key
    When I attempt to deserialize it as a ComposeDocument
    Then a descriptive error is returned
    And the error indicates which field is missing or malformed

  Scenario: ComposeDocument JSON integrates with existing ComposeNode JSON format
    Given an existing ComposeNode JSON string (as used by ComposeNode.toString())
    When I wrap it in a ComposeDocument JSON with initialState and actions
    Then the wrapped document deserializes correctly
    And the root ComposeNode matches what ComposeNode would produce standalone
    And the existing ComposeNode.toString() format is not broken
