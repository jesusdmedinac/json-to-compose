package com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.Alignment
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.exception.AlignmentException

fun String.toAlignment(): Alignment = when (this) {
    // 2D Alignments
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

fun String.toVerticalAlignment(): Alignment.Vertical = when (this) {
    // 1D Alignments.Verticals
    "Top" -> Alignment.Top
    "CenterVertically" -> Alignment.CenterVertically
    "Bottom" -> Alignment.Bottom
    else -> throw AlignmentException("Alignment $this not supported")
}

fun String.toHorizontalsAlignment(): Alignment.Horizontal = when (this) {
    // 1D Alignments.Horizontals
    "Start" -> Alignment.Start
    "CenterHorizontally" -> Alignment.CenterHorizontally
    "End" -> Alignment.End
    else -> throw AlignmentException("Alignment $this not supported")
}