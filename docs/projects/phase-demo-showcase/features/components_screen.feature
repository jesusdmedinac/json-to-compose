Feature: Components screen with interactive demos for all 18 components
  As a developer using json-to-compose
  I want a dedicated screen showcasing every component with interactive demos
  To serve as a comprehensive reference for the library's component catalog

  Background:
    The Components screen demonstrates all 18 components supported by
    json-to-compose. Components are organized by category: Layout, Content,
    Input, Containers, Lazy Lists, Navigation, and Custom. Each demo shows
    the component in action with representative properties.

  Scenario: Layout demos (Column, Row, Box with arrangements)
    Given the Components screen is displayed
    When the Layout section renders
    Then a Column demo shows children stacked vertically with SpacedBy arrangement
    And a Row demo shows children arranged horizontally with SpaceEvenly
    And a Box demo shows overlapping children with TopStart and BottomEnd alignment
    And each layout demo has labeled children for clarity

  Scenario: Content demos (Text, Image URL, Image resource)
    Given the Components screen is displayed
    When the Content section renders
    Then a Text demo shows styled text with different font sizes and colors
    And an Image URL demo shows an image loaded from a remote URL
    And an Image resource demo shows an image loaded from local drawable resources

  Scenario: Interactive input demos (Button, TextField, Switch, Checkbox with StateHost)
    Given the Components screen is displayed
    When the Input section renders
    Then a Button demo responds to clicks via Behavior and shows click feedback
    And a TextField demo shows an editable input with state managed by StateHost
    And a Switch demo toggles between on and off states via StateHost
    And a Checkbox demo toggles between checked and unchecked states via StateHost

  Scenario: Container demos (Card, AlertDialog, mini-Scaffold)
    Given the Components screen is displayed
    When the Containers section renders
    Then a Card demo shows elevated content with rounded corners
    And an AlertDialog demo shows a dialog with title, text, confirm, and dismiss buttons
    And a mini-Scaffold demo shows a Scaffold with topBar and content

  Scenario: Lazy list demos (LazyColumn, LazyRow)
    Given the Components screen is displayed
    When the Lazy Lists section renders
    Then a LazyColumn demo shows a vertical scrolling list of items
    And a LazyRow demo shows a horizontal scrolling list of items
    And each list contains multiple dynamically generated items

  Scenario: Navigation component demos (TopAppBar, BottomBar)
    Given the Components screen is displayed
    When the Navigation section renders
    Then a TopAppBar demo shows a top bar with title and navigation icon
    And a BottomBar demo shows a bottom bar with BottomNavigationItems
    And the BottomNavigationItems demonstrate selected and unselected states

  Scenario: Custom renderer demo (ProductCard)
    Given the Components screen is displayed
    And a custom renderer for "ProductCard" is registered
    When the Custom section renders
    Then a Custom component demo shows a ProductCard rendered by the custom renderer
    And the ProductCard demonstrates how users can extend the library with custom components

  Scenario: Built entirely with ComposeNode trees
    Given all component demos are defined
    When the Components screen renders
    Then all UI elements are built using ComposeNode trees
    And every node is rendered via ComposeNode.toString().ToCompose()
    And no native Compose components are used directly for demo content
