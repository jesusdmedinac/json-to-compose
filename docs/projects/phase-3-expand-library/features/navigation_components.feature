Feature: Navigation Components rendered from JSON
  As a developer using json-to-compose
  I want to define NavigationBar, NavigationRail, TabRow, and NavigationDrawer in JSON
  So that I can control app navigation structure entirely from the backend

  Scenario: Render a NavigationBar with three items
    Given a JSON with a NavigationBar containing 3 NavigationBarItem children
    When the node is processed by the renderer
    Then a Material 3 NavigationBar with 3 navigation items is shown

  Scenario: Render a NavigationBarItem with icon and label
    Given a JSON with a NavigationBarItem with NavigationBarItemProps(label = "Home", iconName = "home", selected = true)
    When the node is processed by the renderer
    Then a selected NavigationBarItem with "Home" label and home icon is shown

  Scenario: NavigationBarItem selection controlled by state
    Given a JSON with NavigationBarItem with NavigationBarItemProps(selectedStateHostName = "tab0Selected")
    And a StateHost "tab0Selected" with value true
    When the node is processed by the renderer
    Then the NavigationBarItem appears selected

  Scenario: NavigationBarItem onClick triggers action
    Given a ComposeDocument with a NavigationBarItem with onClickEventName = "selectTab0"
    And an action "selectTab0" that sets state "selectedTab" to 0
    When the user clicks the NavigationBarItem
    Then the state "selectedTab" becomes 0

  Scenario: Render a NavigationRail with items
    Given a JSON with a NavigationRail containing 4 NavigationRailItem children
    When the node is processed by the renderer
    Then a vertical Material 3 NavigationRail with 4 items on the left is shown

  Scenario: Render a NavigationRailItem with icon and label
    Given a JSON with a NavigationRailItem with NavigationRailItemProps(label = "Settings", iconName = "settings", selected = false)
    When the node is processed by the renderer
    Then an unselected NavigationRailItem with "Settings" label and settings icon is shown

  Scenario: Render a NavigationRail with header FAB
    Given a JSON with a NavigationRail with NavigationRailProps(header = FloatingActionButton(Icon("edit")))
    When the node is processed by the renderer
    Then a NavigationRail with a FAB at the top is shown

  Scenario: Render a ModalNavigationDrawer with content
    Given a JSON with a ModalNavigationDrawer containing drawer sheet items and main content
    When the node is processed by the renderer
    Then a NavigationDrawer that slides from the left with drawer items is shown

  Scenario: ModalNavigationDrawer visibility controlled by state
    Given a JSON with a ModalNavigationDrawer with NavigationDrawerProps(isOpenStateHostName = "drawerOpen")
    And a StateHost "drawerOpen" with value false
    When the node is processed by the renderer
    Then the NavigationDrawer is closed

  Scenario: Render a TabRow with tabs
    Given a JSON with a TabRow containing 3 Tab children
    When the node is processed by the renderer
    Then a Material 3 TabRow with 3 tabs and indicator is shown

  Scenario: Render a Tab with text and icon
    Given a JSON with a Tab with TabProps(text = "Photos", iconName = "photo", selected = true)
    When the node is processed by the renderer
    Then a selected Tab with "Photos" text and photo icon is shown

  Scenario: Tab selection controlled by state
    Given a JSON with Tab children with TabProps(selectedStateHostName = "activeTab")
    And a StateHost "activeTab" with value 1
    When the node is processed by the renderer
    Then the second Tab appears selected

  Scenario: Render a ScrollableTabRow with many tabs
    Given a JSON with a ScrollableTabRow containing 8 Tab children
    When the node is processed by the renderer
    Then a horizontally scrollable TabRow with 8 tabs is shown

  Scenario: Render NavigationBar inside Scaffold bottomBar
    Given a JSON with a Scaffold with ScaffoldProps(bottomBar = NavigationBar with items)
    When the node is processed by the renderer
    Then a Scaffold with NavigationBar at the bottom is shown

  Scenario: Serialize and deserialize all navigation components
    Given JSON strings for NavigationBar, NavigationBarItem, NavigationRail, NavigationRailItem, ModalNavigationDrawer, TabRow, ScrollableTabRow, and Tab
    When each is deserialized to ComposeNode and serialized back
    Then each resulting JSON maintains all navigation component properties
