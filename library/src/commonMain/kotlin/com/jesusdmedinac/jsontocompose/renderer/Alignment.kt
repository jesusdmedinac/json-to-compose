package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.Alignment
import com.jesusdmedinac.jsontocompose.exception.AlignmentException

/**
 * Maps this string to a two-dimensional [Alignment].
 *
 * Supported values: "TopStart", "TopCenter", "TopEnd", "CenterStart", "Center",
 * "CenterEnd", "BottomStart", "BottomCenter", "BottomEnd".
 *
 * @throws AlignmentException if the value is not recognized.
 */
fun String.toAlignment(): Alignment = when (this) {
    "TopStart" -> Alignment.TopStart
    "TopCenter" -> Alignment.TopCenter
    "TopEnd" -> Alignment.TopEnd
    "CenterStart" -> Alignment.CenterStart
    "Center" -> Alignment.Center
    "CenterEnd" -> Alignment.CenterEnd
    "BottomStart" -> Alignment.BottomStart
    "BottomCenter" -> Alignment.BottomCenter
    "BottomEnd" -> Alignment.BottomEnd
    else -> throw AlignmentException("Alignment $this not supported")
}

/**
 * Maps this string to a vertical [Alignment.Vertical].
 *
 * Supported values: "Top", "CenterVertically", "Bottom".
 *
 * @throws AlignmentException if the value is not recognized.
 */
fun String.toVerticalAlignment(): Alignment.Vertical = when (this) {
    "Top" -> Alignment.Top
    "CenterVertically" -> Alignment.CenterVertically
    "Bottom" -> Alignment.Bottom
    else -> throw AlignmentException("Alignment $this not supported")
}

/**
 * Maps this string to a horizontal [Alignment.Horizontal].
 *
 * Supported values: "Start", "CenterHorizontally", "End".
 *
 * @throws AlignmentException if the value is not recognized.
 */
fun String.toHorizontalsAlignment(): Alignment.Horizontal = when (this) {
    "Start" -> Alignment.Start
    "CenterHorizontally" -> Alignment.CenterHorizontally
    "End" -> Alignment.End
    else -> throw AlignmentException("Alignment $this not supported")
}
