package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        Text("LoginScreen")
    }
}