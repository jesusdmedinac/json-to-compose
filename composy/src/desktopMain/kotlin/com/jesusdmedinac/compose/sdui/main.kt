package com.jesusdmedinac.compose.sdui

import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.jesusdmedinac.compose.sdui.presentation.ui.App
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.inputStream

@OptIn(ExperimentalResourceApi::class)
fun main() {
    val version = System.getProperty("app.version") ?: "Development"
    application {
        val appIcon = remember {
            System.getProperty("app.dir")
                ?.let { Paths.get(it, "icon-512.png") }
                ?.takeIf { it.exists() }
                ?.inputStream()
                ?.buffered()
                ?.use { BitmapPainter(it.readAllBytes().decodeToImageBitmap()) }
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "ComposeSDUI",
            state = WindowState(
                placement = WindowPlacement.Maximized,
            ),
            icon = appIcon
        ) {
            App()
        }
    }
}