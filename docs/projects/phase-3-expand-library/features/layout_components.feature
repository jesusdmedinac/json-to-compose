Feature: Layout Components rendered from JSON
  As a developer using json-to-compose
  I want to use Spacer, Divider, FlowRow, FlowColumn, and Surface from JSON
  So that I can build precise layouts from the backend

  Scenario: Render a Spacer with fixed height
    Given a JSON with a Spacer node with a Height modifier of 16
    When the node is processed by the renderer
    Then an empty space of 16dp height is rendered

  Scenario: Render a Spacer with fixed width in a Row
    Given a JSON with a Row containing [Text("A"), Spacer(Width=8), Text("B")]
    When the node is processed by the renderer
    Then "A" and "B" are separated by 8dp of horizontal space

  Scenario: Render a Spacer with weight modifier in a Column
    Given a JSON with a Column containing [Text("Top"), Spacer(Weight=1), Text("Bottom")]
    When the node is processed by the renderer
    Then "Top" is at the top, "Bottom" at the bottom, with Spacer filling remaining space

  Scenario: Render a HorizontalDivider
    Given a JSON with a node of type "HorizontalDivider"
    When the node is processed by the renderer
    Then a thin horizontal line separator is shown

  Scenario: Render a HorizontalDivider with custom thickness and color
    Given a JSON with a HorizontalDivider with DividerProps(thickness = 2, color = "#FF888888")
    When the node is processed by the renderer
    Then a 2dp gray horizontal divider is shown

  Scenario: Render a VerticalDivider
    Given a JSON with a Row containing [Text("Left"), VerticalDivider, Text("Right")]
    When the node is processed by the renderer
    Then a thin vertical line separator between "Left" and "Right" is shown

  Scenario: Render a FlowRow with wrapping children
    Given a JSON with a FlowRow containing 10 Chip children that exceed the row width
    When the node is processed by the renderer
    Then chips wrap to the next line when they exceed available width

  Scenario: Render a FlowRow with horizontalArrangement and verticalArrangement
    Given a JSON with a FlowRow with FlowRowProps(horizontalArrangement = "SpaceBetween", verticalArrangement = "Center")
    When the node is processed by the renderer
    Then children are spaced evenly horizontally and centered vertically within each line

  Scenario: Render a FlowColumn with wrapping children
    Given a JSON with a FlowColumn containing 10 children that exceed the column height
    When the node is processed by the renderer
    Then children wrap to the next column when they exceed available height

  Scenario: Render a Surface with tonalElevation
    Given a JSON with a Surface with SurfaceProps(tonalElevation = 4) and a Text child
    When the node is processed by the renderer
    Then a Material 3 Surface with tonal elevation color adjustment is shown

  Scenario: Render a Surface with custom shape and color
    Given a JSON with a Surface with SurfaceProps(shape = RoundedCorner(16), color = "#FFF5F5F5")
    When the node is processed by the renderer
    Then a Surface with rounded corners and light gray background is shown

  Scenario: Column with Arrangement.spacedBy
    Given a JSON with a Column with ColumnProps(verticalArrangement = "SpacedBy:8")
    When the node is processed by the renderer
    Then a Column where children are separated by 8dp of space is shown

  Scenario: Row with Arrangement.spacedBy
    Given a JSON with a Row with RowProps(horizontalArrangement = "SpacedBy:12")
    When the node is processed by the renderer
    Then a Row where children are separated by 12dp of space is shown

  Scenario: Serialize and deserialize all layout components
    Given JSON strings for Spacer, HorizontalDivider, VerticalDivider, FlowRow, FlowColumn, and Surface
    When each is deserialized to ComposeNode and serialized back
    Then each resulting JSON maintains all layout component properties
