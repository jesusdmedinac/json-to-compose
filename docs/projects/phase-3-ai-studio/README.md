# Phase 3: Composy AI Studio

## Idea (Step 1)
Transform Composy from a traditional manual form-and-tree editor into an AI-first, conversational generative UI studio for Jetpack Compose Multiplatform (Android, iOS, Desktop, and Web Wasm), inspired by the ergonomics of v0, Lovable, Bolt, and Pencil (`pen.dev`).

## Core Capabilities
1. **A2UI Express DSL Engine (`:library`)**: Linear DSL parser (1 line per component, 80%+ reduction in LLM inference tokens) that compiles streaming text into `ComposeNode` AST without syntax errors.
2. **AI Studio Shell UI (`:composy`)**: Modern 2-pane workspace: conversational prompt/chat interface on the left and live native Compose canvas on the right.
3. **Direct Canvas Hit-Testing (The Pencil Factor)**: Click-to-inspect directly on the rendered Skia preview, enabling context-aware iterative prompt refinement.
4. **Kotlin Compose Code Generator (`:library`)**: Instant bidirectional toggle from preview to clean, idiomatic `@Composable` Kotlin code with one-click copy.
