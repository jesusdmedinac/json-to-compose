package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.jesusdmedinac.compose.sdui.auth.presentation.ui.AuthScreen
import com.jesusdmedinac.compose.sdui.di.appModule
import com.jesusdmedinac.compose.sdui.di.dataModule
import com.jesusdmedinac.compose.sdui.presentation.ui.screen.ChatScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(
            dataModule(),
            appModule()
        )
    }) {
        Navigator(MainScreen)
    }
}