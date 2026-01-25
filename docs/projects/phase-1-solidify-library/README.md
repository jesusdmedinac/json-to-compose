# Phase 1: Solidify json-to-compose Library

## Idea (Step 1)

The json-to-compose library is already published on Maven Central (v1.1.0) and works on 4 platforms (Android, iOS, Desktop, Web). However, for other developers to trust it and adopt it in production, it needs three things: solid test coverage, more components and modifiers, and professional documentation.

The goal of this phase is to take the library from "functional" to "reliable and complete":

- **Exhaustive tests** that validate every component, every modifier, and the full JSON to Compose UI pipeline. Including unit tests, integration tests, and snapshot tests.
- **Expand components** with those missing from Material 3: Card, Dialog, TopAppBar, BottomBar, Switch, and Checkbox.
- **Expand modifiers** with operations that any real app needs: border, shadow, clip, shape, alpha, and rotation.
- **JSON Schema validation** so developers receive clear errors when their JSON has problems, instead of silent crashes.
- **KDoc on the entire public API** so that IDE autocompletion shows useful documentation and automatic documentation can be generated.

## Scope

This project focuses exclusively on the `library/` module. It does not touch the editor (`composy/`), the server (`server/`), nor the demo app (`composeApp/`).

## Success Criteria

- Test coverage > 80% in the library module
- The 6 new components render correctly from JSON on all 4 platforms
- The 6 new modifiers apply correctly and are serializable
- Invalid JSON produces descriptive error messages, not cryptic exceptions
- Every public class, function, and property has KDoc