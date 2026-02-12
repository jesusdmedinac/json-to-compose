Feature: ListItem Component rendered from JSON
  As a developer using json-to-compose
  I want to use Material 3 ListItem from JSON
  So that I can build standardized list layouts from the backend

  Scenario: Render a ListItem with headline text
    Given a JSON with a ListItem with ListItemProps(headlineText = "Settings")
    When the node is processed by the renderer
    Then a Material 3 ListItem with "Settings" as headline content is shown

  Scenario: Render a ListItem with headline and supporting text
    Given a JSON with a ListItem with ListItemProps(headlineText = "Wi-Fi", supportingText = "Connected to HomeNetwork")
    When the node is processed by the renderer
    Then a ListItem with "Wi-Fi" headline and "Connected to HomeNetwork" below is shown

  Scenario: Render a ListItem with overline text
    Given a JSON with a ListItem with ListItemProps(overlineText = "CATEGORY", headlineText = "Item Name")
    When the node is processed by the renderer
    Then a ListItem with "CATEGORY" above the headline is shown

  Scenario: Render a ListItem with leading icon
    Given a JSON with a ListItem with ListItemProps(headlineText = "Inbox", leadingIcon = Icon("inbox"))
    When the node is processed by the renderer
    Then a ListItem with an inbox icon on the left and "Inbox" headline is shown

  Scenario: Render a ListItem with leading image
    Given a JSON with a ListItem with ListItemProps(headlineText = "John Doe", leadingContent = Image(url = "avatar.jpg"))
    When the node is processed by the renderer
    Then a ListItem with an avatar image on the left and "John Doe" headline is shown

  Scenario: Render a ListItem with trailing content
    Given a JSON with a ListItem with ListItemProps(headlineText = "Notifications", trailingContent = Switch(checked = true))
    When the node is processed by the renderer
    Then a ListItem with "Notifications" headline and a Switch on the right is shown

  Scenario: Render a ListItem with trailing text
    Given a JSON with a ListItem with ListItemProps(headlineText = "Storage", trailingText = "64 GB")
    When the node is processed by the renderer
    Then a ListItem with "Storage" headline and "64 GB" on the right is shown

  Scenario: ListItem onClick triggers action
    Given a ComposeDocument with a ListItem with onClickEventName = "openSettings"
    And an action "openSettings" that sets state "currentScreen" to "settings"
    When the user clicks the ListItem
    Then the state "currentScreen" becomes "settings"

  Scenario: Render a LazyColumn of ListItems
    Given a JSON with a LazyColumn containing 5 ListItem children with different headlines
    When the node is processed by the renderer
    Then a scrollable list with 5 Material 3 ListItems is shown

  Scenario: Serialize and deserialize ListItem with all slots
    Given a JSON string with a ListItem containing headlineText, supportingText, overlineText, leadingContent, and trailingContent
    When deserialized to ComposeNode and serialized back
    Then the resulting JSON maintains all ListItem properties
