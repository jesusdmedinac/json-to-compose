package com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Arrangement
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.exception.ArrangementException

@Throws(ArrangementException::class)
fun String.toArrangement(): Arrangement.HorizontalOrVertical? = when (this) {
    "Center" -> Arrangement.Center
    "SpaceEvenly" -> Arrangement.SpaceEvenly
    "SpaceBetween" -> Arrangement.SpaceBetween
    "SpaceAround" -> Arrangement.SpaceAround
    else -> throw ArrangementException("Arrangement $this not supported")
}

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
