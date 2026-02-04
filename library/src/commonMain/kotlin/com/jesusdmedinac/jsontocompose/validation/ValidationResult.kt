package com.jesusdmedinac.jsontocompose.validation

enum class ValidationSeverity {
    Error,
    Warning,
}

enum class ValidationErrorCode {
    EMPTY_JSON,
    MALFORMED_JSON,
    UNKNOWN_COMPOSE_TYPE,
    PROPERTIES_TYPE_MISMATCH,
    UNKNOWN_MODIFIER_OPERATION,
    MODIFIER_VALUE_OUT_OF_RANGE,
    INVALID_HEX_COLOR,
    CHILDREN_ON_LEAF_NODE,
    INVALID_ARRANGEMENT,
    INVALID_ALIGNMENT,
    UNRECOGNIZED_PROPERTIES,
}

data class ValidationError(
    val path: String,
    val message: String,
    val code: ValidationErrorCode,
    val severity: ValidationSeverity = ValidationSeverity.Error,
)

enum class ValidationMode {
    Strict,
    Permissive,
}

data class ValidationResult(
    val errors: List<ValidationError>,
) {
    val isValid: Boolean
        get() = errors.none { it.severity == ValidationSeverity.Error }

    val isClean: Boolean
        get() = errors.isEmpty()

    fun errorsOnly(): List<ValidationError> =
        errors.filter { it.severity == ValidationSeverity.Error }

    fun warningsOnly(): List<ValidationError> =
        errors.filter { it.severity == ValidationSeverity.Warning }
}
