Feature: TopAppBar Component rendered from JSON
  As a developer using json-to-compose
  I want to be able to define a TopAppBar in JSON
  To create top navigation bars controlled from the backend

  Scenario: Render a TopAppBar with title
    Given a JSON with a node of type "TopAppBar" with TopAppBarProps(title = "My App")
    When the node is processed by the renderer
    Then a Material 3 TopAppBar with title "My App" is shown

  Scenario: Render a TopAppBar with navigation icon
    Given a JSON with a TopAppBar with TopAppBarProps(navigationIconEventName = "nav_back")
    When the node is processed by the renderer
    Then a TopAppBar with navigation icon (back arrow) is shown

  Scenario: TopAppBar emits event when navigation icon is pressed
    Given a rendered TopAppBar with navigationIconEventName = "go_back"
    And a registered Behavior
    When the user presses the navigation icon
    Then Behavior.onEvent is invoked with eventName "go_back"

  Scenario: Render a TopAppBar with actions
    Given a JSON with a TopAppBar with TopAppBarProps including actions as list of ComposeNodes
    When the node is processed by the renderer
    Then a TopAppBar with actions rendered on the right is shown

  Scenario: TopAppBar integrated with Scaffold
    Given a JSON with a Scaffold that has a TopAppBar as topBar
    When the tree is processed by the renderer
    Then a Scaffold with the TopAppBar positioned at the top is shown

  Scenario: Serialize and deserialize a TopAppBar from JSON
    Given a JSON string with type "TopAppBar" and all its properties
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all TopAppBar properties