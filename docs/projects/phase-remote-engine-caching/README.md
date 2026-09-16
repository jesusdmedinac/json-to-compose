# Phase: Remote Engine and Caching System (Server-Driven UI Runtime)

**Status:** Planned  
**Module:** `library/`  
**Date:** 2026-09-16  
**Tracking Branch:** `feat/remote-engine-cache`  

---

## 1. Idea (Step 1 - Natural Language Definition)

Currently, `json-to-compose` is an exceptional parser and renderer: given a JSON string, it decodes it into a `ComposeNode` or `ComposeDocument` and renders interactive native Jetpack Compose UI across Android, Desktop (JVM), iOS, and Web (Wasm).

However, in real-world Server-Driven UI (SDUI) applications, **JSON documents live on remote servers or CDNs**, not in static asset bundles. Today, developers have to manually write custom Ktor HTTP clients, manage background threads, implement ad-hoc disk/memory caching, handle HTTP 304/ETag headers, and wire loading/error states into Compose.

Without an integrated remote engine:
- Every screen transition or app restart re-fetches JSON from the server, causing latency, UI flicker, and unnecessary server load.
- When a user loses connectivity, the screen fails completely instead of serving the cached offline layout.
- There is no unified `RemoteComposeDocument(url)` declarative Composable to load server-driven screens out of the box.

### The Objective

Provide an **offline-first, multiplatform Remote SDUI Engine and Caching System** directly in the `library/` module:
1. **Remote Fetcher (`SduiClient` / `RemoteDocumentFetcher`)**: Fetches `ComposeDocument` and `ComposeNode` payloads over HTTP/HTTPS with custom headers, authentication tokens, timeouts, and structured error handling.
2. **Multiplatform Cache Storage (`SduiCacheStorage`)**: High-performance dual-tier caching (L1 In-Memory LRU Cache + L2 Persistent Cache) with deterministic cache key hashing and configurable TTL (Time-To-Live).
3. **Smart Invalidation & Revalidation**: Full HTTP caching semantics (`ETag`, `If-None-Match`, `Cache-Control`, `304 Not Modified`) and **Stale-While-Revalidate (SWR)**: immediately render the cached screen for instant 0ms startup, revalidate against the CDN in the background, and seamlessly update the UI if the layout changed.
4. **Declarative Composable API (`RemoteComposeDocument` / `RemoteComposeNode`)**: High-level Compose components that observe a `RemoteUiState` Flow with customizable loading indicators, offline banners, error views with retry triggers, and smooth recomposition without visual flicker.

---

## 2. Scope

### In Scope
- **Module:** `library/` (`commonMain`, with multiplatform storage abstractions for `androidMain`, `desktopMain`, `iosMain`, `wasmJsMain`).
- **Networking:** Ktor Client engine integration in `commonMain` utilizing platform-specific engines already present (`android`, `java`, `darwin`).
- **Caching:** L1 memory cache (in-memory map with LRU eviction) + L2 persistent cache abstraction (key-value / file-based storage).
- **Semantics:** ETag / `If-None-Match`, HTTP 304, `Cache-Control: max-age`, TTL expiration, offline fallback, force-refresh.
- **UI Runtime:** Declarative `RemoteComposeDocument` & `RemoteComposeNode` Composables, `RemoteUiState` state holder, slot-based UI (loading, error, retry).
- **Synergy:** Integration points for `KMP-CLI` (`sdui-starter` template and `kmp-sdui-compose` skill) and `Composy` visual editor.

### Out of Scope
- Backend server implementation (covered in existing `server/` module).
- Visual editor WYSIWYG sync (belongs to `composy/` in subsequent phases).
- Dynamic binary bytecode downloading (SDUI is purely declarative JSON layout and action trees).

---

## 3. Success Criteria

1. **Zero-Code Remote Rendering**: A developer can display a remote screen with a single line:
   ```kotlin
   RemoteComposeDocument(url = "https://api.example.com/screens/home.json")
   ```
2. **Instant App Launch (Stale-While-Revalidate)**: Cached screens render immediately (<16ms) without showing a blank screen or loading spinner when valid cache exists.
3. **Bandwidth & Latency Optimization**: If the backend layout hasn't changed, the server responds with `304 Not Modified`, saving network data and avoiding UI re-rendering.
4. **Resilient Offline Fallback**: If the network is offline or times out, the engine serves the last known cached document and marks the state as offline/stale.
5. **Configurable & Extensible**: Developers can inject their own Ktor `HttpClient`, custom headers/auth tokens, custom cache storage, and custom loading/error Compose slots.
6. **100% Test Coverage on Desktop**: All 24 Gherkin scenarios verified via automated tests in `library/desktopTest`.

---

## 4. Architecture and Design Decisions

```
                           +-------------------------------+
                           |     RemoteComposeDocument     |
                           |         (Composable)          |
                           +---------------+---------------+
                                           |
                                   observes Flow<RemoteUiState>
                                           |
                           +---------------v---------------+
                           |          SduiEngine           |
                           |  (Orchestrator & Flow Cache)  |
                           +-------+---------------+-------+
                                   |               |
                         read/write cache      fetch network
                                   |               |
             +---------------------v-----+   +-----v---------------------+
             |     SduiCacheStorage      |   |        SduiClient         |
             |   (L1 Memory + L2 Disk)   |   |   (Ktor HTTP Client)      |
             +---------------------------+   +---------------------------+
                         |                                 |
              [ETag / TTL / Payload]              [GET url, 304 / 200]
```

### 4.1. RemoteUiState Sealed Hierarchy
```kotlin
sealed interface RemoteUiState {
    data object Loading : RemoteUiState
    data class Success(
        val document: ComposeDocument,
        val isStale: Boolean = false,
        val isOffline: Boolean = false,
    ) : RemoteUiState
    data class Error(
        val cause: Throwable,
        val cachedFallback: ComposeDocument? = null,
        val retry: () -> Unit,
    ) : RemoteUiState
}
```

### 4.2. Dual-Tier SduiCacheStorage
- **L1 In-Memory LRU**: Fast memory cache holding deserialized `ComposeDocument` instances for immediate display.
- **L2 Persistent Storage**: Serialized JSON + metadata (`ETag`, `timestamp`, `ttlSeconds`) persisted locally per platform.
- **CacheKey**: SHA-256 hash of `url` + normalized sorted query parameters and designated variance headers.

### 4.3. Revalidation Flow (Stale-While-Revalidate)
1. Query L1/L2 cache for key.
2. If cache hit: emit `RemoteUiState.Success(cachedDocument, isStale = true)`.
3. Concurrently trigger background network request with `If-None-Match: <etag>` and `If-Modified-Since: <timestamp>`.
4. If network returns `304 Not Modified`:
   - Update cache entry timestamp.
   - Emit `RemoteUiState.Success(cachedDocument, isStale = false)`.
5. If network returns `200 OK`:
   - Parse new `ComposeDocument`.
   - Update L1 + L2 cache with payload, new `ETag`, and new timestamp.
   - Emit `RemoteUiState.Success(freshDocument, isStale = false)`.
6. If network fails and cache hit was already emitted:
   - Emit `RemoteUiState.Success(cachedDocument, isStale = true, isOffline = true)`.
7. If network fails and no cache exists:
   - Emit `RemoteUiState.Error(cause, cachedFallback = null, retry = ...)`.

---

## 5. Technical References

- **Ktor Client 3.3.0**: Existing dependencies in `gradle/libs.versions.toml` (`ktor-client-core`, `ktor-client-android`, `ktor-client-darwin`, `ktor-client-java`).
- **Serialization**: `kotlinx-serialization-json` 1.9.0.
- **Compose Runtime**: Compose Multiplatform 1.9.3, `library/` module.
- **Testing Standard**: Compose UI testing rules (`./gradlew :library:desktopTest`).

---

## 6. Synergy with KMP-CLI & Composy

1. **`kmp-sdui-compose` Skill**: Once this phase is planned and implemented, the `kmp-sdui-compose` skill in KMP-CLI can teach agents how to fetch and cache remote SDUI screens using `RemoteComposeDocument(url)`.
2. **`kmp create --template sdui-starter`**: Developers can scaffold a full KMP app with instant remote SDUI capability using json-to-compose.
3. **Composy Preview Integration**: The visual editor can test live URLs with simulation of network delays, offline states, and cache revalidation.
