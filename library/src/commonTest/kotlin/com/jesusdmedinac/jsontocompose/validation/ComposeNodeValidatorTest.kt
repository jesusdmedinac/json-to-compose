package com.jesusdmedinac.jsontocompose.validation

import com.jesusdmedinac.jsontocompose.model.ComposeModifier
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ComposeNodeValidatorTest {

    // --- Scenario 1: Valid JSON processes without errors ---

    @Test
    fun validJsonProducesNoErrors() {
        val json = """
            {
                "type": "Column",
                "properties": {
                    "type": "ColumnProps",
                    "children": [
                        {
                            "type": "Text",
                            "properties": {
                                "type": "TextProps",
                                "text": "Hello"
                            }
                        }
                    ],
                    "verticalArrangement": "Center",
                    "horizontalAlignment": "CenterHorizontally"
                }
            }
        """.trimIndent()

        val result = json.validateJson()

        assertTrue(result.isValid)
        assertTrue(result.isClean)
        assertEquals(0, result.errors.size)
    }

    @Test
    fun validNodeProducesNoErrors() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
        )

        val result = node.validate()

        assertTrue(result.isValid)
        assertTrue(result.isClean)
    }

    // --- Scenario 2: JSON with unknown component type ---

    @Test
    fun unknownComposeTypeProducesError() {
        val json = """
            {
                "type": "FooBar",
                "properties": {
                    "type": "TextProps",
                    "text": "Hello"
                }
            }
        """.trimIndent()

        val result = json.validateJson()

        assertFalse(result.isValid)
        assertEquals(1, result.errorsOnly().size)
        assertEquals(ValidationErrorCode.UNKNOWN_COMPOSE_TYPE, result.errorsOnly()[0].code)
        assertTrue(result.errorsOnly()[0].message.contains("FooBar"))
        assertEquals("root", result.errorsOnly()[0].path)
    }

    // --- Scenario 3: JSON with properties incompatible with type ---

    @Test
    fun propertiesTypeMismatchProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.ColumnProps(
                children = emptyList(),
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(1, result.errorsOnly().size)
        assertEquals(ValidationErrorCode.PROPERTIES_TYPE_MISMATCH, result.errorsOnly()[0].code)
        assertTrue(result.errorsOnly()[0].message.contains("TextProps"))
        assertTrue(result.errorsOnly()[0].message.contains("ColumnProps"))
    }

    // --- Scenario 4: JSON with unknown modifier operation ---

    @Test
    fun unknownModifierOperationProducesError() {
        val json = """
            {
                "type": "Text",
                "properties": {
                    "type": "TextProps",
                    "text": "Hello"
                },
                "composeModifier": {
                    "type": "ComposeModifier",
                    "operations": [
                        {
                            "type": "UnknownModifier",
                            "value": 10
                        }
                    ]
                }
            }
        """.trimIndent()

        val result = json.validateJson()

        assertFalse(result.isValid)
        val modifierErrors = result.errorsOnly().filter {
            it.code == ValidationErrorCode.UNKNOWN_MODIFIER_OPERATION
        }
        assertEquals(1, modifierErrors.size)
        assertTrue(modifierErrors[0].message.contains("UnknownModifier"))
        assertTrue(modifierErrors[0].path.contains("operations[0]"))
    }

    // --- Scenario 5: JSON with invalid tree structure (child in leaf node) ---

    @Test
    fun childrenOnLeafNodeProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "child"),
                    )
                ),
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        val leafErrors = result.errorsOnly().filter {
            it.code == ValidationErrorCode.CHILDREN_ON_LEAF_NODE
        }
        assertEquals(1, leafErrors.size)
        assertTrue(leafErrors[0].message.contains("Text"))
    }

    @Test
    fun imageWithChildrenProducesError() {
        val node = ComposeNode(
            type = ComposeType.Image,
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "child"),
                    )
                ),
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertTrue(result.errorsOnly().any { it.code == ValidationErrorCode.CHILDREN_ON_LEAF_NODE })
    }

    // --- Scenario 6: JSON with out-of-range modifier value ---

    @Test
    fun negativePaddingProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(value = -5),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(1, result.errorsOnly().size)
        assertEquals(ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE, result.errorsOnly()[0].code)
        assertTrue(result.errorsOnly()[0].message.contains("-5"))
    }

    @Test
    fun negativeWidthProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Width(value = -10),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE, result.errorsOnly()[0].code)
    }

    @Test
    fun negativeHeightProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Height(value = -1),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE, result.errorsOnly()[0].code)
    }

    @Test
    fun negativeBorderWidthProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Border(width = -2, color = "#FF000000"),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertTrue(result.errorsOnly().any { it.code == ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE })
    }

    @Test
    fun negativeShadowElevationProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Shadow(elevation = -3),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE, result.errorsOnly()[0].code)
    }

    @Test
    fun alphaOutOfRangeProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Alpha(value = 1.5f),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE, result.errorsOnly()[0].code)
        assertTrue(result.errorsOnly()[0].message.contains("1.5"))
    }

    @Test
    fun alphaNegativeProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Alpha(value = -0.1f),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE, result.errorsOnly()[0].code)
    }

    // --- Scenario 7: JSON with invalid hex color ---

    @Test
    fun invalidHexColorInBorderProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Border(width = 1, color = "red"),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertTrue(result.errorsOnly().any { it.code == ValidationErrorCode.INVALID_HEX_COLOR })
    }

    @Test
    fun invalidHexColorInBackgroundColorProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.BackgroundColor(hexColor = "#FFF"),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertTrue(result.errorsOnly().any { it.code == ValidationErrorCode.INVALID_HEX_COLOR })
    }

    @Test
    fun invalidHexColorInBackgroundProducesError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Background(color = "notacolor"),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertTrue(result.errorsOnly().any { it.code == ValidationErrorCode.INVALID_HEX_COLOR })
    }

    @Test
    fun validHexColorProducesNoError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Border(width = 1, color = "#FF000000"),
                )
            ),
        )

        val result = node.validate()

        assertTrue(result.isClean)
    }

    // --- Scenario 8: JSON with invalid arrangement ---

    @Test
    fun invalidVerticalArrangementProducesError() {
        val node = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                verticalArrangement = "InvalidArrangement",
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(1, result.errorsOnly().size)
        assertEquals(ValidationErrorCode.INVALID_ARRANGEMENT, result.errorsOnly()[0].code)
        assertTrue(result.errorsOnly()[0].message.contains("InvalidArrangement"))
    }

    @Test
    fun invalidHorizontalArrangementProducesError() {
        val node = ComposeNode(
            type = ComposeType.Row,
            properties = NodeProperties.RowProps(
                horizontalArrangement = "Bogus",
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.INVALID_ARRANGEMENT, result.errorsOnly()[0].code)
    }

    @Test
    fun invalidHorizontalAlignmentOnColumnProducesError() {
        val node = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                horizontalAlignment = "TopStart",
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.INVALID_ALIGNMENT, result.errorsOnly()[0].code)
    }

    @Test
    fun invalidVerticalAlignmentOnRowProducesError() {
        val node = ComposeNode(
            type = ComposeType.Row,
            properties = NodeProperties.RowProps(
                verticalAlignment = "BottomCenter",
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.INVALID_ALIGNMENT, result.errorsOnly()[0].code)
    }

    @Test
    fun invalidContentAlignmentOnBoxProducesError() {
        val node = ComposeNode(
            type = ComposeType.Box,
            properties = NodeProperties.BoxProps(
                contentAlignment = "NotAnAlignment",
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.INVALID_ALIGNMENT, result.errorsOnly()[0].code)
    }

    @Test
    fun validArrangementsProduceNoErrors() {
        val node = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                verticalArrangement = "SpaceBetween",
                horizontalAlignment = "End",
            ),
        )

        val result = node.validate()

        assertTrue(result.isClean)
    }

    // --- Scenario 9: Empty JSON ---

    @Test
    fun emptyJsonProducesError() {
        val result = "".validateJson()

        assertFalse(result.isValid)
        assertEquals(1, result.errors.size)
        assertEquals(ValidationErrorCode.EMPTY_JSON, result.errors[0].code)
    }

    @Test
    fun blankJsonProducesError() {
        val result = "   ".validateJson()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.EMPTY_JSON, result.errors[0].code)
    }

    // --- Scenario 10: Malformed JSON (invalid syntax) ---

    @Test
    fun malformedJsonProducesError() {
        val result = "{not valid json}".validateJson()

        assertFalse(result.isValid)
        assertEquals(1, result.errors.size)
        assertEquals(ValidationErrorCode.MALFORMED_JSON, result.errors[0].code)
    }

    @Test
    fun truncatedJsonProducesError() {
        val result = """{"type": "Text", "properties":""".validateJson()

        assertFalse(result.isValid)
        assertEquals(ValidationErrorCode.MALFORMED_JSON, result.errors[0].code)
    }

    // --- Scenario 11: Validation in strict vs permissive mode ---

    @Test
    fun permissiveModeIgnoresUnrecognizedProperties() {
        val json = """
            {
                "type": "Text",
                "properties": {
                    "type": "TextProps",
                    "text": "Hello"
                },
                "unknownField": "should be ignored"
            }
        """.trimIndent()

        val result = json.validateJson(mode = ValidationMode.Permissive)

        assertTrue(result.isValid)
        assertEquals(0, result.warningsOnly().size)
    }

    @Test
    fun strictModeDetectsUnrecognizedProperties() {
        val json = """
            {
                "type": "Text",
                "properties": {
                    "type": "TextProps",
                    "text": "Hello"
                },
                "unknownField": "should trigger warning"
            }
        """.trimIndent()

        val result = json.validateJson(mode = ValidationMode.Strict)

        assertTrue(result.isValid, "Unrecognized properties should be warnings, not errors")
        assertFalse(result.isClean)
        assertEquals(1, result.warningsOnly().size)
        assertEquals(ValidationErrorCode.UNRECOGNIZED_PROPERTIES, result.warningsOnly()[0].code)
    }

    // --- Edge cases ---

    @Test
    fun multipleErrorsAreCollected() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Padding(value = -5),
                    ComposeModifier.Operation.Alpha(value = 2.0f),
                    ComposeModifier.Operation.BackgroundColor(hexColor = "invalid"),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(3, result.errorsOnly().size)
    }

    @Test
    fun deepPathsAreCorrectlyReported() {
        val node = ComposeNode(
            type = ComposeType.Column,
            properties = NodeProperties.ColumnProps(
                children = listOf(
                    ComposeNode(
                        type = ComposeType.Row,
                        properties = NodeProperties.RowProps(
                            children = listOf(
                                ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Deep"),
                                    composeModifier = ComposeModifier(
                                        operations = listOf(
                                            ComposeModifier.Operation.Padding(value = -1),
                                        )
                                    ),
                                )
                            ),
                        ),
                    )
                ),
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(1, result.errorsOnly().size)
        assertEquals(
            "root.children[0].children[0].composeModifier.operations[0]",
            result.errorsOnly()[0].path,
        )
    }

    @Test
    fun nullPropertiesProducesNoError() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = null,
        )

        val result = node.validate()

        assertTrue(result.isValid)
        assertTrue(result.isClean)
    }

    @Test
    fun borderWithInvalidColorAndNegativeWidthProducesBothErrors() {
        val node = ComposeNode(
            type = ComposeType.Text,
            properties = NodeProperties.TextProps(text = "Hello"),
            composeModifier = ComposeModifier(
                operations = listOf(
                    ComposeModifier.Operation.Border(width = -1, color = "bad"),
                )
            ),
        )

        val result = node.validate()

        assertFalse(result.isValid)
        assertEquals(2, result.errorsOnly().size)
        val codes = result.errorsOnly().map { it.code }.toSet()
        assertTrue(ValidationErrorCode.MODIFIER_VALUE_OUT_OF_RANGE in codes)
        assertTrue(ValidationErrorCode.INVALID_HEX_COLOR in codes)
    }

    @Test
    fun validJsonFromStringWithModifiers() {
        val json = """
            {
                "type": "Text",
                "properties": {
                    "type": "TextProps",
                    "text": "Hello"
                },
                "composeModifier": {
                    "type": "ComposeModifier",
                    "operations": [
                        {
                            "type": "Padding",
                            "value": 16
                        },
                        {
                            "type": "Alpha",
                            "value": 0.5
                        }
                    ]
                }
            }
        """.trimIndent()

        val result = json.validateJson()

        assertTrue(result.isValid)
        assertTrue(result.isClean)
    }

    @Test
    fun unknownTypeInNestedChildProducesErrorWithPath() {
        val json = """
            {
                "type": "Column",
                "properties": {
                    "type": "ColumnProps",
                    "children": [
                        {
                            "type": "InvalidWidget"
                        }
                    ]
                }
            }
        """.trimIndent()

        val result = json.validateJson()

        assertFalse(result.isValid)
        val unknownErrors = result.errorsOnly().filter {
            it.code == ValidationErrorCode.UNKNOWN_COMPOSE_TYPE
        }
        assertEquals(1, unknownErrors.size)
        assertEquals("root.children[0]", unknownErrors[0].path)
    }

    @Test
    fun errorsOnlyAndWarningsOnlyFilterCorrectly() {
        val result = ValidationResult(
            errors = listOf(
                ValidationError("a", "err", ValidationErrorCode.EMPTY_JSON, ValidationSeverity.Error),
                ValidationError("b", "warn", ValidationErrorCode.UNRECOGNIZED_PROPERTIES, ValidationSeverity.Warning),
                ValidationError("c", "err2", ValidationErrorCode.MALFORMED_JSON, ValidationSeverity.Error),
            )
        )

        assertEquals(2, result.errorsOnly().size)
        assertEquals(1, result.warningsOnly().size)
        assertFalse(result.isValid)
        assertFalse(result.isClean)
    }

    @Test
    fun resultWithOnlyWarningsIsValidButNotClean() {
        val result = ValidationResult(
            errors = listOf(
                ValidationError("a", "warn", ValidationErrorCode.UNRECOGNIZED_PROPERTIES, ValidationSeverity.Warning),
            )
        )

        assertTrue(result.isValid)
        assertFalse(result.isClean)
    }
}
