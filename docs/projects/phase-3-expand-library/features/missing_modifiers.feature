Feature: Missing Modifier Operations
  As a developer using json-to-compose
  I want additional modifier operations for clickable, weight, scroll, offset, size, and more
  So that I can build any layout from JSON without needing native code

  # Clickable Modifier
  Scenario: Apply Clickable modifier with onClick event
    Given a JSON with a Text node with a Clickable modifier and onClickEventName = "textClicked"
    When the node is processed by the renderer
    Then the Text is clickable and triggers the "textClicked" behavior on tap

  Scenario: Apply Clickable modifier with ripple indication
    Given a JSON with a Box with a Clickable modifier
    When the user taps the Box
    Then a ripple effect is shown at the tap location

  # Weight Modifier
  Scenario: Apply Weight modifier in a Column
    Given a JSON with a Column containing [Text(Weight=1), Text(Weight=2)]
    When the node is processed by the renderer
    Then the first Text takes 1/3 and the second Text takes 2/3 of the available height

  Scenario: Apply Weight modifier in a Row
    Given a JSON with a Row containing [Box(Weight=1), Box(Weight=1)]
    When the node is processed by the renderer
    Then both Boxes take equal width (50% each)

  # Scroll Modifiers
  Scenario: Apply VerticalScroll modifier to a Column
    Given a JSON with a Column with a VerticalScroll modifier containing many children that exceed screen height
    When the node is processed by the renderer
    Then the Column is vertically scrollable

  Scenario: Apply HorizontalScroll modifier to a Row
    Given a JSON with a Row with a HorizontalScroll modifier containing many children that exceed screen width
    When the node is processed by the renderer
    Then the Row is horizontally scrollable

  # Offset Modifier
  Scenario: Apply Offset modifier with x and y
    Given a JSON with a Text node with an Offset modifier of x = 10 and y = 20
    When the node is processed by the renderer
    Then the Text is offset by 10dp right and 20dp down from its natural position

  Scenario: Apply negative Offset modifier
    Given a JSON with a Box with an Offset modifier of x = -8 and y = 0
    When the node is processed by the renderer
    Then the Box is offset 8dp to the left

  # Size Modifier
  Scenario: Apply Size modifier with equal width and height
    Given a JSON with a Box with a Size modifier of 64
    When the node is processed by the renderer
    Then a 64dp x 64dp Box is rendered

  Scenario: Apply Size modifier with different width and height
    Given a JSON with a Box with a Size modifier of width = 100 and height = 50
    When the node is processed by the renderer
    Then a 100dp x 50dp Box is rendered

  # WrapContent Modifiers
  Scenario: Apply WrapContentWidth modifier
    Given a JSON with a Box with FillMaxWidth and a child Box with WrapContentWidth
    When the node is processed by the renderer
    Then the child Box takes only the width of its content

  Scenario: Apply WrapContentHeight modifier
    Given a JSON with a Box with FillMaxHeight and a child Box with WrapContentHeight
    When the node is processed by the renderer
    Then the child Box takes only the height of its content

  # AspectRatio Modifier
  Scenario: Apply AspectRatio modifier 16:9
    Given a JSON with an Image with FillMaxWidth and AspectRatio modifier of 1.78
    When the node is processed by the renderer
    Then the Image fills the width and has a 16:9 aspect ratio height

  Scenario: Apply AspectRatio modifier 1:1 (square)
    Given a JSON with a Box with AspectRatio modifier of 1.0
    When the node is processed by the renderer
    Then the Box renders as a perfect square

  # ZIndex Modifier
  Scenario: Apply ZIndex modifier to overlay elements
    Given a JSON with a Box containing [Text("Behind", ZIndex=0), Text("Front", ZIndex=1)]
    When the node is processed by the renderer
    Then "Front" renders on top of "Behind"

  # MinSize and MaxSize Modifiers
  Scenario: Apply MinWidth modifier
    Given a JSON with a Button with MinWidth modifier of 120
    When the node is processed by the renderer
    Then the Button has a minimum width of 120dp regardless of content

  Scenario: Apply MinHeight modifier
    Given a JSON with a TextField with MinHeight modifier of 56
    When the node is processed by the renderer
    Then the TextField has a minimum height of 56dp

  Scenario: Apply MaxWidth modifier
    Given a JSON with a Text with MaxWidth modifier of 200
    When the node is processed by the renderer
    Then the Text wraps its content but never exceeds 200dp width

  Scenario: Apply MaxHeight modifier
    Given a JSON with a Column with VerticalScroll and MaxHeight modifier of 300
    When the node is processed by the renderer
    Then the Column scrolls within a maximum height of 300dp

  # AnimateContentSize Modifier
  Scenario: Apply AnimateContentSize modifier
    Given a JSON with a Column with AnimateContentSize modifier
    When the Column's children change (e.g., text expands)
    Then the Column smoothly animates its size change

  # Semantics Modifier
  Scenario: Apply TestTag modifier for UI testing
    Given a JSON with a Button with a TestTag modifier of "submit_button"
    When the node is processed by the renderer
    Then the Button has the semantic test tag "submit_button" for UI test frameworks

  Scenario: Serialize and deserialize all new modifiers
    Given JSON strings for Clickable, Weight, VerticalScroll, HorizontalScroll, Offset, Size, WrapContentWidth, WrapContentHeight, AspectRatio, ZIndex, MinWidth, MinHeight, MaxWidth, MaxHeight, AnimateContentSize, and TestTag modifiers
    When each is deserialized to ComposeModifier and serialized back
    Then each resulting JSON maintains all modifier properties
