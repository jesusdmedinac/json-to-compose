Feature: Cache Invalidation, HTTP 304 Revalidation, and Stale-While-Revalidate
  As an application developer using json-to-compose
  I want intelligent cache revalidation with ETag support and Stale-While-Revalidate flows
  So that users see cached screens immediately while keeping UI up-to-date with backend changes

  Background:
    Given an SduiEngine orchestrating SduiClient and SduiCacheStorage

  Scenario: Send If-None-Match header when cached ETag exists
    Given a cached ComposeDocument for "https://api.example.com/screens/home" with stored ETag "W/\"v1-987abc\""
    When the engine performs a revalidation request for "https://api.example.com/screens/home"
    Then the outgoing HTTP GET request contains header "If-None-Match" with value "W/\"v1-987abc\""

  Scenario: Handle 304 Not Modified by serving cached document
    Given a cached ComposeDocument for "https://api.example.com/screens/home" with stored ETag "W/\"v1-987abc\""
    When the remote server responds with HTTP 304 Not Modified
    Then no JSON deserialization of the body is performed
    And the cached document is retained
    And the cache entry's lastVerified timestamp is refreshed

  Scenario: Update cache on 200 OK with new content and updated ETag
    Given a cached ComposeDocument with ETag "W/\"v1-old\""
    When the server responds with HTTP 200 OK, a new ComposeDocument payload, and ETag "W/\"v2-new\""
    Then the new ComposeDocument is parsed and stored in cache
    And the new ETag "W/\"v2-new\"" replaces the previous ETag in cache metadata
    And the updated document is emitted to callers

  Scenario: Stale-While-Revalidate emits cached content instantly then updates on network response
    Given a cached ComposeDocument stored for "https://api.example.com/screens/feed"
    And a remote server that takes 500 milliseconds to respond with an updated document
    When the consumer collects loadDocumentFlow("https://api.example.com/screens/feed")
    Then the first emission is RemoteUiState.Success with the cached document and isStale set to true within 16 milliseconds
    And after 500 milliseconds, a second emission is RemoteUiState.Success with the updated document and isStale set to false

  Scenario: Offline fallback when network is unavailable but stale cache exists
    Given a cached ComposeDocument stored for "https://api.example.com/screens/profile"
    And the device has no network connection
    When the consumer collects loadDocumentFlow("https://api.example.com/screens/profile")
    Then the emission is RemoteUiState.Success with the cached document, isStale set to true, and isOffline set to true
    And the user can view and interact with the cached screen despite being offline

  Scenario: Force-refresh bypasses cache and updates storage
    Given a cached ComposeDocument for "https://api.example.com/screens/orders"
    When loadDocumentFlow("https://api.example.com/screens/orders", forceRefresh = true) is executed
    Then the engine does not emit the cached document first
    And it forces an unconditional HTTP GET request without If-None-Match
    And on successful 200 OK response it updates the cache and emits RemoteUiState.Success with isStale set to false
