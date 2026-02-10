Feature: Styles screen with modifier gallery for all 14 modifier operations
  As a developer using json-to-compose
  I want a dedicated screen showcasing every modifier with visual demos
  To serve as a comprehensive reference for the library's styling capabilities

  Background:
    The Styles screen demonstrates all 14 modifier operations supported by
    json-to-compose. Modifiers are organized by category: Sizing, Colors,
    Shapes and Clips, Borders and Shadows, Transparency and Rotation,
    and Combined modifiers. Each demo visually shows the modifier's effect.

  Scenario: Sizing modifier demos (Padding, FillMaxWidth, Width+Height, FillMaxSize)
    Given the Styles screen is displayed
    When the Sizing section renders
    Then a Padding demo shows content with visible padding (before and after comparison)
    And a FillMaxWidth demo shows a box stretching to full width
    And a Width+Height demo shows a box with fixed dimensions
    And a FillMaxSize demo shows a box filling all available space
    And a FillMaxHeight demo is included if applicable

  Scenario: Color palette demos (5 colored boxes with hex labels)
    Given the Styles screen is displayed
    When the Colors section renders
    Then 5 boxes are displayed, each with a different BackgroundColor
    And each box shows its hex color code as a Text label
    And the colors demonstrate the BackgroundColor modifier capabilities

  Scenario: Shape and clip demos (Circle, RoundedCorner, per-corner)
    Given the Styles screen is displayed
    When the Shapes section renders
    Then a Circle clip demo shows content clipped to a circle
    And a RoundedCorner clip demo shows content with uniform rounded corners
    And a per-corner demo shows content with different radius per corner
    And the Background modifier with shape is also demonstrated

  Scenario: Border and shadow comparison
    Given the Styles screen is displayed
    When the Border and Shadow section renders
    Then a Border demo shows boxes with different border widths and colors
    And a Shadow demo shows boxes with different elevation levels
    And borders and shadows are shown side by side for visual comparison
    And different shapes are used with borders and shadows

  Scenario: Alpha and rotation gallery (25/50/75/100%, 0/15/45/90 degrees)
    Given the Styles screen is displayed
    When the Alpha and Rotation section renders
    Then an Alpha gallery shows 4 boxes at 25%, 50%, 75%, and 100% opacity
    And each box is labeled with its alpha value
    And a Rotation gallery shows 4 boxes at 0, 15, 45, and 90 degrees
    And each box is labeled with its rotation angle

  Scenario: Combined modifiers demo
    Given the Styles screen is displayed
    When the Combined section renders
    Then a demo shows a single component with multiple modifiers applied
    And the modifiers include Padding, BackgroundColor, Border, Shadow, and Clip
    And the demo illustrates how modifiers compose together

  Scenario: Built entirely with ComposeNode trees
    Given all modifier demos are defined
    When the Styles screen renders
    Then all UI elements are built using ComposeNode trees
    And every node is rendered via ComposeNode.toString().ToCompose()
    And no native Compose components are used directly for demo content
