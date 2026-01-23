# Plan de Actualización - Versión 1.1.0

## Resumen

Este documento define las tareas necesarias para lanzar la versión 1.1.0 de json-to-compose, que incluye la nueva funcionalidad de **Custom Components** para extensibilidad sin fork.

---

## 1. Actualizar Versión

**Archivo:** `library/build.gradle.kts`

```kotlin
// Cambiar de:
version = "1.0.3"

// A:
version = "1.1.0"
```

**Justificación:** Nueva funcionalidad (Custom Components) justifica minor version bump según SemVer.

---

## 2. Actualizar README.md

### 2.1 Corregir versión en instalación

```kotlin
// Cambiar de:
implementation("com.jesusdmedinac:json-to-compose:1.0.1")

// A:
implementation("com.jesusdmedinac:json-to-compose:1.1.0")
```

### 2.2 Actualizar sección de Uso

La API actual usa `CompositionLocalProvider` en lugar de pasar `Behavior` directamente. Actualizar ejemplo:

```kotlin
@Composable
fun App() {
    val behaviors = mapOf(
        "button_clicked" to object : Behavior {
            override fun onClick() {
                println("Button clicked!")
            }
        }
    )

    CompositionLocalProvider(LocalBehavior provides behaviors) {
        MaterialTheme {
            JSON_AS_STRING.ToCompose()
        }
    }
}
```

### 2.3 Actualizar JSON de ejemplo

El JSON actual requiere estructura con `properties` y `type`. Ejemplo correcto:

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
        }
      }
    ]
  }
}
```

### 2.4 Actualizar lista de componentes soportados

Componentes actuales:

- Text
- Button
- Column
- Row
- Box
- Image (URL y recursos locales)
- TextField
- LazyColumn
- LazyRow
- Scaffold
- **Custom** (NUEVO en 1.1.0)

### 2.5 Agregar sección de CompositionLocals

Documentar los 4 CompositionLocals disponibles:

| CompositionLocal | Propósito |
|------------------|-----------|
| `LocalBehavior` | Manejo de eventos onClick |
| `LocalStateHost` | Estado para TextField |
| `LocalDrawableResources` | Recursos drawable locales |
| `LocalCustomRenderers` | Componentes custom (NUEVO) |

### 2.6 Agregar sección de Custom Components

Nueva sección documentando:

1. Cómo definir un renderer custom
2. Cómo proveerlo via `LocalCustomRenderers`
3. Estructura JSON para Custom
4. Ejemplo completo

```kotlin
// 1. Definir el renderer
val customRenderers = mapOf(
    "ProductCard" to { node: ComposeNode ->
        val props = node.properties as? NodeProperties.CustomProps
        val data = props?.customData
        // Renderizar componente
    }
)

// 2. Proveer al contexto
CompositionLocalProvider(LocalCustomRenderers provides customRenderers) {
    jsonString.ToCompose()
}
```

```json
// 3. JSON del servidor
{
  "type": "Custom",
  "properties": {
    "type": "CustomProps",
    "customType": "ProductCard",
    "customData": {
      "title": "Mi Producto",
      "price": "99.99"
    }
  }
}
```

### 2.7 Agregar sección de Modifiers

Documentar operaciones de `ComposeModifier` disponibles:

- `Padding(value)`
- `FillMaxSize`
- `FillMaxWidth`
- `FillMaxHeight`
- `Width(value)`
- `Height(value)`
- `BackgroundColor(hexColor)`

### 2.8 Agregar sección de Alignment y Arrangement

Documentar valores disponibles para layouts:

**Alignment (Box):**
- TopStart, TopCenter, TopEnd
- CenterStart, Center, CenterEnd
- BottomStart, BottomCenter, BottomEnd

**Vertical Arrangement (Column):**
- Top, Bottom, Center, SpaceEvenly, SpaceBetween, SpaceAround

**Horizontal Arrangement (Row):**
- Start, End, Center, SpaceEvenly, SpaceBetween, SpaceAround

---

## 3. Checklist Pre-Release

- [ ] Actualizar versión en `library/build.gradle.kts`
- [ ] Actualizar versión en README.md
- [ ] Actualizar ejemplo de uso en README.md
- [ ] Actualizar JSON de ejemplo en README.md
- [ ] Actualizar lista de componentes en README.md
- [ ] Agregar sección CompositionLocals en README.md
- [ ] Agregar sección Custom Components en README.md
- [ ] Agregar sección Modifiers en README.md
- [ ] Agregar sección Alignment/Arrangement en README.md
- [ ] Ejecutar tests
- [ ] Compilar en todas las plataformas
- [ ] Crear tag de Git v1.1.0
- [ ] Publicar en Maven Central

---

## 4. Changelog

### v1.1.0

**Nueva funcionalidad:**
- Soporte para Custom Components via `LocalCustomRenderers`
- Los desarrolladores pueden extender la librería sin hacer fork

**Nuevos componentes:**
- `Custom` - Permite registrar y usar componentes personalizados

**API:**
- Nuevo `LocalCustomRenderers` CompositionLocal
- Nuevo `NodeProperties.CustomProps` con `customType` y `customData`

---

## 5. Notas

- La nueva funcionalidad es 100% backward compatible
- No hay breaking changes desde 1.0.x
- Los componentes existentes siguen funcionando igual