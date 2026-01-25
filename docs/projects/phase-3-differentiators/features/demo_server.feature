Feature: Functional Demo Server for Server-Driven UI
  As a developer evaluating json-to-compose
  I want a demo server that serves JSON on REST endpoints
  To demonstrate the real SDUI use case: changing the UI from the backend without redeployment

  Scenario: Server serves JSON for a full screen
    Given the Ktor server is running
    When a GET request is made to /api/screens/home
    Then the server responds with a valid ComposeNode JSON
    And the JSON represents a home screen with header, content, and footer

  Scenario: Server serves multiple screens
    Given the server is configured with screens: home, profile, settings
    When a GET request is made to /api/screens/profile
    Then the server responds with the profile screen JSON
    And each screen has a different layout

  Scenario: Server allows updating screen without redeploy
    Given the home screen has a Text("Version 1")
    When the administrator updates the home JSON changing it to "Version 2"
    And the app makes a GET request to /api/screens/home
    Then the app receives the JSON with Text("Version 2")
    And the UI updates without reinstalling the app

  Scenario: Demo app consumes server in real-time
    Given the demo app (composeApp) is configured to connect to the server
    When the app runs on Android/iOS/Desktop/Web
    Then it loads the initial screen from the server
    And renders the UI using json-to-compose

  Scenario: Server serves components by section
    Given the server has modular sections (header, banner, product_list)
    When a GET request is made to /api/sections/banner
    Then the server responds with the JSON of the banner component only
    And the app can compose the screen combining multiple sections

  Scenario: Server handles errors gracefully
    Given a request to a screen that does not exist
    When a GET request is made to /api/screens/nonexistent
    Then the server responds with status 404 and a descriptive message

  Scenario: Server has basic administration panel
    Given the server is running
    When the administrator accesses /admin
    Then they see an interface to edit each screen's JSON
    And can save changes that are immediately reflected in the apps

  Scenario: Server supports screen versioning
    Given the home screen has version 1 and version 2
    When the app makes a GET request to /api/screens/home?version=1
    Then it receives version 1 of the layout
    When the app makes a GET request to /api/screens/home?version=2
    Then it receives version 2 of the layout

  Scenario: Server has health check endpoint
    Given the server is running
    When a GET request is made to /api/health
    Then it responds with status 200 and the server version