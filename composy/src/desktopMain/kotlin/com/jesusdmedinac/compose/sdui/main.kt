package com.jesusdmedinac.compose.sdui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.jesusdmedinac.compose.sdui.presentation.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ComposeSDUI",
    ) {
        App()
    }
}