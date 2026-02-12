Feature: Card Variant Components rendered from JSON
  As a developer using json-to-compose
  I want to use ElevatedCard and OutlinedCard from JSON
  So that I can choose the correct card style from the backend

  Scenario: Render an ElevatedCard with Text child
    Given a JSON with a node of type "ElevatedCard" and a Text child "Elevated content"
    When the node is processed by the renderer
    Then a Material 3 ElevatedCard with default elevation and the Text child is shown

  Scenario: Render an ElevatedCard with custom elevation
    Given a JSON with a node of type "ElevatedCard" with CardProps(elevation = 12)
    When the node is processed by the renderer
    Then an ElevatedCard with 12dp elevation is shown

  Scenario: Render an OutlinedCard with Text child
    Given a JSON with a node of type "OutlinedCard" and a Text child "Outlined content"
    When the node is processed by the renderer
    Then a Material 3 OutlinedCard with border and no elevation and the Text child is shown

  Scenario: Render an OutlinedCard with custom border color
    Given a JSON with a node of type "OutlinedCard" with OutlinedCardProps(borderColor = "#FF000000")
    When the node is processed by the renderer
    Then an OutlinedCard with black border is shown

  Scenario: Card variants support cornerRadius
    Given a JSON with an ElevatedCard with CardProps(cornerRadius = 24)
    When the node is processed by the renderer
    Then an ElevatedCard with 24dp rounded corners is shown

  Scenario: Serialize and deserialize card variants
    Given JSON strings for ElevatedCard and OutlinedCard with all their properties
    When each is deserialized to ComposeNode and serialized back
    Then each resulting JSON maintains all card variant properties
