package com.jesusdmedinac.jsontocompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.renderer.ToBox
import com.jesusdmedinac.jsontocompose.renderer.ToButton
import com.jesusdmedinac.jsontocompose.renderer.ToColumn
import com.jesusdmedinac.jsontocompose.renderer.ToCard
import com.jesusdmedinac.jsontocompose.renderer.ToAlertDialog
import com.jesusdmedinac.jsontocompose.renderer.ToCustom
import com.jesusdmedinac.jsontocompose.renderer.ToImage
import com.jesusdmedinac.jsontocompose.renderer.ToLazyColumn
import com.jesusdmedinac.jsontocompose.renderer.ToLazyRow
import com.jesusdmedinac.jsontocompose.renderer.ToRow
import com.jesusdmedinac.jsontocompose.renderer.ToScaffold
import com.jesusdmedinac.jsontocompose.renderer.ToText
import com.jesusdmedinac.jsontocompose.renderer.ToTextField
import com.jesusdmedinac.jsontocompose.renderer.ToBottomBar
import com.jesusdmedinac.jsontocompose.renderer.ToSwitch
import com.jesusdmedinac.jsontocompose.renderer.ToTopAppBar
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.DrawableResource

val LocalDrawableResources = staticCompositionLocalOf<Map<String, DrawableResource>> { emptyMap() }

val LocalBehavior = staticCompositionLocalOf<Map<String, Behavior>> { emptyMap() }

val LocalStateHost = staticCompositionLocalOf<Map<String, StateHost<*>>> { emptyMap() }

val LocalCustomRenderers = staticCompositionLocalOf<Map<String, @Composable (ComposeNode) -> Unit>> { emptyMap() }

@Composable
fun String.ToCompose() {
    Json.decodeFromString<ComposeNode>(this).ToCompose()
}

@Composable
fun ComposeNode.ToCompose() {
    when (type) {
        ComposeType.Column -> ToColumn()
        ComposeType.Row -> ToRow()
        ComposeType.Box -> ToBox()
        ComposeType.Text -> ToText()
        ComposeType.Button -> ToButton()
        ComposeType.Image -> ToImage()
        ComposeType.TextField -> ToTextField()
        ComposeType.LazyColumn -> ToLazyColumn()
        ComposeType.LazyRow -> ToLazyRow()
        ComposeType.Scaffold -> ToScaffold()
        ComposeType.Card -> ToCard()
        ComposeType.AlertDialog -> ToAlertDialog()
        ComposeType.TopAppBar -> ToTopAppBar()
        ComposeType.BottomBar -> ToBottomBar()
        ComposeType.Switch -> ToSwitch()
        ComposeType.Custom -> ToCustom()
    }
}
