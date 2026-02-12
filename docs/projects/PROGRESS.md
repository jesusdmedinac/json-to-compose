# PROGRESS — json-to-compose Project Roadmap

> Last updated: 2026-02-10

## Overall Progress

| # | Phase | Module | Scenarios | Completed | Progress |
|---|-------|--------|-----------|-----------|----------|
| 1 | [phase-1-solidify-library](#1-phase-1-solidify-library) | `library/` | 187 | 187 | **100%** |
| 2 | [phase-actions-system](#2-phase-actions-system) | `library/` | 37 | 37 | **100%** |
| 3 | [phase-demo-showcase](#3-phase-demo-showcase) | `composeApp/` | 36 | 0 | **0%** |
| 4 | [phase-3-expand-library](#4-phase-3-expand-library) | `library/` | 206 | 0 | **0%** |
| 5 | [phase-2-editor-mvp](#5-phase-2-editor-mvp) | `composy/` | 54 | 0 | **0%** |
| 6 | [phase-3-differentiators](#6-phase-3-differentiators) | Multi-module | 37 | 0 | **0%** |
| | **TOTAL** | | **557** | **224** | **40%** |

```
Progress: [████████████████░░░░░░░░░░░░░░░░░░░░░░░░] 40%
           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
           phase-1 ✅  actions ✅  demo  expand  editor  diff
```

---

## Dependency Graph

```
phase-1-solidify-library ✅
  │
  ├── phase-actions-system ✅
  │     │
  │     ├── phase-demo-showcase ← NEXT
  │     │
  │     └── phase-3-expand-library
  │           │
  │           └── phase-2-editor-mvp
  │                 │
  └─────────────────┴── phase-3-differentiators
```

---

## Recommended Development Order

### 1. phase-1-solidify-library
**Status:** ✅ Complete (187/187 — 100%)
**Module:** `library/`
**Summary:** Foundation — tests, 6 new components (Card, Dialog, TopAppBar, BottomBar, Switch, Checkbox), 6 new modifiers (Border, Shadow, Clip, Shape, Alpha, Rotate), JSON validation, KDoc, demo app showcase.

### 2. phase-actions-system
**Status:** ✅ Complete (37/37 — 100%)
**Module:** `library/`
**Summary:** Declarative state and behaviors from JSON. ComposeAction (SetState, ToggleState, Log, Sequence, Custom), ComposeDocument, ActionDispatcher, auto-wiring, custom action handlers, backward compatibility.

---

### 3. phase-demo-showcase
**Status:** 🔜 Next (0/36 — 0%)
**Module:** `composeApp/`
**Depends on:** phase-1 ✅, phase-actions-system ✅

**Why next:**
- Uses **only** the existing 18 components + 14 modifiers + actions system — no new library work needed.
- Quick win with high visibility: produces a shareable, multi-screen demo app.
- Demonstrates the library's real capabilities to potential adopters.
- Independent of `composy/` — does not block or get blocked by the editor.
- Validates that the actions system works in a real multi-screen navigation scenario.

**Features:**
| Feature | Scenarios |
|---------|-----------|
| Navigation Infrastructure | 6 |
| App Shell with Scaffold and Design Language | 4 |
| Catalog Screen — Home | 5 |
| Components Screen — Interactive Demos | 8 |
| Styles Screen — Modifier Gallery | 7 |
| Multi-Platform Compilation | 6 |

### 4. phase-3-expand-library
**Status:** 📋 Planned (0/206 — 0%)
**Module:** `library/`
**Depends on:** phase-1 ✅, phase-actions-system ✅

**Why after demo:**
- The demo showcase proves the current 18 components work in a real app. Then this phase massively expands the library.
- This is the **biggest phase** (206 scenarios) and the foundation for making the library production-ready.
- Adds 30+ new components, 16+ new modifiers, and 10+ new action types.
- Every phase after this benefits from the expanded component set.
- The editor (phase-2) should support ALL components, so expanding first avoids revisiting the property panel later.

**Features:**
| Feature | Scenarios | What it adds |
|---------|-----------|--------------|
| Text Enhancement | 16 | fontSize, fontWeight, color, textAlign, maxLines, overflow, etc. |
| Button Variants | 12 | OutlinedButton, TextButton, ElevatedButton, FilledTonalButton, IconButton, FAB |
| Card Variants | 6 | ElevatedCard, OutlinedCard |
| TextField Enhancement | 16 | placeholder, label, icons, isError, keyboardType, password, OutlinedTextField |
| Navigation Components | 15 | NavigationBar, NavigationRail, ModalNavigationDrawer, TabRow, Tab |
| Input Components | 18 | Slider, RadioButton, SegmentedButton, DatePicker, TimePicker, SearchBar |
| Layout Components | 14 | Spacer, Divider, FlowRow, FlowColumn, Surface, Arrangement.spacedBy |
| Pager Components | 8 | HorizontalPager, VerticalPager |
| ModalBottomSheet | 8 | ModalBottomSheet with state, drag, shape, scrim |
| Display Components | 22 | Icon, Badge, Chips (4 types), Progress indicators, Tooltip |
| Snackbar | 8 | SnackbarHost, ShowSnackbar action |
| ListItem | 10 | Material 3 ListItem with all slots |
| Missing Modifiers | 22 | Clickable, Weight, Scroll, Offset, Size, AspectRatio, ZIndex, etc. |
| Existing Component Properties | 17 | Enhanced Image, Button, Scaffold, Card, AlertDialog, TopAppBar variants |
| Advanced Actions | 14 | Navigate, Conditional, Delay, IncrementState, LaunchUrl, CopyToClipboard, UpdateList |

### 5. phase-2-editor-mvp
**Status:** 📋 Planned (0/54 — 0%)
**Module:** `composy/`
**Depends on:** phase-1 ✅. Benefits from phase-3-expand-library.

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

### 6. phase-3-differentiators
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
