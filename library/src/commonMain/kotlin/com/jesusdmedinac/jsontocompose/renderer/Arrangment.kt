package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.jsontocompose.exception.ArrangementException

/**
 * Maps this string to an arrangement usable for both horizontal and vertical axes.
 *
 * Supported values: "Center", "SpaceEvenly", "SpaceBetween", "SpaceAround", "SpacedBy:X".
 *
 * @throws ArrangementException if the value is not recognized.
 */
@Throws(ArrangementException::class)
fun String.toArrangement(): Arrangement.HorizontalOrVertical = when {
    this == "Center" -> Arrangement.Center
    this == "SpaceEvenly" -> Arrangement.SpaceEvenly
    this == "SpaceBetween" -> Arrangement.SpaceBetween
    this == "SpaceAround" -> Arrangement.SpaceAround
    this.startsWith("SpacedBy:") -> {
        val dp = this.substringAfter("SpacedBy:").toIntOrNull()
            ?: throw ArrangementException("SpacedBy value must be an integer, got $this")
        Arrangement.spacedBy(dp.dp)
    }
    else -> throw ArrangementException("Arrangement $this not supported")
}

/**
 * Maps this string to a horizontal [Arrangement.Horizontal].
 *
 * Supported standard values: "Start", "End", "Center", "SpaceEvenly", "SpaceBetween", "SpaceAround", "SpacedBy:X".
 * Supported absolute values: "AbsoluteLeft", "AbsoluteCenter", "AbsoluteRight",
 * "AbsoluteSpaceBetween", "AbsoluteSpaceEvenly", "AbsoluteSpaceAround".
 *
 * @throws ArrangementException if the value is not recognized.
 */
@Throws(ArrangementException::class)
fun String.toHorizontalArrangement(): Arrangement.Horizontal = when {
    this == "Start" -> Arrangement.Start
    this == "End" -> Arrangement.End
    this == "Center" -> Arrangement.Center
    this == "SpaceEvenly" -> Arrangement.SpaceEvenly
    this == "SpaceBetween" -> Arrangement.SpaceBetween
    this == "SpaceAround" -> Arrangement.SpaceAround
    this == "AbsoluteLeft" -> Arrangement.Absolute.Left
    this == "AbsoluteCenter" -> Arrangement.Absolute.Center
    this == "AbsoluteRight" -> Arrangement.Absolute.Right
    this == "AbsoluteSpaceBetween" -> Arrangement.Absolute.SpaceBetween
    this == "AbsoluteSpaceEvenly" -> Arrangement.Absolute.SpaceEvenly
    this == "AbsoluteSpaceAround" -> Arrangement.Absolute.SpaceAround
    this.startsWith("SpacedBy:") -> {
        val dp = this.substringAfter("SpacedBy:").toIntOrNull()
            ?: throw ArrangementException("SpacedBy value must be an integer, got $this")
        Arrangement.spacedBy(dp.dp)
    }
    else -> throw ArrangementException("Arrangement $this not supported")
}

/**
 * Maps this string to a vertical [Arrangement.Vertical].
 *
 * Supported values: "Top", "Bottom", "Center", "SpaceEvenly", "SpaceBetween", "SpaceAround", "SpacedBy:X".
 *
 * @throws ArrangementException if the value is not recognized.
 */
@Throws(ArrangementException::class)
fun String.toVerticalArrangement(): Arrangement.Vertical = when {
    this == "Top" -> Arrangement.Top
    this == "Bottom" -> Arrangement.Bottom
    this == "Center" -> Arrangement.Center
    this == "SpaceEvenly" -> Arrangement.SpaceEvenly
    this == "SpaceBetween" -> Arrangement.SpaceBetween
    this == "SpaceAround" -> Arrangement.SpaceAround
    this.startsWith("SpacedBy:") -> {
        val dp = this.substringAfter("SpacedBy:").toIntOrNull()
            ?: throw ArrangementException("SpacedBy value must be an integer, got $this")
        Arrangement.spacedBy(dp.dp)
    }
    else -> throw ArrangementException("Arrangement $this not supported")
}
