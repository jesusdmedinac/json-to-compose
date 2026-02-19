# Phase: Actions System — Declarative State and Behaviors from JSON

**Status:** Planned
**Date:** 2026-02-06

## Idea (Step 1)

La libreria json-to-compose permite renderizar UI completa desde JSON. Sin embargo, para que esa UI sea interactiva, el consumidor debe escribir codigo Kotlin significativo: crear instancias de `MutableStateHost`, definir objetos `Behavior`, mapearlos por nombre, y proveerlos via `CompositionLocalProvider`. Esto significa que la interactividad sigue atada al codigo nativo.

El objetivo es que un documento JSON pueda declarar **estado inicial**, **acciones nombradas** y el **arbol de UI**, de modo que la libreria auto-genere los `StateHost` y `Behavior` necesarios y los conecte automaticamente. El consumidor solo necesita llamar `ComposeDocument.ToCompose()` y toda la interactividad funciona sin escribir Kotlin adicional.

### Ejemplo del problema actual

```kotlin
// El consumidor debe escribir TODO esto en Kotlin:
val switchState = remember { MutableStateHost(false) }
val dialogState = remember { MutableStateHost(false) }
val behaviors = mapOf(
    "toggle_switch" to object : Behavior { override fun invoke() { println("toggled") } },
    "show_dialog" to object : Behavior { override fun invoke() { dialogState.onStateChange(true) } },
    "dismiss_dialog" to object : Behavior { override fun invoke() { dialogState.onStateChange(false) } },
)
val stateHosts = mapOf<String, StateHost<*>>(
    "switch_state" to switchState,
    "dialog_visible" to dialogState,
)
CompositionLocalProvider(
    LocalBehavior provides behaviors,
    LocalStateHost provides stateHosts,
) { root.ToCompose() }
```

### Ejemplo del objetivo

```json
{
  "initialState": {
    "switch_state": false,
    "dialog_visible": false,
    "text_value": "",
    "counter": 0
  },
  "actions": {
    "toggle_switch": [
      { "action": "toggleState", "stateKey": "switch_state" }
    ],
    "show_dialog": [
      { "action": "setState", "stateKey": "dialog_visible", "value": true }
    ],
    "dismiss_dialog": [
      { "action": "setState", "stateKey": "dialog_visible", "value": false },
      { "action": "log", "message": "Dialog dismissed" }
    ]
  },
  "root": {
    "type": "Column",
    "children": [
      {
        "type": "Switch",
        "properties": {
          "checkedStateHostName": "switch_state",
          "onCheckedChangeEventName": "toggle_switch"
        }
      }
    ]
  }
}
```

```kotlin
// El consumidor solo escribe esto:
val document = json.decodeFromString<ComposeDocument>(jsonString)
document.ToCompose()
```

## Scope

- **In scope**: `library/` module only
  - Nuevos modelos: `ComposeAction`, `ComposeDocument`
  - Nuevo runtime: `ActionDispatcher`
  - Auto-wiring: `ComposeDocument.ToCompose()` extension
  - Extensibilidad: `LocalCustomActionHandlers` para acciones custom
  - Serializacion JSON: `ComposeDocument` serializable con kotlinx.serialization
  - Tests unitarios y de integracion
- **Out of scope**: `composeApp/`, `composy/`, `server/` (se benefician pero no se modifican en esta fase)

## Success Criteria

1. `ComposeDocument` encapsula `initialState`, `actions`, y `root` en un solo objeto serializable
2. `ComposeAction` soporta al menos: `SetState`, `ToggleState`, `Log`, `Sequence`, `Custom`
3. `ComposeDocument.ToCompose()` auto-genera `MutableStateHost` y `Behavior` sin codigo del consumidor
4. La API existente (`Behavior`, `StateHost`, `LocalBehavior`, `LocalStateHost`) sigue funcionando sin cambios
5. Acciones custom extensibles via `LocalCustomActionHandlers`
6. Serializacion JSON completa de ida y vuelta (round-trip)
7. Tests cubren cada tipo de accion, auto-wiring, y backward compatibility

## Design Decisions

### ComposeAction como sealed class

```
ComposeAction
├── SetState(stateKey: String, value: JsonElement)
├── ToggleState(stateKey: String)
├── Log(message: String)
├── Sequence(actions: List<ComposeAction>)
└── Custom(type: String, params: Map<String, JsonElement>)
```

- `SetState` usa `JsonElement` para soportar cualquier tipo (String, Boolean, Int, etc.)
- `ToggleState` es un shortcut para el patron comun de invertir un boolean
- `Sequence` permite encadenar acciones (ej: "cierra dialog + loggea + navega")
- `Custom` es el escape hatch para acciones que el consumidor define (ej: navigate, HTTP calls)

### ComposeDocument como wrapper

```
ComposeDocument
├── initialState: Map<String, JsonElement>
├── actions: Map<String, List<ComposeAction>>
└── root: ComposeNode
```

- `initialState` se convierte en `Map<String, MutableStateHost<*>>` en runtime
- `actions` se convierte en `Map<String, Behavior>` donde cada Behavior despacha su lista de acciones
- `root` se renderiza con el contexto ya preparado

### Extensibilidad via Custom Actions

```kotlin
val LocalCustomActionHandlers = staticCompositionLocalOf<Map<String, (ComposeAction.Custom) -> Unit>> { emptyMap() }
```

- El consumidor registra handlers para tipos custom (ej: "navigate", "httpRequest")
- La libreria no tiene opinion sobre navegacion ni networking, pero permite extenderla

### Backward Compatibility

- `ComposeNode.ToCompose()` sigue funcionando exactamente como antes
- `ComposeDocument.ToCompose()` es una nueva API que agrega auto-wiring encima
- El consumidor puede mezclar estado manual y auto-generado

## Technical References

- **Existing Behavior**: `library/.../behavior/Behavior.kt` — interfaz con `invoke()`
- **Existing StateHost**: `library/.../state/StateHost.kt` + `MutableStateHost.kt` — estado reactivo con Compose
- **Existing StateHostResolver**: `library/.../state/StateHostResolver.kt` — resolucion con 3 niveles de prioridad
- **Existing LocalBehavior/LocalStateHost**: `library/.../JsonToCompose.kt` — CompositionLocals
- **Convention reference**: Follows `docs/projects/phase-1-solidify-library/` structure

## Implementation Order

### Phase A: Models (Features 1 + 2)
1-6. Definir `ComposeAction` sealed class con sus 5 subtipos
7-11. Definir `ComposeDocument` con initialState, actions, root

### Phase B: Runtime (Features 3 + 4 + 5)
12-18. Implementar `ActionDispatcher` que ejecuta cada tipo de accion
19-24. Implementar auto-wiring en `ComposeDocument.ToCompose()`
25-28. Implementar `LocalCustomActionHandlers` para extensibilidad

### Phase C: Serialization (Feature 6)
29-33. Implementar serializacion JSON de ComposeDocument y ComposeAction

### Phase D: Compatibility (Feature 7)
34-37. Verificar backward compatibility con API existente

## Files Modified During Implementation

| File | Action |
|------|--------|
| `library/.../model/ComposeAction.kt` | **NEW**: Sealed class con tipos de accion |
| `library/.../model/ComposeDocument.kt` | **NEW**: Wrapper con initialState + actions + root |
| `library/.../runtime/ActionDispatcher.kt` | **NEW**: Ejecutor de acciones |
| `library/.../runtime/DocumentCompose.kt` | **NEW**: ComposeDocument.ToCompose() extension |
| `library/.../JsonToCompose.kt` | Agregar LocalCustomActionHandlers |
| `library/build.gradle.kts` | Posiblemente agregar kotlinx-serialization-json si no existe |
| `library/.../test/...` | **NEW**: Tests para cada feature |
