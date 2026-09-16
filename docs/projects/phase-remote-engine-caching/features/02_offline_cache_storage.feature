Feature: Offline Multiplatform Cache Storage for SDUI Documents
  As an application developer using json-to-compose
  I want an offline dual-tier caching storage (L1 memory + L2 persistent)
  So that documents are cached locally and user screens open instantly without network lag or redundant downloads

  Background:
    Given a SduiCacheStorage configured with an in-memory L1 cache and persistent L2 storage

  Scenario: Store and retrieve remote document in memory cache (L1)
    Given a ComposeDocument fetched from "https://api.example.com/screens/dashboard"
    When the document is saved in SduiCacheStorage with key "dashboard_key"
    Then an immediate read for "dashboard_key" returns the cached ComposeDocument from memory
    And no disk I/O or network request is executed

  Scenario: Persist cached document across app sessions (L2)
    Given a ComposeDocument saved in SduiCacheStorage under key "settings_key"
    When the L1 in-memory cache is evicted or cleared
    And a read for "settings_key" is performed
    Then the document is successfully recovered from the L2 persistent storage
    And the document is promoted back into the L1 in-memory cache

  Scenario: Cache key generation from URL and optional query parameters
    Given two URLs "https://api.example.com/feed?tab=news" and "https://api.example.com/feed?tab=sports"
    When SduiCacheKeyGenerator generates cache keys for both URLs
    Then two distinct deterministic cache keys are generated
    And storing the "news" document does not overwrite the "sports" document

  Scenario: Honor TTL (Time-To-Live) expiration
    Given a cached document stored with a TTL of 300 seconds
    When 301 seconds elapse since storage
    And a cache read is requested for that key
    Then the cache entry is identified as expired
    And SduiCacheStorage returns null or marks the entry as needing revalidation

  Scenario: Evict oldest entries when L1 memory cache reaches max capacity (LRU)
    Given an L1 memory cache configured with a maximum capacity of 5 entries
    When 6 distinct documents are sequentially stored in the cache
    Then the least recently accessed document is evicted from L1 memory
    And the most recently stored 5 documents remain in L1 memory

  Scenario: Clear cache programmatically
    Given multiple documents stored across L1 memory and L2 persistent storage
    When clearCache() is invoked on SduiCacheStorage
    Then all L1 in-memory entries are purged
    And all L2 persistent storage files or entries are deleted
    And subsequent reads for previously cached keys return null
