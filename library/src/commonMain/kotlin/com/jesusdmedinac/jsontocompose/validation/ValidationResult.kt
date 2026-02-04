package com.jesusdmedinac.jsontocompose.validation

/**
 * Severity level of a validation finding.
 */
enum class ValidationSeverity {
    /** A critical issue that makes the JSON invalid for rendering. */
    Error,
    /** A non-critical issue (e.g., unrecognized properties in strict mode). */
    Warning,
}

/**
 * Identifies the specific type of validation issue found.
 */
enum class ValidationErrorCode {
    /** The JSON string is empty or blank. */
    EMPTY_JSON,
    /** The JSON string is not valid JSON syntax. */
    MALFORMED_JSON,
    /** The `type` field does not match any [ComposeType][com.jesusdmedinac.jsontocompose.model.ComposeType]. */
    UNKNOWN_COMPOSE_TYPE,
    /** The `properties` type does not match the expected type for the component. */
    PROPERTIES_TYPE_MISMATCH,
    /** A modifier operation type is not recognized. */
    UNKNOWN_MODIFIER_OPERATION,
    /** A modifier numeric value is outside the valid range. */
    MODIFIER_VALUE_OUT_OF_RANGE,
    /** A hex color string does not match the `#AARRGGBB` format. */
    INVALID_HEX_COLOR,
    /** A leaf-type node (Text, Image, etc.) has children. */
    CHILDREN_ON_LEAF_NODE,
    /** An arrangement string is not a valid arrangement value. */
    INVALID_ARRANGEMENT,
    /** An alignment string is not a valid alignment value. */
    INVALID_ALIGNMENT,
    /** JSON contains properties not defined in the data model (strict mode only). */
    UNRECOGNIZED_PROPERTIES,
}

/**
 * Represents a single validation finding at a specific location in the node tree.
 *
 * @property path Dot-separated path to the issue (e.g., "root.children[0].composeModifier.operations[1]").
 * @property message Human-readable description of the issue.
 * @property code Machine-readable error code for programmatic handling.
 * @property severity Whether this is an [Error][ValidationSeverity.Error] or [Warning][ValidationSeverity.Warning].
 */
data class ValidationError(
    val path: String,
    val message: String,
    val code: ValidationErrorCode,
    val severity: ValidationSeverity = ValidationSeverity.Error,
)

/**
 * Controls the strictness of JSON validation.
 */
enum class ValidationMode {
    /**
     * Strict mode detects unrecognized JSON properties and reports them as warnings.
     * All other checks produce errors as usual.
     */
    Strict,
    /**
     * Permissive mode ignores unrecognized JSON properties.
     * Only structural and value errors are reported.
     */
    Permissive,
}

/**
 * Result of validating a [ComposeNode][com.jesusdmedinac.jsontocompose.model.ComposeNode] tree or JSON string.
 *
 * Collects all validation findings (no fail-fast). Use [isValid] to check if the tree
 * is renderable, and [isClean] to check if there are no findings at all.
 *
 * @property errors All validation findings (both errors and warnings).
 */
data class ValidationResult(
    val errors: List<ValidationError>,
) {
    /** `true` if there are no [Error][ValidationSeverity.Error]-level findings. */
    val isValid: Boolean
        get() = errors.none { it.severity == ValidationSeverity.Error }

    /** `true` if there are no findings at all (neither errors nor warnings). */
    val isClean: Boolean
        get() = errors.isEmpty()

    /** Returns only the [Error][ValidationSeverity.Error]-level findings. */
    fun errorsOnly(): List<ValidationError> =
        errors.filter { it.severity == ValidationSeverity.Error }

    /** Returns only the [Warning][ValidationSeverity.Warning]-level findings. */
    fun warningsOnly(): List<ValidationError> =
        errors.filter { it.severity == ValidationSeverity.Warning }
}
