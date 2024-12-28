package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.jesusdmedinac.compose.sdui.di.appModule
import com.jesusdmedinac.compose.sdui.presentation.ui.initializer.SupabaseClientInitializer
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    ComposyTheme(
        useDarkTheme = true
    ) {
        SupabaseClientInitializer {
            KoinApplication(application = {
                modules(appModule())
            }) {
                Navigator(AuthScreen)
            }
        }
    }
}