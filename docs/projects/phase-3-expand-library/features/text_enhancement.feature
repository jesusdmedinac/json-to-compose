Feature: Text Component Property Enhancement
  As a developer using json-to-compose
  I want Text to support all standard typography properties
  So that I can fully control text appearance from the backend

  Scenario: Render Text with fontSize
    Given a JSON with a Text node with TextProps(text = "Hello", fontSize = 24)
    When the node is processed by the renderer
    Then a Text with 24sp font size is displayed

  Scenario: Render Text with fontWeight Bold
    Given a JSON with a Text node with TextProps(text = "Bold", fontWeight = "Bold")
    When the node is processed by the renderer
    Then a Text with bold font weight is displayed

  Scenario: Render Text with fontWeight Light
    Given a JSON with a Text node with TextProps(text = "Light", fontWeight = "Light")
    When the node is processed by the renderer
    Then a Text with light (300) font weight is displayed

  Scenario: Render Text with fontStyle Italic
    Given a JSON with a Text node with TextProps(text = "Italic", fontStyle = "Italic")
    When the node is processed by the renderer
    Then a Text with italic font style is displayed

  Scenario: Render Text with color
    Given a JSON with a Text node with TextProps(text = "Red", color = "#FFFF0000")
    When the node is processed by the renderer
    Then a Text with red color is displayed

  Scenario: Render Text with textAlign Center
    Given a JSON with a Text node with TextProps(text = "Centered", textAlign = "Center")
    When the node is processed by the renderer
    Then a Text with center alignment is displayed

  Scenario: Render Text with textAlign End
    Given a JSON with a Text node with TextProps(text = "End", textAlign = "End")
    When the node is processed by the renderer
    Then a Text with end alignment is displayed

  Scenario: Render Text with maxLines
    Given a JSON with a Text node with TextProps(text = "Long text...", maxLines = 2)
    When the node is processed by the renderer
    Then a Text that truncates after 2 lines is displayed

  Scenario: Render Text with overflow Ellipsis
    Given a JSON with a Text node with TextProps(text = "Overflow...", maxLines = 1, overflow = "Ellipsis")
    When the node is processed by the renderer
    Then a Text with ellipsis overflow is displayed

  Scenario: Render Text with letterSpacing
    Given a JSON with a Text node with TextProps(text = "Spaced", letterSpacing = 4)
    When the node is processed by the renderer
    Then a Text with 4sp letter spacing is displayed

  Scenario: Render Text with lineHeight
    Given a JSON with a Text node with TextProps(text = "Tall lines", lineHeight = 32)
    When the node is processed by the renderer
    Then a Text with 32sp line height is displayed

  Scenario: Render Text with textDecoration Underline
    Given a JSON with a Text node with TextProps(text = "Underlined", textDecoration = "Underline")
    When the node is processed by the renderer
    Then a Text with underline decoration is displayed

  Scenario: Render Text with textDecoration LineThrough
    Given a JSON with a Text node with TextProps(text = "Struck", textDecoration = "LineThrough")
    When the node is processed by the renderer
    Then a Text with strikethrough decoration is displayed

  Scenario: Render Text with minLines
    Given a JSON with a Text node with TextProps(text = "Short", minLines = 3)
    When the node is processed by the renderer
    Then a Text that occupies at least 3 lines of vertical space is displayed

  Scenario: Render Text with state-driven fontSize
    Given a JSON with a Text node with TextProps(text = "Dynamic", fontSizeStateHostName = "titleSize")
    And a StateHost "titleSize" with value 32
    When the node is processed by the renderer
    Then a Text with 32sp font size is displayed

  Scenario: Serialize and deserialize Text with all typography properties
    Given a JSON string with a Text node containing fontSize, fontWeight, color, textAlign, maxLines, and textDecoration
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all Text typography properties
