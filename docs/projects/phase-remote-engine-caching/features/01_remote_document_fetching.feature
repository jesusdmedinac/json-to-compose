Feature: Remote SDUI Document Fetching via HTTP Client
  As an application developer using json-to-compose
  I want to fetch ComposeDocument and ComposeNode JSON payloads from remote URLs
  So that my application's user interface is driven dynamically by a backend or CDN

  Background:
    Given an HTTP client engine configured in commonMain
    And a JSON deserializer supporting ComposeDocument and ComposeNode

  Scenario: Fetch ComposeDocument from remote URL successfully
    Given a remote endpoint "https://api.example.com/screens/home" returning HTTP 200 with valid ComposeDocument JSON
    When the client fetches the document from "https://api.example.com/screens/home"
    Then the response is parsed into a ComposeDocument instance
    And the document contains its initial state, actions, and root ComposeNode
    And the HTTP request method is "GET"

  Scenario: Fetch ComposeNode from remote URL successfully
    Given a remote endpoint "https://api.example.com/components/banner" returning HTTP 200 with valid ComposeNode JSON
    When the client fetches the node from "https://api.example.com/components/banner"
    Then the response is parsed into a ComposeNode tree
    And the root node can be rendered via ToCompose()

  Scenario: Configure custom HTTP headers and bearer auth token
    Given a remote endpoint "https://api.example.com/screens/profile" requiring authentication
    And a fetcher configured with an "Authorization" header containing "Bearer sdui_token_123"
    And a custom header "X-Client-Version" set to "2.4.0"
    When the client executes the fetch request
    Then the outgoing HTTP request contains both the "Authorization" and "X-Client-Version" headers
    And the server accepts the authenticated request

  Scenario: Handle HTTP client and server errors gracefully
    Given a remote endpoint "https://api.example.com/screens/missing" returning HTTP 404
    When the client fetches the document from "https://api.example.com/screens/missing"
    Then a SduiFetchException.HttpError is thrown
    And the exception contains status code 404 and the response message

  Scenario: Handle network timeout and connectivity failures
    Given a remote endpoint "https://api.example.com/screens/slow" that times out after 2000 milliseconds
    When the client fetches the document with a 1000 millisecond timeout
    Then a SduiFetchException.NetworkError is thrown
    And the exception encapsulates the underlying network or timeout cause without crashing the application

  Scenario: Deserialize with custom Json configuration ignoring unknown keys
    Given a remote endpoint returning ComposeDocument JSON with extra unrecognized properties
    When the client deserializes the response with ignoreUnknownKeys enabled
    Then the deserialization succeeds without throwing a SerializationException
    And only known properties are mapped into the ComposeDocument instance
