# AGENTS.md - Guía para Agentes de IA

Este documento proporciona contexto esencial para que agentes de IA colaboren efectivamente en este proyecto.

## Visión General

**json-to-compose** es un framework de Server-Driven UI (SDUI) para Kotlin Multiplatform que convierte estructuras JSON en componentes de Jetpack Compose en tiempo de ejecución.

**Caso de uso principal:** El backend controla la UI de la aplicación sin requerir actualizaciones de la app.

## Estructura del Proyecto

```
json-to-compose/
├── library/       → Librería core (publicada en Maven Central v1.0.3)
├── composy/       → Editor visual web/desktop
├── composeApp/    → App demo multiplataforma
├── server/        → Backend Ktor
└── shared/        → Utilidades compartidas
```

### Módulo Principal: `/library`

Ubicación del código core:
```
library/src/commonMain/kotlin/com/jesusdmedinac/jsontocompose/
├── JsonToCompose.kt              → Entry point, CompositionLocals, router
├── model/
│   ├── ComposeNode.kt            → Nodo serializable del árbol UI
│   ├── ComposeType.kt            → Enum de tipos de componentes
│   ├── ComposeModifier.kt        → Modificadores serializables
│   └── NodeProperties.kt         → Props específicos por componente (sealed interface)
├── renderer/
│   ├── ComponentRenderers.kt     → Funciones @Composable por componente
│   ├── Alignment.kt              → Mappers String → Compose Alignment
│   └── Arrangment.kt             → Mappers String → Compose Arrangement
├── modifier/
│   └── ModifierMapper.kt         → Aplica operaciones de modificador
├── behavior/
│   └── Behavior.kt               → Interface para eventos click
└── state/
    └── StateHost.kt              → Interface para manejo de estado
```

## Arquitectura Core

### Flujo de Datos

```
JSON String → kotlinx.serialization → ComposeNode → Router → Renderer específico → Compose UI
```

### Router Principal (`JsonToCompose.kt:34-47`)

```kotlin
@Composable
fun ComposeNode.ToCompose() {
    when (type) {
        ComposeType.Column -> ToColumn()
        ComposeType.Row -> ToRow()
        ComposeType.Box -> ToBox()
        ComposeType.Text -> ToText()
        ComposeType.Button -> ToButton()
        ComposeType.Image -> ToImage()
        ComposeType.TextField -> ToTextField()
        ComposeType.LazyColumn -> ToLazyColumn()
        ComposeType.LazyRow -> ToLazyRow()
        ComposeType.Scaffold -> ToScaffold()
    }
}
```

### Componentes Soportados

| Tipo | Props Class | Categoría |
|------|-------------|-----------|
| Text | TextProps | Leaf |
| Image | ImageProps | Leaf |
| TextField | TextFieldProps | Leaf |
| Button | ButtonProps | Single Child |
| Scaffold | ScaffoldProps | Single Child |
| Column | ColumnProps | Container |
| Row | RowProps | Container |
| Box | BoxProps | Container |
| LazyColumn | ColumnProps | Container (lazy) |
| LazyRow | RowProps | Container (lazy) |

## Cómo Agregar un Nuevo Componente

### Paso 1: Agregar al enum `ComposeType`

Archivo: `model/ComposeType.kt`

```kotlin
enum class ComposeType {
    // ... existentes
    NuevoComponente;

    fun isLayout(): Boolean = when (this) {
        Column, Row, Box, NuevoComponente -> true  // si es layout
        else -> false
    }

    fun hasChild(): Boolean = when (this) {
        Button, NuevoComponente -> true  // si tiene un solo hijo
        else -> false
    }
}
```

### Paso 2: Crear Props en `NodeProperties`

Archivo: `model/NodeProperties.kt`

```kotlin
@Serializable
@SerialName("NuevoComponenteProps")
data class NuevoComponenteProps(
    val propiedad1: String? = null,
    val propiedad2: Int? = null,
    val children: List<ComposeNode>? = null,  // si es container
    val child: ComposeNode? = null,            // si es single child
) : NodeProperties
```

**Importante:** Usar `@SerialName` para serialización JSON correcta.

### Paso 3: Crear el Renderer

Archivo: `renderer/ComponentRenderers.kt`

```kotlin
@Composable
fun ComposeNode.ToNuevoComponente() {
    val props = properties as? NodeProperties.NuevoComponenteProps ?: return
    val modifier = Modifier from composeModifier

    // Implementar el composable
    NuevoComponente(
        modifier = modifier,
        propiedad1 = props.propiedad1 ?: "default",
    ) {
        props.children?.forEach { it.ToCompose() }
        // o para single child:
        props.child?.ToCompose()
    }
}
```

### Paso 4: Agregar al Router

Archivo: `JsonToCompose.kt`

```kotlin
@Composable
fun ComposeNode.ToCompose() {
    when (type) {
        // ... existentes
        ComposeType.NuevoComponente -> ToNuevoComponente()
    }
}
```

### Paso 5: Actualizar `ComposeNode.children()` (si aplica)

Archivo: `model/ComposeNode.kt:25-30`

```kotlin
private fun NodeProperties?.children(): List<ComposeNode> = when(this) {
    is NodeProperties.ColumnProps -> children
    is NodeProperties.RowProps -> children
    is NodeProperties.BoxProps -> children
    is NodeProperties.NuevoComponenteProps -> children  // agregar si es container
    else -> null
} ?: emptyList()
```

## Sistema de Modificadores

### Operaciones Disponibles

| Operación | Parámetros | Efecto |
|-----------|------------|--------|
| Padding | `value: Int` | Padding en dp |
| FillMaxSize | - | Llena ancho y alto |
| FillMaxWidth | - | Llena ancho |
| FillMaxHeight | - | Llena alto |
| Width | `value: Int` | Ancho fijo en dp |
| Height | `value: Int` | Alto fijo en dp |
| BackgroundColor | `hexColor: String` | Color ARGB (#AARRGGBB) |

### Agregar Nueva Operación de Modificador

1. **Agregar al enum** (`modifier/ModifierMapper.kt:15-23`):
```kotlin
enum class ModifierOperation {
    // ... existentes
    NuevaOperacion;
}
```

2. **Crear clase sealed** (`model/ComposeModifier.kt`):
```kotlin
@Serializable
@SerialName("NuevaOperacion")
data class NuevaOperacion(val valor: Int) : Operation(
    modifierOperation = ModifierOperation.NuevaOperacion
)
```

3. **Implementar en mapper** (`modifier/ModifierMapper.kt:25-39`):
```kotlin
is ComposeModifier.Operation.NuevaOperacion -> result.nuevaOperacion(operation.valor.dp)
```

## Inyección de Dependencias (CompositionLocal)

### Recursos Drawable
```kotlin
val LocalDrawableResources = staticCompositionLocalOf<Map<String, DrawableResource>> { emptyMap() }
```

### Comportamientos (Clicks)
```kotlin
val LocalBehavior = staticCompositionLocalOf<Map<String, Behavior>> { emptyMap() }

interface Behavior {
    fun onClick()
}
```

### Estado (TextField)
```kotlin
val LocalStateHost = staticCompositionLocalOf<Map<String, StateHost<*>>> { emptyMap() }

interface StateHost<T> {
    val state: T
    fun onStateChange(newState: T)
}
```

### Uso en App
```kotlin
CompositionLocalProvider(
    LocalDrawableResources provides mapOf("icon" to Res.drawable.icon),
    LocalBehavior provides mapOf("btn_click" to myBehavior),
    LocalStateHost provides mapOf("text_state" to myStateHost),
) {
    jsonNode.ToCompose()
}
```

## Alignment y Arrangement

### Alignment 2D (Box)
`TopStart`, `TopCenter`, `TopEnd`, `CenterStart`, `Center`, `CenterEnd`, `BottomStart`, `BottomCenter`, `BottomEnd`

### Vertical Alignment (Row)
`Top`, `CenterVertically`, `Bottom`

### Horizontal Alignment (Column)
`Start`, `CenterHorizontally`, `End`

### Vertical Arrangement (Column/LazyColumn)
`Top`, `Bottom`, `Center`, `SpaceEvenly`, `SpaceBetween`, `SpaceAround`

### Horizontal Arrangement (Row/LazyRow)
`Start`, `End`, `Center`, `SpaceEvenly`, `SpaceBetween`, `SpaceAround`

## Convenciones de Código

### Nomenclatura
- **Props:** `{ComponentName}Props` (e.g., `ColumnProps`, `TextProps`)
- **Renderers:** `To{ComponentName}()` como extension function de `ComposeNode`
- **Mappers:** `to{TargetType}()` como extension function de String

### Serialización
- Usar `@Serializable` en todas las data classes
- Usar `@SerialName("NombreExplicito")` para control del JSON
- Usar `@Transient` para campos que no deben serializarse

### Patrones
- Props nullable con valores default: `val prop: Type? = null`
- Early return si props no coinciden: `val props = properties as? Props ?: return`
- Modifier siempre aplicado: `val modifier = Modifier from composeModifier`

## Comandos Gradle Útiles

```bash
# Compilar librería
./gradlew :library:build

# Tests
./gradlew :library:test

# Publicar a Maven Local (para pruebas)
./gradlew :library:publishToMavenLocal

# Ejecutar app demo (Desktop)
./gradlew :composeApp:run

# Ejecutar editor Composy (Desktop)
./gradlew :composy:run

# Servidor Ktor
./gradlew :server:run
```

## Ejemplo JSON Completo

```json
{
  "type": "Column",
  "properties": {
    "type": "ColumnProps",
    "children": [
      {
        "type": "Text",
        "properties": {
          "type": "TextProps",
          "text": "Hello World"
        },
        "composeModifier": {
          "operations": [
            { "type": "Padding", "value": 16 }
          ]
        }
      },
      {
        "type": "Button",
        "properties": {
          "type": "ButtonProps",
          "onClickEventName": "my_button",
          "child": {
            "type": "Text",
            "properties": {
              "type": "TextProps",
              "text": "Click me"
            }
          }
        }
      }
    ],
    "verticalArrangement": "Center",
    "horizontalAlignment": "CenterHorizontally"
  },
  "composeModifier": {
    "operations": [
      { "type": "FillMaxSize" }
    ]
  }
}
```

## Plataformas Soportadas

- Android (SDK mínimo según `libs.versions`)
- iOS (x64, arm64, simulator arm64)
- Desktop (JVM)
- WebAssembly (wasmJs)

## Dependencias Principales

- **Jetpack Compose** - UI framework
- **kotlinx.serialization** - JSON parsing
- **Coil** - Carga de imágenes (con soporte Ktor)
- **Ktor** - Cliente HTTP

## Checklist para Nuevos Componentes

- [ ] Agregar tipo a `ComposeType` enum
- [ ] Crear `{Nombre}Props` en `NodeProperties` sealed interface
- [ ] Implementar `To{Nombre}()` renderer
- [ ] Agregar case al router en `ComposeNode.ToCompose()`
- [ ] Actualizar `children()` si es container
- [ ] Agregar tests
- [ ] Actualizar este documento si hay nuevos patrones
