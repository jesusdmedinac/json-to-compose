package com.jesusdmedinac.compose.sdui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.jesusdmedinac.compose.sdui.presentation.ui.App
import io.github.vinceglb.filekit.core.FileKit

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ComposeSDUI",
        state = WindowState(
            placement = WindowPlacement.Maximized,
        ),
        resizable = false
    ) {
        App()
    }
}