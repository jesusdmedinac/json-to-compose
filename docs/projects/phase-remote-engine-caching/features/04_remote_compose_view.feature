Feature: Declarative Remote Compose UI Component
  As an application developer using json-to-compose
  I want a declarative RemoteComposeDocument and RemoteComposeNode Composable
  So that I can embed server-driven screens in my Compose hierarchy with loading, error, and offline handling

  Background:
    Given a Compose Multiplatform UI context on Android, Desktop, iOS, or Wasm

  Scenario: Render RemoteComposeDocument with default loading and success states
    Given a remote URL "https://api.example.com/screens/home" returning a ComposeDocument with a Text title
    When RemoteComposeDocument(url = "https://api.example.com/screens/home") is composed
    Then a loading indicator is initially displayed
    And once the document is resolved, the document UI tree is rendered with auto-wired state and actions
    And the title text is visible in the Compose UI tree

  Scenario: Provide custom loading Composable slot
    Given a remote URL that is currently fetching
    When RemoteComposeDocument is composed with loadingContent = { Text("Custom loading skeleton...") }
    Then "Custom loading skeleton..." is displayed during the loading state
    And the default circular progress indicator is omitted

  Scenario: Provide custom error Composable slot with retry callback
    Given a remote URL returning HTTP 500 error
    When RemoteComposeDocument is composed with custom errorContent receiving error and retry lambda
    Then the custom error Composable is displayed
    And invoking the retry lambda triggers a new fetch attempt without reconstructing the parent screen

  Scenario: Re-render automatically when URL parameter changes
    Given RemoteComposeDocument currently displaying screen from "https://api.example.com/screens/page1"
    When the url parameter changes to "https://api.example.com/screens/page2"
    Then the previous request is cancelled
    And the new screen from "https://api.example.com/screens/page2" is fetched and displayed

  Scenario: Render RemoteComposeNode for standalone UI trees
    Given a remote URL "https://api.example.com/components/header" returning a raw ComposeNode
    When RemoteComposeNode(url = "https://api.example.com/components/header") is composed
    Then the node is fetched, cached, and rendered via ComposeNode.ToCompose()

  Scenario: Seamless transition on background revalidation without UI flicker
    Given a screen is currently displaying a cached ComposeDocument
    When a background revalidation receives an updated document version
    Then the UI transitions smoothly to the updated document layout
    And no full-screen loading spinner or layout flicker is displayed during the transition
