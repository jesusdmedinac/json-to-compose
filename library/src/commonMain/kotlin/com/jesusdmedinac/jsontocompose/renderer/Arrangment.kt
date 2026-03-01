package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Arrangement
import com.jesusdmedinac.jsontocompose.exception.ArrangementException

/**
 * Maps this string to an arrangement usable for both horizontal and vertical axes.
 *
 * Supported values: "Center", "SpaceEvenly", "SpaceBetween", "SpaceAround".
 *
 * @throws ArrangementException if the value is not recognized.
 */
@Throws(ArrangementException::class)
fun String.toArrangement(): Arrangement.HorizontalOrVertical? = when (this) {
    "Center" -> Arrangement.Center
    "SpaceEvenly" -> Arrangement.SpaceEvenly
    "SpaceBetween" -> Arrangement.SpaceBetween
    "SpaceAround" -> Arrangement.SpaceAround
    else -> throw ArrangementException("Arrangement $this not supported")
}

/**
 * Maps this string to a horizontal [Arrangement.Horizontal].
 *
 * Supported standard values: "Start", "End", "Center", "SpaceEvenly", "SpaceBetween", "SpaceAround".
 * Supported absolute values: "AbsoluteLeft", "AbsoluteCenter", "AbsoluteRight",
 * "AbsoluteSpaceBetween", "AbsoluteSpaceEvenly", "AbsoluteSpaceAround".
 *
 * @throws ArrangementException if the value is not recognized.
 */
@Throws(ArrangementException::class)
fun String.toHorizontalArrangement(): Arrangement.Horizontal = when (this) {
    "Start" -> Arrangement.Start
    "End" -> Arrangement.End
    "Center" -> Arrangement.Center
    "SpaceEvenly" -> Arrangement.SpaceEvenly
    "SpaceBetween" -> Arrangement.SpaceBetween
    "SpaceAround" -> Arrangement.SpaceAround
    "AbsoluteLeft" -> Arrangement.Absolute.Left
    "AbsoluteCenter" -> Arrangement.Absolute.Center
    "AbsoluteRight" -> Arrangement.Absolute.Right
    "AbsoluteSpaceBetween" -> Arrangement.Absolute.SpaceBetween
    "AbsoluteSpaceEvenly" -> Arrangement.Absolute.SpaceEvenly
    "AbsoluteSpaceAround" -> Arrangement.Absolute.SpaceAround
    else -> throw ArrangementException("Arrangement $this not supported")
}

/**
 * Maps this string to a vertical [Arrangement.Vertical].
 *
 * Supported values: "Top", "Bottom", "Center", "SpaceEvenly", "SpaceBetween", "SpaceAround".
 *
 * @throws ArrangementException if the value is not recognized.
 */
@Throws(ArrangementException::class)
fun String.toVerticalArrangement(): Arrangement.Vertical = when (this) {
    "Top" -> Arrangement.Top
    "Bottom" -> Arrangement.Bottom
    "Center" -> Arrangement.Center
    "SpaceEvenly" -> Arrangement.SpaceEvenly
    "SpaceBetween" -> Arrangement.SpaceBetween
    "SpaceAround" -> Arrangement.SpaceAround
    else -> throw ArrangementException("Arrangement $this not supported")
}
