package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

/**
 * Maps this string to a [FontWeight].
 * Supported: "Thin", "ExtraLight", "Light", "Normal", "Medium", "SemiBold", "Bold", "ExtraBold", "Black".
 */
fun String?.toFontWeight(): FontWeight = when (this) {
    "Thin" -> FontWeight.Thin
    "ExtraLight" -> FontWeight.ExtraLight
    "Light" -> FontWeight.Light
    "Normal" -> FontWeight.Normal
    "Medium" -> FontWeight.Medium
    "SemiBold" -> FontWeight.SemiBold
    "Bold" -> FontWeight.Bold
    "ExtraBold" -> FontWeight.ExtraBold
    "Black" -> FontWeight.Black
    else -> FontWeight.Normal
}

/**
 * Maps this string to a [FontStyle].
 * Supported: "Normal", "Italic".
 */
fun String?.toFontStyle(): FontStyle = when (this) {
    "Normal" -> FontStyle.Normal
    "Italic" -> FontStyle.Italic
    else -> FontStyle.Normal
}

/**
 * Maps this string to a [TextAlign].
 * Supported: "Left", "Right", "Center", "Justify", "Start", "End".
 */
fun String?.toTextAlign(): TextAlign = when (this) {
    "Left" -> TextAlign.Left
    "Right" -> TextAlign.Right
    "Center" -> TextAlign.Center
    "Justify" -> TextAlign.Justify
    "Start" -> TextAlign.Start
    "End" -> TextAlign.End
    else -> TextAlign.Start
}

/**
 * Maps this string to a [TextOverflow].
 * Supported: "Clip", "Ellipsis", "Visible".
 */
fun String?.toTextOverflow(): TextOverflow = when (this) {
    "Clip" -> TextOverflow.Clip
    "Ellipsis" -> TextOverflow.Ellipsis
    "Visible" -> TextOverflow.Visible
    else -> TextOverflow.Clip
}

/**
 * Maps this string to a [TextDecoration].
 * Supported: "None", "Underline", "LineThrough".
 */
fun String?.toTextDecoration(): TextDecoration = when (this) {
    "None" -> TextDecoration.None
    "Underline" -> TextDecoration.Underline
    "LineThrough" -> TextDecoration.LineThrough
    else -> TextDecoration.None
}
