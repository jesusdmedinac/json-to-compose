Feature: Public Web Playground
  As a developer interested in json-to-compose
  I want a web playground accessible from the browser
  To try the library without installing anything and decide if I adopt it

  Scenario: Access playground without account or installation
    Given a developer visits the playground URL
    When the page loads
    Then they see the full composy editor working in the browser
    And can start creating components immediately without login

  Scenario: Playground loads with pre-loaded example
    Given a developer visiting the playground for the first time
    When the page loads
    Then they see an example tree with a demo screen (Column with Text, Image, Button)
    And the preview shows the example interface

  Scenario: Select from multiple pre-loaded examples
    Given the developer is in the playground
    When they open the example selector
    Then they see options like: "Login Form", "Product Card", "Profile Screen", "Settings Page"
    When they select "Login Form"
    Then the editor loads the login form tree
    And the preview shows the form

  Scenario: Copy generated JSON to clipboard
    Given the developer has created a design in the playground
    When they press the "Copy JSON" button
    Then the full JSON of the tree is copied to the clipboard
    And a confirmation toast "JSON copied to clipboard" is shown

  Scenario: Share design via URL
    Given the developer has created a design in the playground
    When they press the "Share" button
    Then a unique URL encoding the design is generated
    And the URL is copied to the clipboard
    When another developer opens that URL
    Then they see the same design in the editor

  Scenario: Playground shows integration instructions
    Given the developer has a ready design
    When they press the "Use in your app" button
    Then they see instructions to add the Maven Central dependency
    And see the necessary Kotlin code to render the JSON
    And the instructions include examples for Android, iOS, Desktop, and Web

  Scenario: Playground works in offline mode after first load
    Given the developer has visited the playground previously
    When they lose internet connection
    Then the playground continues working with cached data
    And they can still edit and export JSON

  Scenario: Playground has dark mode
    Given the developer is in the playground
    When they activate dark mode from the top bar
    Then the entire editor interface changes to dark theme
    And the preview maintains the colors defined in the design

  Scenario: Playground is responsive on mobile devices
    Given a developer accesses the playground from a phone
    When the page loads
    Then the interface adapts showing stacked panels instead of side-by-side
    And the main functions remain accessible

  Scenario: Playground has inline documentation
    Given the developer is in the playground
    When they hover over a component type in the palette
    Then they see a tooltip with the component description and its properties
    And a link to the full documentation