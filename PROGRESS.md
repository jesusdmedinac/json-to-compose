# PROGRESS — json-to-compose Project Roadmap

> [!IMPORTANT]
> **Mandatory Workflow Requirement:** To avoid a `NullPointerException` (NPE) in Android Unit Tests, agents **must NOT** run UI tests using `./gradlew :library:test`. Instead, always use `./gradlew :library:desktopTest` for primary validation.
> See the [Compose Multiplatform UI Testing Guide](docs/COMPOSE_MULTIPLATFORM_TESTING.md#mandatory-testing-rules-for-agents) for more details.

> Last updated: 2025-03-05

## Overall Progress

| #   | Phase                                                             | Module        | Scenarios | Completed | Progress |
| --- | ----------------------------------------------------------------- | ------------- | --------- | --------- | -------- |
| 1   | [phase-1-solidify-library](#1-phase-1-solidify-library)           | `library/`    | 187       | 187       | **100%** |
| 2   | [phase-actions-system](#2-phase-actions-system)                   | `library/`    | 37        | 37        | **100%** |
| 3   | [phase-demo-showcase](#3-phase-demo-showcase)                     | `composeApp/` | 36        | 36        | **100%** |
| 4   | [phase-3-expand-library](#4-phase-3-expand-library)               | `library/`    | 239       | 90        | **38%**  |
| 5   | [phase-2-editor-mvp](#5-phase-2-editor-mvp)                       | `composy/`    | 54        | 0         | **0%**   |
| 6   | [phase-4-semantics-testability](#6-phase-4-semantics-testability) | `library/`    | 26        | 0         | **0%**   |
| 7   | [phase-3-differentiators](#7-phase-3-differentiators)             | Multi-module  | 37        | 0         | **0%**   |
|     | **TOTAL**                                                         |               | **616**   | **350**   | **57%**  |

```
Progress: [████████████████████████░░░░░░░░░░░░░░░░] 57%
           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
           phase-1 ✅  actions ✅  demo ✅  expand  editor  sem  diff
```

---

## Dependency Graph

```
phase-3-expand-library ← IN PROGRESS
  │
  ├── phase-2-editor-mvp
  │
  └── phase-4-semantics-testability
        │
  ──────┴── phase-3-differentiators
```

---

## Recommended Development Order

### 1. phase-3-expand-library

**Status:** 🏗️ In Progress (90/239 — 38%)
**Module:** `library/`
**Depends on:** phase-1 ✅, phase-actions-system ✅

**Why after demo:**

- The demo showcase proves the current 18 components work in a real app. Then this phase massively expands the library.
- This is the **biggest phase** (236 scenarios) and the foundation for making the library production-ready.
- Adds 30+ new components, 16+ new modifiers, and 10+ new action types.
- Every phase after this benefits from the expanded component set.
- The editor (phase-2) should support ALL components, so expanding first avoids revisiting the property panel later.

**Features:**
| Feature | Scenarios | What it adds | Status |
|---------|-----------|--------------|--------|
| Skeleton Bootstrap | 3 | Base enum, properties and router switches for parallel work | ✅ Done |
| Text Enhancement | 18 | fontSize, fontWeight, color, textAlign, maxLines, overflow, etc. | ✅ Done |
| Button Variants | 14 | OutlinedButton, TextButton, ElevatedButton, FilledTonalButton, IconButton, FAB | ✅ Done |
| Card Variants | 8 | ElevatedCard, OutlinedCard | ✅ Done |
| TextField Enhancement | 18 | placeholder, label, icons, isError, keyboardType, password, OutlinedTextField | ✅ Done |
| Navigation Components | 17 | NavigationBar, NavigationRail, ModalNavigationDrawer, TabRow, Tab | ✅ Done |
| Input Components | 20 | Slider, RadioButton, SegmentedButton, DatePicker, TimePicker, SearchBar | 🔜 Pending |
| Layout Components | 16 | Spacer, Divider, FlowRow, FlowColumn, Surface, Arrangement.spacedBy | 🔜 Pending |
| Pager Components | 10 | HorizontalPager, VerticalPager | 🔜 Pending |
| ModalBottomSheet | 10 | ModalBottomSheet with state, drag, shape, scrim | 🔜 Pending |
| Display Components | 24 | Icon, Badge, Chips (4 types), Progress indicators, Tooltip | 🏗️ In Progress |
| Snackbar | 10 | SnackbarHost, ShowSnackbar action | 🔜 Pending |
| ListItem | 12 | Material 3 ListItem with all slots | 🔜 Pending |
| Missing Modifiers | 24 | Clickable, Weight, Scroll, Offset, Size, AspectRatio, ZIndex, etc. | 🔜 Pending |
| Existing Component Properties | 19 | Enhanced Image, Button, Scaffold, Card, AlertDialog, TopAppBar variants | 🔜 Pending |
| Advanced Actions | 16 | Navigate, Conditional, Delay, IncrementState, LaunchUrl, CopyToClipboard, UpdateList | 🔜 Pending |

### 2. phase-2-editor-mvp

**Status:** 📋 Planned (0/54 — 0%)
**Module:** `composy/`
**Depends on:** Benefits from phase-3-expand-library.

**Why after expand:**

- The editor's **Full Property Panel** feature needs to know about all component types and their properties.
- If done before expand, the property panel would need to be revisited for every new component type.
- Better to expand the library first, then build the editor's property panel once with full coverage.
- The **Critical Bug Fixes** (delete modifier, collapse state) are quick wins that could be cherry-picked earlier if needed.

**Features:**
| Feature | Scenarios |
|---------|-----------|
| Tree Node Deletion | 7 |
| Modifier Deletion in Node Editor | 4 |
| Tree Node Reordering | 8 |
| Full Property Panel per Component | 11 |
| Improved Real-time Preview | 8 |
| JSON Export and Import | 8 |
| Critical Editor Bug Fixes | 8 |

### 3. phase-4-semantics-testability

**Status:** 📋 Planned (0/26 — 0%)
**Module:** `library/`
**Depends on:** phase-3-expand-library.

**Why here:**

- Ensures rigorous testing for all the new components and properties added in Phase 3.
- Upgrades the testing infrastructure to validate styles and layout correctness, not just existence.
- Can be implemented in parallel with Phase 2 or Phase 3 components as they are added.

**Features:**
| Feature | Scenarios |
|---------|-----------|
| Text Semantics | 8 |
| Modifier Semantics | 7 |
| Layout Semantics | 5 |
| Component Semantics | 6 |

### 4. phase-3-differentiators

**Status:** 📋 Planned (0/37 — 0%)
**Module:** `composy/`, `server/`, `intellij-plugin/` (new), deployment
**Depends on:** All previous phases.

**Why last:**

- AI generation needs to know ALL available components to generate valid JSON.
- The demo server should serve the full component set.
- The IDE plugin benefits from a complete, stable library API.
- The web playground IS composy — it should be feature-complete first (phase-2 fixes).
- These are the advanced differentiators that make the project stand out, built on a solid foundation.

**Features:**
| Feature | Scenarios |
|---------|-----------|
| AI Layout Generation from Prompts | 8 |
| Functional Demo Server for SDUI | 9 |
| IntelliJ/Android Studio Plugin for Preview | 10 |
| Public Web Playground | 10 |

---

## Notes

- **Naming clarification:** The phases are numbered by when they were planned, not by execution order. `phase-3-expand-library` was planned third but executes fourth. The recommended order above is the optimal execution sequence.
- **Parallel execution:** `phase-demo-showcase` (composeApp/) and `phase-2-editor-mvp` (composy/) touch different modules and could theoretically run in parallel. However, the editor benefits from the expanded library, so the recommended order is sequential.
- **Cherry-picking:** Critical bug fixes from `phase-2-editor-mvp` (e.g., delete modifier onClick, collapsedNodes by ID) are isolated fixes that could be done at any time without disrupting the order.
- **ComposeDocument consideration:** When `phase-2-editor-mvp` implements JSON Export/Import, it should support both `ComposeNode` (plain) and `ComposeDocument` (with state/actions) formats, since phase-actions-system is already complete.
