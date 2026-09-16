# Phase: Remote Engine and Caching System — Progress

**Phase:** `phase-remote-engine-caching`  
**Module:** `library/`  
**Total Scenarios:** 24  
**Completed:** 0  
**Progress:** 0%  

---

## Feature 1: Remote SDUI Document Fetching via HTTP Client
- [ ] Fetch ComposeDocument from remote URL successfully
- [ ] Fetch ComposeNode from remote URL successfully
- [ ] Configure custom HTTP headers and bearer auth token
- [ ] Handle HTTP client and server errors gracefully
- [ ] Handle network timeout and connectivity failures
- [ ] Deserialize with custom Json configuration ignoring unknown keys

## Feature 2: Offline Multiplatform Cache Storage for SDUI Documents
- [ ] Store and retrieve remote document in memory cache (L1)
- [ ] Persist cached document across app sessions (L2)
- [ ] Cache key generation from URL and optional query parameters
- [ ] Honor TTL (Time-To-Live) expiration
- [ ] Evict oldest entries when L1 memory cache reaches max capacity (LRU)
- [ ] Clear cache programmatically

## Feature 3: Cache Invalidation, HTTP 304 Revalidation, and Stale-While-Revalidate
- [ ] Send If-None-Match header when cached ETag exists
- [ ] Handle 304 Not Modified by serving cached document
- [ ] Update cache on 200 OK with new content and updated ETag
- [ ] Stale-While-Revalidate emits cached content instantly then updates on network response
- [ ] Offline fallback when network is unavailable but stale cache exists
- [ ] Force-refresh bypasses cache and updates storage

## Feature 4: Declarative Remote Compose UI Component
- [ ] Render RemoteComposeDocument with default loading and success states
- [ ] Provide custom loading Composable slot
- [ ] Provide custom error Composable slot with retry callback
- [ ] Re-render automatically when URL parameter changes
- [ ] Render RemoteComposeNode for standalone UI trees
- [ ] Seamless transition on background revalidation without UI flicker
