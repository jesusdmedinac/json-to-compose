# ADR-002: StateHost Type Information in JSON

**Status:** Accepted
**Date:** 2026-01-31
**Decision makers:** Project maintainers

## Context

The `StateHost<T>` interface is the mechanism by which JSON-driven components read and write state (e.g., a TextField's text value, a Dialog's visibility). Components reference a StateHost by name via properties like `valueStateHostName` or `visibilityStateHostName`.

Currently, the JSON only carries the **name** of the StateHost:

```json
{
  "type": "TextField",
  "properties": {
    "type": "TextFieldProps",
    "valueStateHostName": "search_query"
  }
}
```

The **type** of the StateHost (`String`, `Boolean`, etc.) is implicit — hardcoded in each renderer via unchecked casts:

```kotlin
// ToTextField
val stateHost = currentStateHost[valueStateHostName] as? StateHost<String> ?: return

// ToDialog
val visibilityStateHost = currentStateHost[it] as? StateHost<Boolean>
```

This means:

1. A backend developer authoring JSON cannot know what type to provide without reading the renderer source code.
2. If the host app registers a `StateHost<Int>` where `StateHost<String>` is expected, the `as?` cast fails silently and the component is not rendered — with no error message.
3. Custom components (`ComposeType.Custom`) have no built-in renderer, so the expected StateHost type is completely unknown from the JSON alone.
4. Tooling (JSON validators, visual editors like Composy) cannot verify type correctness.

## Options Evaluated

### Option 1: Keep Types Implicit (Do Nothing)

The type is determined by the component. TextField always expects `String`, Dialog visibility always expects `Boolean`. Developers must consult the documentation.

**Pros:**
- Zero additional complexity in the serialization model.
- Minimal JSON payload — no extra fields.
- For built-in components, the type is always deterministic from the component + property name.

**Cons:**
- Silent failures when the wrong type is provided.
- No self-documenting JSON — requires external documentation.
- Cannot validate types at parse time.
- Custom components are completely opaque.

### Option 2: Add an Explicit `stateHostType` Field per StateHost Reference

Each property that references a StateHost becomes a structured object instead of a plain string:

```json
{
  "type": "TextField",
  "properties": {
    "type": "TextFieldProps",
    "valueStateHost": {
      "name": "search_query",
      "type": "String"
    }
  }
}
```

**Pros:**
- JSON is fully self-documenting — the type is visible next to the name.
- Enables runtime validation: the library can check the registered StateHost matches the declared type before casting.
- Tooling and editors can validate type correctness.
- Custom components can declare their expected types.

**Cons:**
- Breaking change to the JSON schema for `TextFieldProps` and `DialogProps`.
- More verbose JSON.
- Introduces a new serializable data class (`StateHostRef` or similar).
- For built-in components, the type is redundant — TextField always expects String.
- The renderer must still do the unchecked cast internally; the declared type only enables an extra validation layer.

### Option 3: Convention-Based Naming with Documentation (Selected)

Keep StateHost references as plain strings, but establish a naming convention where the **property name** encodes the expected type semantically. Document the expected type for each built-in property in the library's public API (KDoc and JSON Schema).

Current convention already in use:
- `valueStateHostName` (in `TextFieldProps`) → `StateHost<String>` — the "value" of a text field is always a string.
- `visibilityStateHostName` (in `DialogProps`) → `StateHost<Boolean>` — "visibility" is always a boolean.

For future components:
- `checkedStateHostName` (Switch/Checkbox) → `StateHost<Boolean>`
- `selectedIndexStateHostName` (TabBar) → `StateHost<Int>`
- `progressStateHostName` (Slider) → `StateHost<Float>`

To address silent failures, add a runtime warning log when the cast fails instead of failing silently.

**Pros:**
- No breaking changes to the JSON schema.
- Minimal JSON payload.
- Property names are self-documenting (developers can infer the type from the semantic name).
- For built-in components, the convention eliminates ambiguity.
- Adding a warning log helps developers diagnose type mismatches.

**Cons:**
- Convention is not enforceable by the compiler or JSON schema — relies on documentation.
- Custom components still have no type information in the JSON.
- A misspelled or unclear property name could cause confusion.

### Option 4: Type Enum in StateHost Registration (App-Side Only)

Instead of changing the JSON, change how the host app registers StateHosts by requiring a type tag:

```kotlin
registerStateHost("search_query", StateHostType.STRING, myStringStateHost)
```

The library validates at registration time that the StateHost's generic type matches the declared type.

**Pros:**
- No JSON schema change.
- Validation happens at app startup, before any rendering.
- Clear error messages at registration time.

**Cons:**
- Does not help JSON authors understand what type is expected.
- Adds API complexity on the host app side.
- Type erasure in Kotlin/JVM makes runtime generic type checking unreliable.

## Decision

**We will implement Option 3: Convention-Based Naming with Documentation**, combined with runtime warning logs on type mismatch.

### Rationale

1. **Built-in components have deterministic types.** A TextField's value is always `String`. A Dialog's visibility is always `Boolean`. Adding an explicit type field to the JSON would be redundant for every built-in component — it would always say what the property name already implies.

2. **The naming convention is already natural.** The properties `valueStateHostName` and `visibilityStateHostName` clearly convey their types through their semantic meaning. This pattern extends naturally: `checkedStateHostName` → Boolean, `selectedIndexStateHostName` → Int, `progressStateHostName` → Float.

3. **No breaking changes.** Options 2 and 4 require changes to the JSON schema or host API. Option 3 preserves backward compatibility.

4. **Silent failures are the real problem.** The most actionable improvement is adding a warning log when a StateHost cast fails, so developers can quickly diagnose type mismatches. This addresses the core pain point without adding structural complexity.

5. **Custom components are the exception, not the rule.** Custom components already require the host app to provide both the renderer and the StateHost. The developer writing the custom renderer and the developer providing the StateHost are likely the same person (or team), so the type contract is established outside the library.

### Implementation Requirements

1. **Naming convention:** All new StateHost reference properties must follow the pattern `{semanticName}StateHostName` where `{semanticName}` clearly implies the type (e.g., `value` → String, `visibility`/`checked` → Boolean, `selectedIndex` → Int, `progress` → Float).

2. **Warning logs:** When a renderer retrieves a StateHost and the cast fails (returns null), log a warning message that includes:
   - The StateHost name that was looked up.
   - The expected type.
   - A suggestion to check the registered StateHost type.

3. **KDoc:** Every `*StateHostName` property in NodeProperties must include a KDoc comment specifying the expected `StateHost<T>` type.

4. **JSON Schema documentation:** The project documentation must include a table mapping each `*StateHostName` property to its expected type.

## Consequences

### Positive

- Zero breaking changes to JSON schema or host API.
- Property names are self-documenting for built-in components.
- Warning logs eliminate the silent failure problem.
- Convention scales naturally to future components.
- KDoc and documentation provide a single source of truth for types.

### Negative

- Convention is not compiler-enforced — a developer could name a property `fooStateHostName` without the name implying the type.
- Custom components still rely on external documentation for type contracts.
- Warning logs require a logging mechanism (e.g., `println` or a pluggable logger).

### Future

- If the library evolves to support more complex state types (e.g., `List<String>`, custom data classes), the naming convention may not be sufficient. At that point, revisit Option 2 (structured StateHost references with explicit types).
- When adding JSON Schema validation (planned in Phase 1), include rules that verify `*StateHostName` properties are present and match expected patterns.

## References

- `StateHost<T>` interface: `library/src/commonMain/kotlin/.../state/StateHost.kt`
- Current StateHost usages in renderers: `library/src/commonMain/kotlin/.../renderer/TextFieldRenderer.kt` and `AlertDialogRenderer.kt`
- `LocalStateHost` definition: `library/src/commonMain/kotlin/.../JsonToCompose.kt` (line 29)
- ADR-001: Snapshot testing approach (precedent for this ADR format)
