Feature: Integration tests of JSON to Compose pipeline
  As a library maintainer
  I want integration tests that validate the full flow from JSON string to Compose UI
  To ensure serialization, routing, and rendering work together

  Scenario: Full pipeline for simple Text
    Given a JSON string '{"type":"Text","properties":{"type":"TextProps","text":"Hello"}}'
    When String.ToCompose() is invoked
    Then a Compose Text with content "Hello" is rendered

  Scenario: Full pipeline for Column with children
    Given a JSON string with a Column containing 2 Text nodes
    When String.ToCompose() is invoked
    Then a Column with 2 Text children is rendered

  Scenario: Full pipeline for deep nested tree
    Given a JSON string with Column > Row > Box > Text("Deep")
    When String.ToCompose() is invoked
    Then the full hierarchy Column > Row > Box > Text is rendered

  Scenario: Full pipeline with modifiers
    Given a JSON string with a Text having Padding(16) and BackgroundColor("#FF00FF00")
    When String.ToCompose() is invoked
    Then a Text with 16dp padding and green background is rendered

  Scenario: Full pipeline with Button and Behavior
    Given a JSON string with a Button(onClickEventName = "click_action") and a Text child
    And a Behavior registered via CompositionLocal
    When String.ToCompose() is invoked and user clicks
    Then the Behavior receives the event "click_action"

  Scenario: Full pipeline with TextField and StateHost
    Given a JSON string with a TextField(onTextChangeEventName = "input_field")
    And a StateHost registered via CompositionLocal
    When String.ToCompose() is invoked and user types text
    Then the StateHost receives the update for field "input_field"

  Scenario: Full pipeline with Image from URL
    Given a JSON string with an Image(url = "https://example.com/photo.jpg")
    When String.ToCompose() is invoked
    Then an AsyncImage loading the URL is rendered

  Scenario: Full pipeline with Custom component
    Given a JSON string with Custom(customType = "StarRating", customData = {"rating": 4.5})
    And a customRenderer registered for "StarRating" via CompositionLocal
    When String.ToCompose() is invoked
    Then the customRenderer receives type "StarRating" and data {"rating": 4.5}

  Scenario: Full pipeline with LazyColumn with many items
    Given a JSON string with a LazyColumn containing 50 Text nodes
    When String.ToCompose() is invoked
    Then a LazyColumn virtualizing 50 items is rendered

  Scenario: Roundtrip serialization: ComposeNode to JSON and back
    Given a ComposeNode tree with Column > [Text("A"), Row > [Text("B"), Text("C")]]
    When serialized to JSON and then deserialized back to ComposeNode
    Then the resulting tree is identical to the original