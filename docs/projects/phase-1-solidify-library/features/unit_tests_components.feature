Feature: Unit tests for existing components
  As a library maintainer
  I want unit tests for each component renderer
  To ensure components render correctly and detect regressions

  Scenario: Unit test for Text renderer
    Given a ComposeNode of type "Text" with TextProps(text = "Hello World")
    When the node is processed by ToText()
    Then a Compose Text component with content "Hello World" is rendered

  Scenario: Unit test for Text renderer without text
    Given a ComposeNode of type "Text" with TextProps(text = null)
    When the node is processed by ToText()
    Then a Compose Text component with empty content is rendered

  Scenario: Unit test for Column renderer
    Given a ComposeNode of type "Column" with ColumnProps and 3 Text children
    When the node is processed by ToColumn()
    Then a Compose Column with 3 Text children is rendered

  Scenario: Unit test for Column renderer with arrangement
    Given a ComposeNode of type "Column" with ColumnProps(verticalArrangement = "SpaceBetween")
    When the node is processed by ToColumn()
    Then a Column with Arrangement.SpaceBetween is rendered

  Scenario: Unit test for Row renderer
    Given a ComposeNode of type "Row" with RowProps and 2 Text children
    When the node is processed by ToRow()
    Then a Compose Row with 2 Text children is rendered

  Scenario: Unit test for Row renderer with alignment
    Given a ComposeNode of type "Row" with RowProps(verticalAlignment = "CenterVertically")
    When the node is processed by ToRow()
    Then a Row with Alignment.CenterVertically is rendered

  Scenario: Unit test for Box renderer
    Given a ComposeNode of type "Box" with BoxProps and 1 Text child
    When the node is processed by ToBox()
    Then a Compose Box with 1 Text child is rendered

  Scenario: Unit test for Box renderer with contentAlignment
    Given a ComposeNode of type "Box" with BoxProps(contentAlignment = "Center")
    When the node is processed by ToBox()
    Then a Box with Alignment.Center is rendered

  Scenario: Unit test for Button renderer
    Given a ComposeNode of type "Button" with ButtonProps and a Text child
    When the node is processed by ToButton()
    Then a Compose Button with the Text child is rendered

  Scenario: Unit test for Button renderer with click event
    Given a ComposeNode of type "Button" with ButtonProps(onClickEventName = "submit")
    And a mock Behavior registered
    When the user clicks on the Button
    Then Behavior.onEvent is invoked with eventName "submit"

  Scenario: Unit test for Image renderer with URL
    Given a ComposeNode of type "Image" with ImageProps(url = "https://example.com/img.png")
    When the node is processed by ToImage()
    Then a Compose Image loading the URL is rendered

  Scenario: Unit test for Image renderer with local resource
    Given a ComposeNode of type "Image" with ImageProps(resourceName = "logo")
    When the node is processed by ToImage()
    Then a Compose Image using the drawable resource "logo" is rendered

  Scenario: Unit test for TextField renderer
    Given a ComposeNode of type "TextField" with TextFieldProps(valueStateHostName = "search")
    When the user writes "kotlin" in the TextField
    Then StateHost.onTextChanged is invoked with eventName "search" and value "kotlin"

  Scenario: Unit test for Scaffold renderer
    Given a ComposeNode of type "Scaffold" with ScaffoldProps and a Column child
    When the node is processed by ToScaffold()
    Then a Compose Scaffold with the Column child is rendered

  Scenario: Unit test for LazyColumn renderer
    Given a ComposeNode of type "LazyColumn" with ColumnProps and 10 Text children
    When the node is processed by ToLazyColumn()
    Then a Compose LazyColumn with 10 items is rendered

  Scenario: Unit test for LazyRow renderer
    Given a ComposeNode of type "LazyRow" with RowProps and 5 Text children
    When the node is processed by ToLazyRow()
    Then a Compose LazyRow with 5 items is rendered

  Scenario: Unit test for Custom renderer
    Given a ComposeNode of type "Custom" with CustomProps(customType = "Rating", customData = {"stars": 5})
    And a customRenderer registered for "Rating"
    When the node is processed by ToCustom()
    Then the customRenderer is invoked with data {"stars": 5}
