Feature: Demo app showcase
  As a developer evaluating json-to-compose
  I want a well-organized demo app that showcases all library capabilities
  So that I can quickly understand what the library can do and how it works

  Background:
    The demo app lives in the composeApp/ module and should serve as a
    component catalog that demonstrates every component and modifier
    the library supports. It should be visually clear, navigable, and
    built entirely using json-to-compose (dogfooding the library).

  Scenario: App has a main screen with categorized sections
    Given the demo app is launched
    When the main screen is displayed
    Then the user sees sections organized by category:
      | Category    | Description                        |
      | Layouts     | Column, Row, Box                   |
      | Content     | Text, Image                        |
      | Input       | Button, TextField, Switch, Checkbox|
      | Containers  | Card, Scaffold, Dialog             |
      | Navigation  | TopAppBar, BottomBar               |
      | Lazy Lists  | LazyColumn, LazyRow                |
      | Custom      | Custom renderer example            |

  Scenario: Each section has a title and visual separator
    Given the demo app main screen is displayed
    When the user scrolls through the component catalog
    Then each category section has a visible title header
    And sections are separated by dividers or spacing

  Scenario: Layout components section shows Column, Row, and Box
    Given the user is viewing the Layouts section
    Then a Column example shows 3 vertically stacked items with arrangement
    And a Row example shows 3 horizontally arranged items with alignment
    And a Box example shows overlapping children with contentAlignment

  Scenario: Content components section shows Text and Image
    Given the user is viewing the Content section
    Then a Text example shows styled text
    And an Image example shows an image loaded from URL
    And an Image example shows an image loaded from local resource

  Scenario: Input components section shows interactive elements
    Given the user is viewing the Input section
    Then a Button example is clickable and shows a feedback message
    And a TextField example accepts text input and reflects state
    And a Switch example toggles between on and off states
    And a Checkbox example toggles between checked and unchecked

  Scenario: Containers section shows Card, Scaffold, and Dialog
    Given the user is viewing the Containers section
    Then a Card example shows elevated content with rounded corners
    And a Scaffold example shows a basic scaffold structure
    And a Dialog example can be triggered by a button

  Scenario: Navigation section shows TopAppBar and BottomBar
    Given the user is viewing the Navigation section
    Then a TopAppBar example shows a title and navigation icon
    And a BottomBar example shows navigation items

  Scenario: Lazy lists section shows LazyColumn and LazyRow
    Given the user is viewing the Lazy Lists section
    Then a LazyColumn example shows a scrollable vertical list of items
    And a LazyRow example shows a scrollable horizontal list of items

  Scenario: Modifiers showcase demonstrates all modifier operations
    Given the user is viewing the Modifiers section
    Then examples demonstrate each modifier:
      | Modifier        | Visual effect                       |
      | Padding         | Visible spacing around text         |
      | FillMaxWidth    | Element spanning full width         |
      | BackgroundColor | Colored background                  |
      | Border          | Visible border around element       |
      | Shadow          | Drop shadow on element              |
      | Clip            | Circular and rounded clipped shapes |
      | Alpha           | Semi-transparent element            |
      | Rotation        | Rotated element                     |

  Scenario: Demo app is built entirely with json-to-compose
    Given the demo app component catalog
    When inspecting the implementation
    Then all showcase sections are rendered via ComposeNode.ToCompose()
    And no section uses hardcoded Compose UI (except the catalog scaffold itself)

  Scenario: Demo app compiles and runs on all platforms
    Given the demo app with the component catalog
    When built for Desktop, Android, iOS, and WASM
    Then it compiles without errors on all 4 platforms
