Feature: Card Component rendered from JSON
  As a developer using json-to-compose
  I want to be able to define a Card in JSON
  To create content cards with elevation and rounded corners controlled from the backend

  Scenario: Render a basic Card with Text child
    Given a JSON with a node of type "Card" and a Text child "Card Content"
    When the node is processed by the renderer
    Then a Material 3 Card with the Text "Card Content" as child is shown

  Scenario: Render a Card with custom elevation
    Given a JSON with a node of type "Card" with CardProps(elevation = 8)
    When the node is processed by the renderer
    Then a Card with 8dp elevation is shown

  Scenario: Render a Card with rounded shape
    Given a JSON with a node of type "Card" with CardProps(cornerRadius = 16)
    When the node is processed by the renderer
    Then a Card with 16dp rounded corners is shown

  Scenario: Render a Card with multiple children in Column
    Given a JSON with a Card containing a Column with Image and Text
    When the node is processed by the renderer
    Then a Card with the rendered Column, Image, and Text is shown

  Scenario: Serialize and deserialize a Card from JSON
    Given a JSON string with type "Card" and elevation and corners properties
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all Card properties