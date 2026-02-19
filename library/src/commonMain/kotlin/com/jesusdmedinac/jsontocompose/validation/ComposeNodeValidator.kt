package com.jesusdmedinac.jsontocompose.validation

import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val lenientJson = Json { ignoreUnknownKeys = true }
private val strictJson = Json { ignoreUnknownKeys = false }

/**
 * Validates this JSON string as a [ComposeNode][com.jesusdmedinac.jsontocompose.model.ComposeNode] tree.
 *
 * Performs pre-parse checks (empty, malformed, unknown types/modifiers) and post-parse
 * checks (properties type match, tree structure, arrangement/alignment, modifier values).
 * All errors are collected — the validator does not fail fast.
 *
 * @param mode [ValidationMode.Strict] reports unrecognized JSON properties as warnings;
 *   [ValidationMode.Permissive] (default) ignores them.
 * @return A [ValidationResult] containing all findings.
 */
fun String.validateJson(
    mode: ValidationMode = ValidationMode.Permissive,
): ValidationResult {
    if (isBlank()) {
        return ValidationResult(
            listOf(
                ValidationError(
                    path = "root",
                    message = "JSON string is empty or blank",
                    code = ValidationErrorCode.EMPTY_JSON,
                )
            )
        )
    }

    val jsonObject: JsonObject
    try {
        jsonObject = Json.parseToJsonElement(this).jsonObject
    } catch (e: Exception) {
        return ValidationResult(
            listOf(
                ValidationError(
                    path = "root",
                    message = "Malformed JSON: ${e.message}",
                    code = ValidationErrorCode.MALFORMED_JSON,
                )
            )
        )
    }

    val preParseErrors = mutableListOf<ValidationError>()
    validateJsonObject(jsonObject, "root", preParseErrors)
    if (preParseErrors.any { it.severity == ValidationSeverity.Error }) {
        return ValidationResult(preParseErrors)
    }

    val node: ComposeNode
    try {
        node = lenientJson.decodeFromString<ComposeNode>(this)
    } catch (e: Exception) {
        return ValidationResult(
            listOf(
                ValidationError(
                    path = "root",
                    message = "Failed to deserialize JSON: ${e.message}",
                    code = ValidationErrorCode.MALFORMED_JSON,
                )
            )
        )
    }

    val errors = mutableListOf<ValidationError>()
    errors.addAll(preParseErrors)
    node.validateNode("root", errors)

    if (mode == ValidationMode.Strict) {
        detectUnrecognizedProperties(jsonObject, "root", errors)
    }

    return ValidationResult(errors)
}

/**
 * Validates this deserialized [ComposeNode] tree.
 *
 * Checks properties type match, tree structure, arrangement/alignment values,
 * and modifier values recursively. All errors are collected.
 *
 * @param mode Validation mode. Strict mode has limited effect on already-deserialized nodes
 *   since unrecognized JSON properties are lost during deserialization.
 * @return A [ValidationResult] containing all findings.
 */
fun ComposeNode.validate(
    mode: ValidationMode = ValidationMode.Permissive,
): ValidationResult {
    val errors = mutableListOf<ValidationError>()
    validateNode("root", errors)

    if (mode == ValidationMode.Strict) {
        val json = try {
            Json.encodeToString(this)
        } catch (_: Exception) {
            null
        }
        if (json != null) {
            // In strict mode on a deserialized node, we can't detect unrecognized properties
            // since the node is already deserialized. This path is primarily for String.validateJson.
        }
    }

    return ValidationResult(errors)
}

private fun validateJsonObject(
    obj: JsonObject,
    path: String,
    errors: MutableList<ValidationError>,
) {
    val typeValue = obj["type"]?.jsonPrimitive?.content
    if (typeValue != null) {
        val validTypes = ComposeType.entries.map { it.name }.toSet()
        if (typeValue !in validTypes) {
            errors.add(
                ValidationError(
                    path = path,
                    message = "Unknown compose type: '$typeValue'",
                    code = ValidationErrorCode.UNKNOWN_COMPOSE_TYPE,
                )
            )
            return
        }
    }

    val modifierObj = obj["composeModifier"]?.jsonObject
    val operations = modifierObj?.get("operations")?.jsonArray
    operations?.forEachIndexed { index, element ->
        val opObj = element.jsonObject
        val opType = opObj["type"]?.jsonPrimitive?.content
        if (opType != null) {
            val validOps = setOf(
                "Padding", "FillMaxSize", "FillMaxWidth", "FillMaxHeight",
                "Width", "Height", "BackgroundColor", "Border", "Shadow",
                "Clip", "Background", "Alpha", "Rotate",
            )
            if (opType !in validOps) {
                errors.add(
                    ValidationError(
                        path = "$path.composeModifier.operations[$index]",
                        message = "Unknown modifier operation: '$opType'",
                        code = ValidationErrorCode.UNKNOWN_MODIFIER_OPERATION,
                    )
                )
            }
        }
    }

    val propsObj = obj["properties"]?.jsonObject
    if (propsObj != null) {
        val childNode = propsObj["child"]
        if (childNode is JsonObject) {
            validateJsonObject(childNode, "$path.child", errors)
        }

        for (key in listOf("children", "actions")) {
            val childrenArr = propsObj[key]
            if (childrenArr is JsonArray) {
                childrenArr.forEachIndexed { index, element ->
                    if (element is JsonObject) {
                        validateJsonObject(element, "$path.$key[$index]", errors)
                    }
                }
            }
        }

        for (key in listOf("topBar", "bottomBar", "title", "text",
            "navigationIcon", "confirmButton", "dismissButton", "label", "icon")) {
            val childNode2 = propsObj[key]
            if (childNode2 is JsonObject) {
                validateJsonObject(childNode2, "$path.$key", errors)
            }
        }
    }
}

private fun ComposeNode.validateNode(
    path: String,
    errors: MutableList<ValidationError>,
) {
    validatePropertiesType(path, errors)
    validateTreeStructure(path, errors)
    validateArrangementAndAlignment(path, errors)
    validateModifiers(path, errors)
    recurseChildren(path, errors)
}

private fun ComposeNode.validatePropertiesType(
    path: String,
    errors: MutableList<ValidationError>,
) {
    val props = properties ?: return
    val expectedClass = EXPECTED_PROPERTIES_TYPE[type] ?: return
    if (!expectedClass.isInstance(props)) {
        errors.add(
            ValidationError(
                path = path,
                message = "Expected ${expectedClass.simpleName} for type ${type.name}, got ${props::class.simpleName}",
                code = ValidationErrorCode.PROPERTIES_TYPE_MISMATCH,
            )
        )
    }
}

private fun ComposeNode.validateTreeStructure(
    path: String,
    errors: MutableList<ValidationError>,
) {
    if (type !in LEAF_TYPES) return
    val props = properties ?: return

    val hasChildren = when (props) {
        is NodeProperties.ColumnProps -> !props.children.isNullOrEmpty()
        is NodeProperties.RowProps -> !props.children.isNullOrEmpty()
        is NodeProperties.BoxProps -> !props.children.isNullOrEmpty()
        is NodeProperties.BottomBarProps -> !props.children.isNullOrEmpty()
        is NodeProperties.ButtonProps -> props.child != null
        is NodeProperties.CardProps -> props.child != null
        is NodeProperties.ScaffoldProps -> props.child != null || props.topBar != null || props.bottomBar != null
        else -> false
    }

    if (hasChildren) {
        errors.add(
            ValidationError(
                path = path,
                message = "Leaf node type ${type.name} must not have children",
                code = ValidationErrorCode.CHILDREN_ON_LEAF_NODE,
            )
        )
    }
}

private fun ComposeNode.validateArrangementAndAlignment(
    path: String,
    errors: MutableList<ValidationError>,
) {
    val props = properties ?: return

    when (props) {
        is NodeProperties.ColumnProps -> {
            props.verticalArrangement?.let { arr ->
                if (arr !in VALID_VERTICAL_ARRANGEMENTS) {
                    errors.add(
                        ValidationError(
                            path = "$path.verticalArrangement",
                            message = "Invalid vertical arrangement: '$arr'",
                            code = ValidationErrorCode.INVALID_ARRANGEMENT,
                        )
                    )
                }
            }
            props.horizontalAlignment?.let { align ->
                if (align !in VALID_HORIZONTAL_ALIGNMENTS) {
                    errors.add(
                        ValidationError(
                            path = "$path.horizontalAlignment",
                            message = "Invalid horizontal alignment: '$align'",
                            code = ValidationErrorCode.INVALID_ALIGNMENT,
                        )
                    )
                }
            }
        }

        is NodeProperties.RowProps -> {
            props.horizontalArrangement?.let { arr ->
                if (arr !in VALID_HORIZONTAL_ARRANGEMENTS) {
                    errors.add(
                        ValidationError(
                            path = "$path.horizontalArrangement",
                            message = "Invalid horizontal arrangement: '$arr'",
                            code = ValidationErrorCode.INVALID_ARRANGEMENT,
                        )
                    )
                }
            }
            props.verticalAlignment?.let { align ->
                if (align !in VALID_VERTICAL_ALIGNMENTS) {
                    errors.add(
                        ValidationError(
                            path = "$path.verticalAlignment",
                            message = "Invalid vertical alignment: '$align'",
                            code = ValidationErrorCode.INVALID_ALIGNMENT,
                        )
                    )
                }
            }
        }

        is NodeProperties.BoxProps -> {
            props.contentAlignment?.let { align ->
                if (align !in VALID_ALIGNMENTS) {
                    errors.add(
                        ValidationError(
                            path = "$path.contentAlignment",
                            message = "Invalid alignment: '$align'",
                            code = ValidationErrorCode.INVALID_ALIGNMENT,
                        )
                    )
                }
            }
        }

        else -> {}
    }
}

private fun ComposeNode.validateModifiers(
    path: String,
    errors: MutableList<ValidationError>,
) {
    composeModifier.operations.forEachIndexed { index, op ->
        val opPath = "$path.composeModifier.operations[$index]"
        when (op) {
            is ComposeModifier.Operation.Padding -> {
                if (op.value < 0) {
                    errors.add(
                        ValidationError(
                            path = opPath,
                            message = "Padding value must not be negative, got ${op.value}",
                            code = ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE,
                        )
                    )
                }
            }

            is ComposeModifier.Operation.Width -> {
                if (op.value < 0) {
                    errors.add(
                        ValidationError(
                            path = opPath,
                            message = "Width value must not be negative, got ${op.value}",
                            code = ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE,
                        )
                    )
                }
            }

            is ComposeModifier.Operation.Height -> {
                if (op.value < 0) {
                    errors.add(
                        ValidationError(
                            path = opPath,
                            message = "Height value must not be negative, got ${op.value}",
                            code = ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE,
                        )
                    )
                }
            }

            is ComposeModifier.Operation.Border -> {
                if (op.width < 0) {
                    errors.add(
                        ValidationError(
                            path = opPath,
                            message = "Border width must not be negative, got ${op.width}",
                            code = ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE,
                        )
                    )
                }
                if (!op.color.matches(HEX_COLOR_REGEX)) {
                    errors.add(
                        ValidationError(
                            path = opPath,
                            message = "Invalid hex color: '${op.color}'",
                            code = ValidationErrorCode.INVALID_HEX_COLOR,
                        )
                    )
                }
            }

            is ComposeModifier.Operation.Shadow -> {
                if (op.elevation < 0) {
                    errors.add(
                        ValidationError(
                            path = opPath,
                            message = "Shadow elevation must not be negative, got ${op.elevation}",
                            code = ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE,
                        )
                    )
                }
            }

            is ComposeModifier.Operation.Alpha -> {
                if (op.value < 0f || op.value > 1f) {
                    errors.add(
                        ValidationError(
                            path = opPath,
                            message = "Alpha value must be between 0 and 1, got ${op.value}",
                            code = ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE,
                        )
                    )
                }
            }

            is ComposeModifier.Operation.BackgroundColor -> {
                if (!op.hexColor.matches(HEX_COLOR_REGEX)) {
                    errors.add(
                        ValidationError(
                            path = opPath,
                            message = "Invalid hex color: '${op.hexColor}'",
                            code = ValidationErrorCode.INVALID_HEX_COLOR,
                        )
                    )
                }
            }

            is ComposeModifier.Operation.Background -> {
                if (!op.color.matches(HEX_COLOR_REGEX)) {
                    errors.add(
                        ValidationError(
                            path = opPath,
                            message = "Invalid hex color: '${op.color}'",
                            code = ValidationErrorCode.INVALID_HEX_COLOR,
                        )
                    )
                }
            }

            else -> {}
        }
    }
}

private fun ComposeNode.recurseChildren(
    path: String,
    errors: MutableList<ValidationError>,
) {
    val props = properties ?: return

    when (props) {
        is NodeProperties.ColumnProps -> {
            props.children?.forEachIndexed { index, child ->
                child.validateNode("$path.children[$index]", errors)
            }
        }

        is NodeProperties.RowProps -> {
            props.children?.forEachIndexed { index, child ->
                child.validateNode("$path.children[$index]", errors)
            }
        }

        is NodeProperties.BoxProps -> {
            props.children?.forEachIndexed { index, child ->
                child.validateNode("$path.children[$index]", errors)
            }
        }

        is NodeProperties.ButtonProps -> {
            props.child?.validateNode("$path.child", errors)
        }

        is NodeProperties.CardProps -> {
            props.child?.validateNode("$path.child", errors)
        }

        is NodeProperties.ScaffoldProps -> {
            props.topBar?.validateNode("$path.topBar", errors)
            props.bottomBar?.validateNode("$path.bottomBar", errors)
            props.child?.validateNode("$path.child", errors)
        }

        is NodeProperties.AlertDialogProps -> {
            props.confirmButton?.validateNode("$path.confirmButton", errors)
            props.dismissButton?.validateNode("$path.dismissButton", errors)
            props.title?.validateNode("$path.title", errors)
            props.text?.validateNode("$path.text", errors)
        }

        is NodeProperties.TopAppBarProps -> {
            props.title?.validateNode("$path.title", errors)
            props.navigationIcon?.validateNode("$path.navigationIcon", errors)
            props.actions?.forEachIndexed { index, child ->
                child.validateNode("$path.actions[$index]", errors)
            }
        }

        is NodeProperties.BottomBarProps -> {
            props.children?.forEachIndexed { index, child ->
                child.validateNode("$path.children[$index]", errors)
            }
        }

        is NodeProperties.BottomNavigationItemProps -> {
            props.label?.validateNode("$path.label", errors)
            props.icon?.validateNode("$path.icon", errors)
        }

        else -> {}
    }
}

private fun detectUnrecognizedProperties(
    obj: JsonObject,
    path: String,
    errors: MutableList<ValidationError>,
) {
    try {
        val encoded = obj.toString()
        strictJson.decodeFromString<ComposeNode>(encoded)
    } catch (e: Exception) {
        errors.add(
            ValidationError(
                path = path,
                message = "Unrecognized properties detected: ${e.message}",
                code = ValidationErrorCode.UNRECOGNIZED_PROPERTIES,
                severity = ValidationSeverity.Warning,
            )
        )
    }
}
